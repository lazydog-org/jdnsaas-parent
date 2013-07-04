/**
 * Copyright 2013 lazydog.org.
 *
 * This file is part of JDNSaaS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lazydog.jdnsaas.bind.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.lazydog.jdnsaas.NotifyMessageMonitorAddress;
import org.lazydog.jdnsaas.NotifyMessageMonitorPort;
import org.lazydog.jdnsaas.NotifyMessageMonitorThreads;
import org.lazydog.jdnsaas.RefreshZoneSchedulerInitialDelay;
import org.lazydog.jdnsaas.RefreshZoneSchedulerThreads;
import org.lazydog.jdnsaas.bind.DNSServerExecutor;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.SOARecord;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zone cache.
 * 
 * @author  Ron Rickard
 */
@ApplicationScoped 
@Eager
public class ZoneCache {

    private static final Logger logger = LoggerFactory.getLogger(ZoneCache.class);
    private static final Long SHUTDOWN_TIMEOUT = 30000L;
    private boolean isAvailable;
    private Runnable notifyMessageMonitor;
    private String notifyMessageMonitorAddress;
    private ExecutorService notifyMessageMonitorPool;
    private int notifyMessageMonitorPort;
    private int notifyMessageMonitorThreads;
    private ConcurrentMap<ZoneAction,Set<Zone>> refreshZoneMap = new ConcurrentHashMap<ZoneAction,Set<Zone>>();
    private Runnable refreshZoneScheduler;
    private int refreshZoneSchedulerInitialDelay;
    private ExecutorService refreshZoneSchedulerPool;
    private int refreshZoneSchedulerThreads;
    private JDNSaaSRepository repository;
    private ConcurrentMap<Zone,List<Record>> zoneMap = new ConcurrentHashMap<Zone,List<Record>>();
    private enum ZoneAction {
        ADD,
        DELETE,
        UPDATE;
    }
          
    /**
     * Add the zone to the zone cache.
     * 
     * @param  zone  the zone.
     */
    private void addZone(final Zone zone) {

        try {

            // Add the zone to the zone cache.
            List<Record> records = DNSServerExecutor.newInstance(zone).findRecords();
            this.zoneMap.put(zone, records);
            logger.debug("Added the zone {} with {} records to the zone cache.", zone, records.size());
        } catch (Exception e) {
            logger.warn("Unable to add the zone {} to the zone cache.", zone, e);
        }
    }
    
    /**
     * Delete the zone from the zone cache.
     * 
     * @param  zone  the zone.
     */
    private void deleteZone(final Zone zone) {
        List<Record> records = this.zoneMap.remove(zone);
        logger.debug("Deleted the zone {} with {} records from the zone cache.", zone, records.size());
    }

    /**
     * Find the records.
     * 
     * @param  viewName  the view name.
     * @param  zoneName  the zone name.
     * 
     * @return  the records.
     */
    public List<Record> findRecords(final Zone zone) {
        
        List<Record> records = new ArrayList<Record>();
        
        // Loop through the cached zones in the zone map.
        for (Zone cachedZone : this.zoneMap.keySet()) {
            
            // Check if the cached zone is the desired zone.
            if (cachedZone.equals(zone)) {
                records.addAll(this.zoneMap.get(cachedZone));
                break;
            }
        }
        
        return records;
    }
    
    /**
     * Flag the zone for a refresh.
     * 
     * @param  zone  the zone.
     */
    protected synchronized void flagZoneForRefresh(final Zone zone) {
        if (this.refreshZoneMap.containsKey(ZoneAction.UPDATE)) {
            this.refreshZoneMap.get(ZoneAction.UPDATE).add(zone);
        } else {
            this.refreshZoneMap.put(ZoneAction.UPDATE, new HashSet<Zone>(Arrays.asList(zone)));
        }
        logger.debug("Flagged the zone {} for refresh.", zone);
    }
    
    /**
     * Flag the zone for a refresh.
     * 
     * @param  zoneName  the zone name.
     */
    protected synchronized void flagZoneForRefresh(final String zoneName) {

        // Loop through the zones in the zone map.
        for (Zone zone : this.zoneMap.keySet()) {

            // Check if the zone is found.
            if (zone.getName().equals(zoneName)) {
                this.flagZoneForRefresh(zone);
            }
        }
    }

    /**
     * Get the refresh interval for the zone from the zone cache.
     * 
     * @param  zone  the zone.
     * 
     * @return  the refresh interval.
     */
    private long getRefreshInterval(final Zone zone) {
        
        long refreshInterval = 0L;

        List<Record> records = this.zoneMap.get(zone);
        
        for (Record record : records) {
            
            if (record.getType() == RecordType.SOA) {
                refreshInterval = ((SOARecord)record).getRefreshInterval();
                break;
            }
        }
        
        return refreshInterval;
    }

    /**
     * Get the serial number for the zone from the zone cache.
     * 
     * @param  zone  the zone.
     * 
     * @return  the serial number.
     */
    private long getSerialNumberFromCache(final Zone zone) {
        
        long serialNumber = 0L;
        
        List<Record> records = this.zoneMap.get(zone);
        
        for (Record record : records) {
            
            if (record.getType() == RecordType.SOA) {
                serialNumber = ((SOARecord)record).getSerialNumber();
                break;
            }
        }
        
        return serialNumber;
    }
    
    /**
     * Get the serial number for the zone from DNS.
     * 
     * @param  zone  the zone.
     * 
     * @return  the serial number.
     */
    private long getSerialNumberFromDNS(final Zone zone) {
        
        long serialNumber = 0L;
        
        try {

            List<Record> soaRecords = DNSServerExecutor.newInstance(zone).findRecords(RecordType.SOA, "@");

            if (soaRecords.size() > 0) {
                serialNumber = ((SOARecord)soaRecords.get(0)).getSerialNumber();
            }

        } catch (Exception e) {
            logger.warn("Unable to find the SOA record for the zone {}.", zone, e);
        }

        return serialNumber;
    }

    /**
     * Is the zone cache available?
     * 
     * @return  true if the zone cache is available, otherwise false.
     */
    public boolean isAvailable() {
        return this.isAvailable;
    }
    
    /**
     * Refresh the zone cache.
     */
    protected synchronized void refresh() {
        
        try {
            
            // Get the zones from the repository.
            List<Zone> repositoryZones = this.repository.findZones();
            
            // Determine which zones need to be added to the cache.
            Set<Zone> zonesToAdd = new HashSet<Zone>(repositoryZones);
            zonesToAdd.removeAll(this.zoneMap.keySet());
            if (!zonesToAdd.isEmpty()) {
                this.refreshZoneMap.put(ZoneAction.ADD, zonesToAdd);
                logger.debug("Found {} zones to add to the zone cache.", zonesToAdd.size());
            }
            
            // Determine which zones need to be deleted from the cache.
            Set<Zone> zonesToDelete = new HashSet<Zone>(this.zoneMap.keySet());
            zonesToDelete.removeAll(repositoryZones);
            if (!zonesToDelete.isEmpty()) {
                this.refreshZoneMap.put(ZoneAction.DELETE, zonesToDelete);
                logger.debug("Found {} zones to delete from the zone cache.", zonesToDelete.size());
            }
        } catch (Exception e) {
            logger.warn("Unable to find the zones in the database.  Using the existing zone map.", e);
        }

        // Check if a refresh is necessary.
        if (!this.refreshZoneMap.isEmpty()) {

            // Handle adding zones to the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.ADD)) {
            
                for (Zone zone : this.refreshZoneMap.get(ZoneAction.ADD)) {

                    // Add the zone to the cache.
                    this.addZone(zone);
                    
                    // Schedule a refresh for the zone.
                    ((RefreshZoneScheduler)this.refreshZoneScheduler).schedule(zone, this.getRefreshInterval(zone));
                }
            }
            
            // Handle deleting zones from the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.DELETE)) {
                
                for (Zone zone : this.refreshZoneMap.get(ZoneAction.DELETE)) {

                    // Delete the zone from the cache.
                    this.deleteZone(zone);
                    
                    // Unschedule a refresh for the zone.
                    ((RefreshZoneScheduler)this.refreshZoneScheduler).unschedule(zone);
                }
            }

            // Handle updating zones in the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.UPDATE)) {
                
                for (Zone zone : this.refreshZoneMap.get(ZoneAction.UPDATE)) {

                    // Update the zone in the cache.
                    this.updateZone(zone);
                    
                    // Rechedule a refresh for the zone.
                    ((RefreshZoneScheduler)this.refreshZoneScheduler).reschedule(zone, this.getRefreshInterval(zone));
                }
            }
            
            // Clear the refresh zone map.
            this.refreshZoneMap.clear();
        }
    }
    
    /**
     * Refresh the zone cache and make it available.
     */
    public synchronized void resume() {
        refresh();
        this.isAvailable = true;
    }

    /**
     * Set the repository.
     * 
     * @param  repository  the repository.
     */
    @Inject
    public void setRepository(final JDNSaaSRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Set the notify message monitor address.
     * 
     * @param  notifyMessageMonitorAddress  the notify message monitor address.
     */
    @Inject 
    public void setNotifyMessageMonitorAddress(@NotifyMessageMonitorAddress final String notifyMessageMonitorAddress) {
        this.notifyMessageMonitorAddress = notifyMessageMonitorAddress;
        logger.info("Set the notify message monitor address to {}.", notifyMessageMonitorAddress);
    }
        
    /**
     * Set the notify message monitor port.
     * 
     * @param  notifyMessageMonitorPort  the notify message monitor port.
     */
    @Inject 
    public void setNotifyMessageMonitorPort(@NotifyMessageMonitorPort final int notifyMessageMonitorPort) {
        this.notifyMessageMonitorPort = notifyMessageMonitorPort;
        logger.info("Set the notify message monitor port to {}.", notifyMessageMonitorPort);
    }

    /**
     * Set the notify message monitor threads.
     * 
     * @param  notifyMessageMonitorThreads  the notify message monitor threads.
     */
    @Inject 
    public void setNotifyMessageMonitorThreads(@NotifyMessageMonitorThreads final int notifyMessageMonitorThreads) {
        this.notifyMessageMonitorThreads = notifyMessageMonitorThreads;
        logger.info("Set the notify message monitor threads to {}.", notifyMessageMonitorThreads);
    }
        
    /**
     * Set the refresh zone scheduler initial delay.
     * 
     * @param  refreshZoneSchedulerInitialDelay  the refresh zone scheduler initial delay.
     */
    @Inject 
    public void setRefreshZoneSchedulerInitialDelay(@RefreshZoneSchedulerInitialDelay final int refreshZoneSchedulerInitialDelay) {
        this.refreshZoneSchedulerInitialDelay = refreshZoneSchedulerInitialDelay;
        logger.info("Set the refresh zone scheduler initial delay to {}.", refreshZoneSchedulerInitialDelay);
    }
    
    /**
     * Set the refresh zone scheduler threads.
     * 
     * @param  refreshZoneSchedulerThreads  the refresh zone scheduler threads.
     */
    @Inject 
    public void setRefreshZoneSchedulerThreads(@RefreshZoneSchedulerThreads final int refreshZoneSchedulerThreads) {
        this.refreshZoneSchedulerThreads = refreshZoneSchedulerThreads;
        logger.info("Set the refresh zone scheduler threads to {}.", refreshZoneSchedulerThreads);
    }
    
    /**
     * Shutdown the zone cache.
     */
    @PreDestroy
    public synchronized void shutdown() throws InterruptedException {
        
        logger.info("Stop the zone cache ...");
        this.suspend();
        
        // Shutdown the notify message monitor and refresh zone scheduler.
        ((NotifyMessageMonitor)this.notifyMessageMonitor).shutdown();
        this.notifyMessageMonitorPool.shutdown();
        ((RefreshZoneScheduler)this.refreshZoneScheduler).shutdown();
        this.refreshZoneSchedulerPool.shutdown();
        
        // Wait for the notify message monitor and refresh zone scheduler to shutdown.
        this.notifyMessageMonitorPool.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
        this.refreshZoneSchedulerPool.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
        logger.info("Zone cache stopped.");
    }

    /**
     * Startup the zone cache.
     * 
     * @throws  IOException  if unable to zone the record cache.
     */
    @PostConstruct
    public synchronized void startup() throws IOException {
        
        logger.info("Start the zone cache ...");
        this.suspend();
        this.refreshZoneScheduler = new RefreshZoneScheduler(this, this.refreshZoneSchedulerThreads, this.refreshZoneSchedulerInitialDelay);
        this.refreshZoneSchedulerPool = Executors.newSingleThreadExecutor();
        this.refreshZoneSchedulerPool.execute(this.refreshZoneScheduler);
        this.notifyMessageMonitor = new NotifyMessageMonitor(this, this.notifyMessageMonitorAddress, this.notifyMessageMonitorPort, this.notifyMessageMonitorThreads);
        this.notifyMessageMonitorPool = Executors.newSingleThreadExecutor();
        this.notifyMessageMonitorPool.execute(this.notifyMessageMonitor);
        logger.info("Zone cache started.");
    }
        
    /**
     * Make the zone cache unavailable.
     */
    public synchronized void suspend() {
        this.isAvailable = false;
    }  
    
    /**
     * Update the zone in the zone cache.
     * 
     * @param  zone  the zone.
     */
    private void updateZone(final Zone zone) {
 
        try {
            
            // Get the DNS and cache serial numbers for the zone.
            long dnsSerialNumber = getSerialNumberFromDNS(zone);
            long cacheSerialNumber = getSerialNumberFromCache(zone);

            logger.debug("Comparing the DNS serial number {} to the zone cache serial number {} for the zone {}.", dnsSerialNumber, cacheSerialNumber, zone);
            if (dnsSerialNumber > cacheSerialNumber) {
                logger.debug("{} records in the zone {} prior to the zone cache update.", this.zoneMap.get(zone).size(), zone);

                // Update the zone in the zone cache.
                cacheSerialNumber = DNSServerExecutor.newInstance(zone).updateRecords(this.zoneMap.get(zone));
                logger.debug("{} records in the zone {} after the zone cache update.", this.zoneMap.get(zone).size(), zone);
                logger.debug("The DNS serial number is {} and the zone cache serial number is {} for the zone {}.", dnsSerialNumber, cacheSerialNumber, zone);
            }
        } catch (Exception e) {
            logger.warn("Unable to update the zone {} in the zone cache.", zone, e);
        }
    }
}
