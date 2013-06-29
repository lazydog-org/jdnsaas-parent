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
import org.lazydog.jdnsaas.model.ZoneIdentity;
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
    private ConcurrentMap<ZoneAction,Set<ZoneIdentity>> refreshZoneMap = new ConcurrentHashMap<ZoneAction,Set<ZoneIdentity>>();
    private Runnable refreshZoneScheduler;
    private int refreshZoneSchedulerInitialDelay;
    private ExecutorService refreshZoneSchedulerPool;
    private int refreshZoneSchedulerThreads;
    private JDNSaaSRepository repository;
    private ConcurrentMap<ZoneIdentity,List<Record>> zoneMap = new ConcurrentHashMap<ZoneIdentity,List<Record>>();
    private enum ZoneAction {
        ADD,
        DELETE,
        UPDATE;
    }

    /**
     * Flag the zone for a refresh.
     * 
     * @param  zoneIdentity  the zone identity.
     */
    protected synchronized void flagForRefresh(final ZoneIdentity zoneIdentity) {
        if (this.refreshZoneMap.containsKey(ZoneAction.UPDATE)) {
            this.refreshZoneMap.get(ZoneAction.UPDATE).add(zoneIdentity);
        } else {
            this.refreshZoneMap.put(ZoneAction.UPDATE, new HashSet<ZoneIdentity>(Arrays.asList(zoneIdentity)));
        }
        logger.debug("Flagged the zone {} for refresh.", zoneIdentity);
    }
    
    /**
     * Flag the zone for a refresh.
     * 
     * @param  zoneName  the zone name.
     */
    protected synchronized void flagForRefresh(final String zoneName) {

        // Loop through the zone identities in the zone map.
        for (ZoneIdentity zoneIdentity : this.zoneMap.keySet()) {

            // Check if the zone is found.
            if (zoneIdentity.getZoneName().equals(zoneName)) {
                this.flagForRefresh(zoneIdentity);
            }
        }
    }
 
    /**
     * Get the records for the specified zone identity from DNS.
     * 
     * @param  zoneIdentity  the zone identity.
     * 
     * @return  the records.
     */
    private List<Record> getRecordsFromDNS(final ZoneIdentity zoneIdentity) {
        
        List<Record> records = new ArrayList<Record>();
        
        try {

            // Get the records for the zone.
            Zone zone = this.repository.findZone(zoneIdentity.getViewName(), zoneIdentity.getZoneName());
            records = DNSServerExecutor.newInstance(zone.getView().getResolvers(), null, zone.getTransferTSIGKey(), null, zone.getName()).findRecords(RecordType.ANY, null);
        } catch (Exception e) {
            logger.warn("Unable to find the records for the zone {}.", zoneIdentity, e);
        }
        
        return records;
    }
     
    /**
     * Get the records for the specified zone identity from DNS.
     * 
     * @param  zoneIdentity  the zone identity.
     * @param  records       the records.
     * 
     * @return  the records.
     */
    private List<Record> getRecordsFromDNS(final ZoneIdentity zoneIdentity, List<Record> records) {
 
        try {

            // Get the records for the zone.
            Zone zone = this.repository.findZone(zoneIdentity.getViewName(), zoneIdentity.getZoneName());
            records = DNSServerExecutor.newInstance(zone.getView().getResolvers(), null, zone.getTransferTSIGKey(), null, zone.getName()).updateRecords(records);
        } catch (Exception e) {
            logger.warn("Unable to find the records for the zone {}.", zoneIdentity, e);
        }
        
        return records;
    }
    
    /**
     * Get the records for the specified zone identity from the zone cache.
     * 
     * @param  zoneIdentity  the zone identity.
     * 
     * @return  the records.
     */
    private List<Record> getRecordsFromCache(ZoneIdentity zoneIdentity) {
        return this.zoneMap.get(zoneIdentity);
    }
        
    /**
     * Get the refresh interval for the specified zone identity from the zone cache.
     * 
     * @param  zoneIdentity  the zone identity.
     * @param  zoneMap       the zone map.
     * 
     * @return  the refresh interval.
     */
    private static long getRefreshInterval(ZoneIdentity zoneIdentity, ConcurrentMap<ZoneIdentity,List<Record>> zoneMap) {
        
        long refreshInterval = 0L;

        List<Record> records = zoneMap.get(zoneIdentity);
        
        for (Record record : records) {
            
            if (record.getType() == RecordType.SOA) {
                refreshInterval = ((SOARecord)record).getData().getRefreshInterval();
                break;
            }
        }
        
        return refreshInterval;
    }

    /**
     * Get the serial number for the specified zone identity from DNS.
     * 
     * @param  zoneIdentity  the zone identity.
     * 
     * @return  the serial number.
     */
    private long getSerialNumberFromDNS(ZoneIdentity zoneIdentity) {
        
        long serialNumber = 0L;
        
        try {
                        
            Zone zone = this.repository.findZone(zoneIdentity.getViewName(), zoneIdentity.getZoneName());
            List<Record> soaRecords = DNSServerExecutor.newInstance(zone.getView().getResolvers(), null, zone.getTransferTSIGKey(), null, zone.getName()).findRecords(RecordType.SOA, "@");

            if (soaRecords.size() > 0) {
                serialNumber = ((SOARecord)soaRecords.get(0)).getData().getSerialNumber();
            }

        } catch (Exception e) {
            logger.warn("Unable to find the SOA record for the zone {}.", zoneIdentity, e);
        }

        return serialNumber;
    }

    /**
     * Get the serial number for the specified zone identity from the zone cache.
     * 
     * @param  zoneIdentity  the zone identity.
     * 
     * @return  the serial number.
     */
    private long getSerialNumberFromCache(ZoneIdentity zoneIdentity) {
        
        long serialNumber = 0L;
        
        List<Record> records = this.zoneMap.get(zoneIdentity);
        
        for (Record record : records) {
            
            if (record.getType() == RecordType.SOA) {
                serialNumber = ((SOARecord)record).getData().getSerialNumber();
                break;
            }
        }
        
        return serialNumber;
    }
    
    /**
     * Get the zone identities.
     * 
     * @param  zones  the zones to get the zone identities for.
     * 
     * @return  the zone identities.
     */
    private static Set<ZoneIdentity> getZoneIdentities(List<Zone> zones) {
        
        Set<ZoneIdentity> zoneIdentities = new HashSet<ZoneIdentity>();
        
        for (Zone zone : zones) {
            zoneIdentities.add(ZoneIdentity.newInstance(zone));
        }
        
        return zoneIdentities;
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
            Set<ZoneIdentity> repositoryZoneIdentities = getZoneIdentities(repositoryZones);
            
            // Determine which zones need to be added to the cache.
            Set<ZoneIdentity> addZoneIdentities = new HashSet<ZoneIdentity>(repositoryZoneIdentities);
            addZoneIdentities.removeAll(this.zoneMap.keySet());
            if (!addZoneIdentities.isEmpty()) {
                this.refreshZoneMap.put(ZoneAction.ADD, addZoneIdentities);
                logger.debug("Found {} zones to add to the zone cache.", addZoneIdentities.size());
            }
            
            // Determine which zones need to be deleted from the cache.
            Set<ZoneIdentity> deleteZoneIdentities = new HashSet<ZoneIdentity>(this.zoneMap.keySet());
            deleteZoneIdentities.removeAll(repositoryZoneIdentities);
            if (!deleteZoneIdentities.isEmpty()) {
                this.refreshZoneMap.put(ZoneAction.DELETE, deleteZoneIdentities);
                logger.debug("Found {} zones to delete from the zone cache.", addZoneIdentities.size());
            }
        } catch (Exception e) {
            logger.warn("Unable to find the zones in the database.  Using the existing zone map.", e);
        }

        // Check if a refresh is necessary.
        if (!this.refreshZoneMap.isEmpty()) {
            
            // Initialize the new zone map with the zone map.
            ConcurrentMap<ZoneIdentity,List<Record>> newZoneMap = new ConcurrentHashMap<ZoneIdentity,List<Record>>(this.zoneMap);
            
            // Handle adding zones to the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.ADD)) {
            
                for (ZoneIdentity zoneIdentity : this.refreshZoneMap.get(ZoneAction.ADD)) {
                    List<Record> records = getRecordsFromDNS(zoneIdentity);
                    newZoneMap.put(zoneIdentity, records);
                    logger.debug("Added {} records to the zone cache for the zone {}.", records.size(), zoneIdentity);
                    
                    // Schedule a refresh for the zone.
                    ((RefreshZoneScheduler)this.refreshZoneScheduler).schedule(zoneIdentity, getRefreshInterval(zoneIdentity, newZoneMap));
                }
            }
            
            // Handle deleting zones from the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.DELETE)) {
                
                for (ZoneIdentity zoneIdentity : this.refreshZoneMap.get(ZoneAction.DELETE)) {
                    List<Record> records = getRecordsFromCache(zoneIdentity);
                    newZoneMap.remove(zoneIdentity);
                    logger.debug("Deleted {} records from the zone cache for the zone {}.", records.size(), zoneIdentity);
                    
                    // Unschedule a refresh for the zone.
                    ((RefreshZoneScheduler)this.refreshZoneScheduler).unschedule(zoneIdentity);
                }
            }

            // Handle updating zones in the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.UPDATE)) {
                
                for (ZoneIdentity zoneIdentity : this.refreshZoneMap.get(ZoneAction.UPDATE)) {

                    // Get the DNS and cache serial numbers for the zone.
                    long dnsSerialNumber = getSerialNumberFromDNS(zoneIdentity);
                    long cacheSerialNumber = getSerialNumberFromCache(zoneIdentity);
                    
                    logger.debug("Comparing the DNS serial number {} to the zone cache serial number {} for the zone {}.", dnsSerialNumber, cacheSerialNumber, zoneIdentity);
                    if (dnsSerialNumber > cacheSerialNumber) {
                        logger.debug("{} records in the zone {} prior to the zone cache update.", this.zoneMap.get(zoneIdentity).size(), zoneIdentity);
                        List<Record> records = getRecordsFromDNS(zoneIdentity, this.zoneMap.get(zoneIdentity));
                        newZoneMap.put(zoneIdentity, records);
                        logger.debug("{} records in the zone {} after the zone cache update.", records.size(), zoneIdentity);
                    }
                    
                    // Rechedule a refresh for the zone.
                    ((RefreshZoneScheduler)this.refreshZoneScheduler).reschedule(zoneIdentity, getRefreshInterval(zoneIdentity, newZoneMap));
                }
            }
            
            // Replace the zone map with the new zone map.
            this.zoneMap = newZoneMap;
            
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
     * Shutdown the zone cache.
     */
    @PreDestroy
    public synchronized void shutdown() throws InterruptedException {
        
        logger.info("Stopping the zone cache ...");
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
}
