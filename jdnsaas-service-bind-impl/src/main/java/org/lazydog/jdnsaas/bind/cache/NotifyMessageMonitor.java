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
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.lazydog.jdnsaas.model.SOARecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Notify message monitor.
 * 
 * @author  Ron Rickard
 */
public class NotifyMessageMonitor extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(NotifyMessageMonitor.class);
    private static final int DEFAULT_SOCKET_TIMEOUT = 30000;
    private static final int DEFAULT_THREADS = 10;
    private static final int UDP_PACKET_SIZE = 4096;
    private String ipAddress;
    private int port;
    private int threads;
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
        super();
        this.zoneCache = zoneCache;
        this.ipAddress = ipAddress;
        this.port = port;
        this.threads = threads;
        
        // Open the UDP socket.  Set a timeout value to allow the monitor to be shutdown gracefully.
        this.socket = new DatagramSocket(port, InetAddress.getByName(ipAddress));
        this.socket.setSoTimeout(DEFAULT_SOCKET_TIMEOUT);
    }
    
    /**
     * Run the notify message monitor.
     */
    @Override
    public void run() {

        // Initialize the notify message thread pool.
        ExecutorService notifyMessageThreadPool = Executors.newFixedThreadPool((this.threads > 0) ? this.threads : DEFAULT_THREADS);
        logger.debug("Startup the notify message thread pool with {} threads.", (this.threads > 0) ? this.threads : DEFAULT_THREADS);
        
        try {

            // Check if the zone cache is not shutdown.
            while (!this.zoneCache.isShutdown()) {

                try {

                    // Receive a packet.
                    DatagramPacket requestPacket = new DatagramPacket(new byte[UDP_PACKET_SIZE], UDP_PACKET_SIZE);
                    this.socket.receive(requestPacket);
                    
                    // TODO: validate the address and port.

                    // Execute a notify message thread to handle the packet.
                    notifyMessageThreadPool.execute(new NotifyMessageThread(this.zoneCache, this.socket, requestPacket));
                } catch (SocketTimeoutException e) {
                    logger.debug("UDP socket {} port {} timed out.", this.ipAddress, this.port);
                } catch (IOException e) {
                    logger.warn("Error receiving a UDP request from {} port {}.", this.ipAddress, this.port);
                } 
            }
        } catch (Exception e) {
            logger.error("The notify message monitor has aborted.", e);
        } finally {

            // Shutdown the notify message thread pool.
            notifyMessageThreadPool.shutdown();
            logger.debug("Shutdown the notify message thread pool.");
            
            // Close the UDP socket.
            this.socket.close();
        }
    }

    /**
     * Notify message thread.
     */
    private class NotifyMessageThread implements Runnable {
        
        private ZoneCache zoneCache;
        private DatagramPacket requestPacket;
        private DatagramSocket socket;
        
        /**
         * Initialize the notify message thread.
         * 
         * @param  socket         the socket.
         * @param  requestPacket  the request packet.
         */
        public NotifyMessageThread(final ZoneCache zoneCache, final DatagramSocket socket, final DatagramPacket requestPacket) {
            this.zoneCache = zoneCache;
            this.socket = socket;
            this.requestPacket = requestPacket;
        }

        /**
         * Run the notify message thread.
         */
        @Override
        public void run() {

            NotifyRequestMessage requestMessage = null;
            
            try {

                // Create a notify request message from the request packet.
                requestMessage = NotifyRequestMessage.newInstance(this.requestPacket.getData());

                // Create the notify response from the request message.
                byte[] response = requestMessage.createNotifyResponseMessage();

                // Send the response.
                DatagramPacket responsePacket = new DatagramPacket(response, response.length, this.requestPacket.getAddress(), this.requestPacket.getPort());
                socket.send(responsePacket);
            } catch (InvalidRequestMessageException e) {
                logger.warn("An invalid notify request message was received.  Not sending a response to {} port {}.", this.requestPacket.getAddress(), this.requestPacket.getPort(), e);
            } catch (IOException e) {
                logger.warn("Error sending a UDP response to {} port {}.", this.requestPacket.getAddress().getHostAddress(), this.requestPacket.getPort());
            }

            // Since there is a notify message, flag the zone cache for refresh.
            // TODO: use the address and port to find the exact zone.
            this.zoneCache.flagForRefresh(requestMessage.getZoneName());
               
            // Check if the record cache is not suspended.
            if (!this.zoneCache.isSuspended()) {
                
                // Refresh the zone cache.
                this.zoneCache.refresh();
            }
        }
    }
}
