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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notify message monitor.
 * 
 * @author  Ron Rickard
 */
public class NotifyMessageMonitor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NotifyMessageMonitor.class);
    private static final int DEFAULT_PORT = 10053;
    private static final int DEFAULT_THREADS = 10;
    private static final int UDP_PACKET_SIZE = 4096;
    private String ipAddress;
    private boolean isRunning;
    private ExecutorService notifyMessageThreadPool;
    private int port;
    private ZoneCache zoneCache;
    private DatagramSocket socket;
    
    /**
     * Create the notify message monitor.
     * 
     * @param  recordCache    the record cache.
     * @param  ipAddress      the IP address.
     * @param  port           the port.
     * @param  threads        the number of threads.
     * 
     * @throws  IOException  if unable to create the notify message monitor.
     */
    public NotifyMessageMonitor(final ZoneCache zoneCache, final String ipAddress, final int port, final int threads) throws IOException {
        this.zoneCache = zoneCache;
        this.ipAddress = ipAddress;
        this.port = (port > 0) ? port : DEFAULT_PORT;
        this.socket = new DatagramSocket(this.port, InetAddress.getByName(this.ipAddress));
        logger.info("Opened the UDP socket {} port {}.", this.socket, this.port);
        this.notifyMessageThreadPool = Executors.newFixedThreadPool((threads > 0) ? threads : DEFAULT_THREADS);
        logger.info("Startup the notify message thread pool with {} threads.", (threads > 0) ? threads : DEFAULT_THREADS);
    }

    /**
     * Startup the notify message monitor.
     */
    @Override
    public void run() {

        this.isRunning = true;

        try {

            // Continue to run until explicitedly shutdown.
            while (this.isRunning) {

                try {

                    // Receive a packet.
                    DatagramPacket requestPacket = new DatagramPacket(new byte[UDP_PACKET_SIZE], UDP_PACKET_SIZE);
                    this.socket.receive(requestPacket);
                    
                    // TODO: validate the address and port.

                    // Execute a notify message thread to handle the packet.
                    this.notifyMessageThreadPool.execute(new NotifyMessageThread(requestPacket));
                } catch (IOException e) {
                    logger.debug("Error receiving a UDP request from {} port {}.", this.ipAddress, this.port);
                }
            }
        } catch (Exception e) {
            logger.error("The notify message monitor has aborted.", e);
        } finally {

            // Shutdown the notify message thread pool.
            this.notifyMessageThreadPool.shutdown();
            logger.info("Shutdown the notify message thread pool.");
            
            // Close the UDP socket if necessary.
            if (!this.socket.isClosed()) {
                this.socket.close();
            }
        }
    }
        
    /**
     * Shutdown the notify message monitor.
     */
    public void shutdown() {
        this.isRunning = false;
        this.socket.close();
        logger.info("Closed the UDP socket {} port {}.", this.socket, this.port);
    }

    /**
     * Notify message thread.
     */
    private class NotifyMessageThread implements Runnable {

        private DatagramPacket requestPacket;
        
        /**
         * Create the notify message thread.
         * 
         * @param  requestPacket  the request packet.
         */
        public NotifyMessageThread(final DatagramPacket requestPacket) {
            this.requestPacket = requestPacket;
        }

        /**
         * Run the notify message thread.
         */
        @Override
        public void run() {

            logger.info("Start a notify message thread.");
            
            NotifyRequestMessage requestMessage = null;
            
            try {

                // Create a notify request message from the request packet.
                requestMessage = NotifyRequestMessage.newInstance(this.requestPacket.getData());

                // Create the notify response from the request message.
                byte[] response = requestMessage.createNotifyResponseMessage();

                // Send the response.
                DatagramPacket responsePacket = new DatagramPacket(response, response.length, this.requestPacket.getAddress(), this.requestPacket.getPort());
                NotifyMessageMonitor.this.socket.send(responsePacket);
                        
                // Since there is a notify message, flag the zone for a refresh.
                // TODO: use the address and port to find the exact zone.  Currently, all the zones with the same name in different views will be flagged for refresh.
                NotifyMessageMonitor.this.zoneCache.flagZoneForRefresh(requestMessage.getZoneName());
            } catch (InvalidRequestMessageException e) {
                logger.warn("An invalid notify request message was received.  Not sending a response to {} port {}.", this.requestPacket.getAddress(), this.requestPacket.getPort(), e);
            } catch (IOException e) {
                logger.warn("Error sending a UDP response to {} port {}.", this.requestPacket.getAddress().getHostAddress(), this.requestPacket.getPort());
            }
   
            // Check if the record cache is available.
            if (NotifyMessageMonitor.this.zoneCache.isAvailable()) {
                
                // Refresh the zone cache.
                NotifyMessageMonitor.this.zoneCache.refresh();
            }
            
            logger.info("Stop a notify message thread.");
        }
    }
}
