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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.lazydog.jdnsaas.model.ZoneIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Refresh the zone scheduler.
 * 
 * @author  Ron Rickard
 */
public class RefreshZoneScheduler implements Runnable {
    
    private static final Logger logger = LoggerFactory.getLogger(RefreshZoneScheduler.class);
    private static final int DEFAULT_INITIAL_DELAY = 10;
    private static final int DEFAULT_THREADS = 10;
    private int initialDelay;
    private boolean isRunning;
    private ConcurrentMap<ZoneIdentity,ScheduledFuture> refreshZoneFutureMap = new ConcurrentHashMap<ZoneIdentity,ScheduledFuture>();
    private ScheduledExecutorService refreshZoneThreadPool;
    private CountDownLatch shutdownLatch;
    private ZoneCache zoneCache;
    
    /**
     * Create the refresh zone scheduler.
     * 
     * @param  recordCache   the record cache.
     * @param  threads       the number of threads.
     * @param  initialDelay  the initial delay before refreshing the zones.
     * 
     * @throws  IOException  if unable to create the refresh zone scheduler.
     */
    public RefreshZoneScheduler(final ZoneCache zoneCache, final int threads, final int initialDelay) {
        this.zoneCache = zoneCache;
        this.initialDelay = (initialDelay > 0) ? initialDelay : DEFAULT_INITIAL_DELAY;
        this.shutdownLatch = new CountDownLatch(1);
        this.refreshZoneThreadPool = Executors.newScheduledThreadPool((threads > 0) ? threads : DEFAULT_THREADS);
        logger.info("Startup the refresh zone thread pool with {} threads.", (threads > 0) ? threads : DEFAULT_THREADS);
    }
    
    /**
     * Reschedule a refresh for the zone.
     * 
     * @param  zoneIdentity     the zone identity.
     * @param  refreshInterval  the refresh interval.
     */
    public void reschedule(final ZoneIdentity zoneIdentity, final long refreshInterval) {
        this.refreshZoneFutureMap.get(zoneIdentity).cancel(true);
        this.refreshZoneFutureMap.put(zoneIdentity, this.refreshZoneThreadPool.scheduleWithFixedDelay(new RefreshZoneThread(zoneIdentity), refreshInterval, refreshInterval, TimeUnit.SECONDS));
        logger.debug("Rescheduled a refresh for the zone {} in {} seconds.", zoneIdentity, refreshInterval);
    }
  
    /**
     * Startup the refresh zone scheduler.
     */
    @Override
    public void run() {

        this.isRunning = true;
        
        // Bootstrap an initial refresh of the zones.
        ZoneIdentity zoneIdentity = ZoneIdentity.noZoneIdentity();
        this.refreshZoneFutureMap.put(zoneIdentity, this.refreshZoneThreadPool.scheduleWithFixedDelay(new RefreshZoneThread(zoneIdentity), this.initialDelay, this.initialDelay, TimeUnit.SECONDS));

        try {

            // Continue to run until explicitedly shutdown.
            while (this.isRunning) {

                try {
                    
                    this.shutdownLatch.await();
                } catch (InterruptedException e) {
                    logger.warn("Interrupt received by the refresh zone scheduler.", e);
                    this.shutdownLatch = new CountDownLatch(1);
                }
            }
        } catch (Exception e) {
            logger.error("The refresh zone scheduler has aborted.", e);
        } finally {

            // Shutdown the refresh zone thread pool.
            this.refreshZoneThreadPool.shutdown();
            logger.info("Shutdown the refresh zone thread pool.");
        }
    }
    
    /**
     * Schedule a refresh for the zone.
     * 
     * @param  zoneIdentity     the zone identity.
     * @param  refreshInterval  the refresh interval.
     */
    public void schedule(final ZoneIdentity zoneIdentity, final long refreshInterval) {
        this.refreshZoneFutureMap.put(zoneIdentity, this.refreshZoneThreadPool.scheduleWithFixedDelay(new RefreshZoneThread(zoneIdentity), refreshInterval, refreshInterval, TimeUnit.SECONDS));
        logger.debug("Scheduled a refresh for the zone {} in {} seconds.", zoneIdentity, refreshInterval);
    }

    /**
     * Shutdown the refresh zone scheduler.
     */
    public void shutdown() {
        this.isRunning = false;
        for (ZoneIdentity zoneIdentity : this.refreshZoneFutureMap.keySet()) {
            this.refreshZoneFutureMap.get(zoneIdentity).cancel(true);
        }
        this.shutdownLatch.countDown();
    }
      
    /**
     * Unschedule a refresh for the zone.
     * 
     * @param  zoneIdentity  the zone identity.
     */
    public void unschedule(final ZoneIdentity zoneIdentity) {
        this.refreshZoneFutureMap.get(zoneIdentity).cancel(true);
        this.refreshZoneFutureMap.remove(zoneIdentity);
        logger.debug("Unschedule a refresh for the zone {}.", zoneIdentity);
    }
    
    /**
     * Refresh zone thread.
     */
    private class RefreshZoneThread implements Runnable {

        private ZoneIdentity zoneIdentity;
        
        /**
         * Create the refresh zone thread.
         * 
         * @param  zoneIdentity  the zone identity.
         */
        public RefreshZoneThread(final ZoneIdentity zoneIdentity) {
            this.zoneIdentity = zoneIdentity;
        }

        /**
         * Run the refresh zone thread.
         */
        @Override
        public void run() {

            logger.info("Start a refresh zone thread.");
            
            // Check if this is the initial refresh of the zones.
            if (this.zoneIdentity.equals(ZoneIdentity.noZoneIdentity())) {
                
                // Refresh the zone cache and make it available.
                RefreshZoneScheduler.this.zoneCache.resume();
                RefreshZoneScheduler.this.unschedule(this.zoneIdentity);
            } else {

                // Flag the zone for a refresh.
                RefreshZoneScheduler.this.zoneCache.flagForRefresh(this.zoneIdentity);

                // Check if the record cache is available.
                if (RefreshZoneScheduler.this.zoneCache.isAvailable()) {

                    // Refresh the zone cache.
                    RefreshZoneScheduler.this.zoneCache.refresh();
                }
            }
            
            logger.info("Stop a refresh zone thread.");
        }
    }
}
