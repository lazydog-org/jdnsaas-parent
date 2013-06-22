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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.lazydog.jdnsaas.NotifyMessageMonitorAddress;
import org.lazydog.jdnsaas.NotifyMessageMonitorPort;
import org.lazydog.jdnsaas.NotifyMessageMonitorThreads;
import org.lazydog.jdnsaas.model.Zone;
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
    private boolean isRunning;
    private boolean isSuspended;
    private NotifyMessageMonitor notifyMessageMonitor;
    private String notifyMessageMonitorAddress;
    private int notifyMessageMonitorPort;
    private int notifyMessageMonitorThreads;
    private Map<String,Boolean> refreshFlagMap = new HashMap<String,Boolean>();
    private Map<String,List<Zone>> zoneMap = new HashMap<String,List<Zone>>();

    /**
     * Is the zone cache running?
     * 
     * @return  true if the zone cache is running, otherwise false.
     */
    public boolean isRunning() {
        return this.isRunning;
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
     * Flag the cache for a refresh.
     * 
     * @param  zoneName         the zone name.
     * @param  resolverAddress  the resolver address.
     * @param  resolverPort     the resolver port.
     */
    public void flagForRefresh(final String zoneName, final String resolverAddress, final int resolverPort) {

    }
    
    /**
     * Refresh the zone cache.
     */
    public void refresh() {
        // Used to manually refresh the record cache.
        
        // Clear the refresh flag map.
        this.refreshFlagMap.clear();
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
        this.isRunning = false;
        this.notifyMessageMonitor.join();
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
        this.isRunning = true;
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
    
    public static void main(String[] args) throws IOException {
        
        ZoneCache zoneCache = new ZoneCache();
        zoneCache.setNotifyMessageMonitorAddress("192.168.0.25");
        zoneCache.setNotifyMessageMonitorPort(10053);
        zoneCache.setNotifyMessageMonitorThreads(10);
        zoneCache.startup();
    }
}
