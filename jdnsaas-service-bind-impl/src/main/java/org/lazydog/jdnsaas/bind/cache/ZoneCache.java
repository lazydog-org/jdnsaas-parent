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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.lazydog.jdnsaas.NotifyMessageMonitorAddress;
import org.lazydog.jdnsaas.NotifyMessageMonitorPort;
import org.lazydog.jdnsaas.NotifyMessageMonitorThreads;
import org.lazydog.jdnsaas.bind.DNSServerExecutor;
import org.lazydog.jdnsaas.bind.DNSServerExecutorException;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.SOARecord;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepository;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepositoryException;
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
    private boolean isShutdown;
    private boolean isSuspended;
    private NotifyMessageMonitor notifyMessageMonitor;
    private String notifyMessageMonitorAddress;
    private int notifyMessageMonitorPort;
    private int notifyMessageMonitorThreads;
    private ConcurrentMap<ZoneAction,Set<ZoneIdentity>> refreshZoneMap = new ConcurrentHashMap<ZoneAction,Set<ZoneIdentity>>();
    private JDNSaaSRepository repository;
    private ConcurrentMap<ZoneIdentity,List<Record>> zoneMap = new ConcurrentHashMap<ZoneIdentity,List<Record>>();
    private enum ZoneAction {
        ADD,
        DELETE,
        UPDATE;
    }
    
    /**
     * Is the zone cache shutdown?
     * 
     * @return  true if the zone cache is shutdown, otherwise false.
     */
    public boolean isShutdown() {
        return this.isShutdown;
    }
    
    /**
     * Is the zone cache suspended?
     * 
     * @return  true if the zone cache is suspended, otherwise false.
     */
    public boolean isSuspended() {
        return this.isSuspended;
    }
    
    /**
     * Flag the zone cache for a refresh.
     * 
     * @param  zoneName  the zone name.
     */
    public synchronized void flagForRefresh(final String zoneName) {

        // Loop through the zone identities in the zone map.
        for (ZoneIdentity zoneIdentity : this.zoneMap.keySet()) {

            // Check if the zone is found.
            if (zoneIdentity.getZoneName().equals(zoneName)) {
                
                if (this.refreshZoneMap.containsKey(ZoneAction.UPDATE)) {
                    this.refreshZoneMap.get(ZoneAction.UPDATE).add(zoneIdentity);
                } else {
                    this.refreshZoneMap.put(ZoneAction.UPDATE, new HashSet<ZoneIdentity>(Arrays.asList(zoneIdentity)));
                }
                
                logger.debug("Flagged the view {} and the zone {} for refresh.", zoneIdentity.getViewName(), zoneIdentity.getZoneName());
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
    private List<Record> getRecordsFromDNS(ZoneIdentity zoneIdentity) {
        
        List<Record> records = new ArrayList<Record>();
        
        try {

            // Get the records for the zone.
            Zone zone = this.repository.findZone(zoneIdentity.getViewName(), zoneIdentity.getZoneName());
            records = DNSServerExecutor.newInstance(zone.getView().getResolvers(), null, zone.getTransferTSIGKey(), null, zone.getName()).findRecords(RecordType.ANY, null);
        } catch (JDNSaaSRepositoryException e) {
            logger.warn("Unable to find the zone {} for the view {}.", zoneIdentity.getZoneName(), zoneIdentity.getViewName(), e);
        } catch (DNSServerExecutorException e) {
            logger.warn("Unable to find all the records for the view {} and the zone {}.", zoneIdentity.getViewName(), zoneIdentity.getZoneName(), e);
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

        } catch (JDNSaaSRepositoryException e) {
            logger.warn("Unable to find the zone {} for the view {}.", zoneIdentity.getZoneName(), zoneIdentity.getViewName(), e);
        } catch (DNSServerExecutorException e) {
            logger.warn("Unable to find the records for the view {} and the zone {}.", zoneIdentity.getViewName(), zoneIdentity.getZoneName(), e);
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
     * Refresh the zone cache.
     */
    public synchronized void refresh() {
        
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
        } catch (JDNSaaSRepositoryException e) {
            logger.warn("Unable to find zones in database.  Using existing zone map.", e);
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
                    logger.debug("Added {} records to the zone cache for the view {} and the zone {}.", records.size(), zoneIdentity.getViewName(), zoneIdentity.getZoneName());
                }
            }
            
            // Handle deleting zones from the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.DELETE)) {
                
                for (ZoneIdentity zoneIdentity : this.refreshZoneMap.get(ZoneAction.DELETE)) {
                    List<Record> records = getRecordsFromCache(zoneIdentity);
                    newZoneMap.remove(zoneIdentity);
                    logger.debug("Deleted {} records from the zone cache for the view {} and the zone {}.", records.size(), zoneIdentity.getViewName(), zoneIdentity.getZoneName());
                }
            }

            // Handle updating zones in the cache.
            if (this.refreshZoneMap.containsKey(ZoneAction.UPDATE)) {
                
                for (ZoneIdentity zoneIdentity : this.refreshZoneMap.get(ZoneAction.UPDATE)) {

                    // Get the DNS and cache serial numbers for the zone.
                    long dnsSerialNumber = getSerialNumberFromDNS(zoneIdentity);
                    long cacheSerialNumber = getSerialNumberFromCache(zoneIdentity);
                    
                    logger.debug("Comparing the DNS serial number {} to the zone cache serial number {} for the view {} and the zone {}.", dnsSerialNumber, cacheSerialNumber, zoneIdentity.getViewName(), zoneIdentity.getZoneName());
                    if (dnsSerialNumber != cacheSerialNumber) {
                        List<Record> records = getRecordsFromDNS(zoneIdentity);
                        newZoneMap.put(zoneIdentity, records);
                    }                       
                }
            }
            
            // Replace the zone map with the new zone map.
            this.zoneMap = newZoneMap;
            
            // Clear the refresh zone map.
            this.refreshZoneMap.clear();
        }
    }
    
    /**
     * Resume the zone cache.
     */
    public void resume() {
        refresh();
        this.isSuspended = false;
    }

    /**
     * Shutdown the zone cache.
     */
    @PreDestroy
    public void shutdown() throws InterruptedException {
        
        logger.info("Stopping the zone cache.");
        
        // Stop the notify message monitor and wait for it to finish.
        this.isShutdown = true;
        this.notifyMessageMonitor.join();
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
     * Startup the zone cache.
     * 
     * @throws  IOException  if unable to zone the record cache.
     */
    @PostConstruct
    public void startup() throws IOException {
        
        logger.info("Startup the zone cache.");
        this.isShutdown = false;
        this.isSuspended = false;
        this.refresh();
        this.notifyMessageMonitor = new NotifyMessageMonitor(this, this.notifyMessageMonitorAddress, this.notifyMessageMonitorPort, this.notifyMessageMonitorThreads);
        this.notifyMessageMonitor.start();
    }
        
    /**
     * Suspend the record cache.
     */
    public void suspend() {
        this.isSuspended = true;
    }
}
