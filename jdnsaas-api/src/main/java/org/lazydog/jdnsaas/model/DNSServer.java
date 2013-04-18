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
package org.lazydog.jdnsaas.model;

/**
 * DNS server.
 * 
 * @author  Ron Rickard
 */
public class DNSServer extends Entity {
    
    private static final long serialVersionUID = 1L;
    private String localAddress;
    private String name;
    private Integer port;
    
    /**
     * Get the local address.
     * 
     * @return  the local address.
     */
    public String getLocalAddress() {
        return this.localAddress;
    }

    /**
     * Get the name.
     * 
     * @return  the name.
     */
    public String getName() {
        return this.name;
    }
     
    /**
     * Get the port.
     * 
     * @return  the port.
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * Set the local address.
     * 
     * @param  localAddress  the local address.
     */
    public void setLocalAddress(final String localAddress) {
        this.localAddress = localAddress;
    }
    
    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(final String name) {
        this.name = name;
    }
       
    /**
     * Set the port.
     * 
     * @param  port  the port.
     */
    public void setPort(final Integer port) {
        this.port = port;
    }
}
