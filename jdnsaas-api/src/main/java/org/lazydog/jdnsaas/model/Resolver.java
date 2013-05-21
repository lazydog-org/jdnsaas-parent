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
 * Resolver.
 * 
 * @author  Ron Rickard
 */
public class Resolver extends Entity {
    
    private static final long serialVersionUID = 1L;
    private String hostName;
    private String localHostName;
    private Integer port;
    private TSIGKey tsigKey;

    /**
     * Get the host name.
     * 
     * @return  the host name.
     */
    public String getHostName() {
        return this.hostName;
    }
         
    /**
     * Get the local host name.
     * 
     * @return  the local host name.
     */
    public String getLocalHostName() {
        return this.localHostName;
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
     * Get the TSIG key.
     * 
     * @return  the TSIG key.
     */
    public TSIGKey getTSIGKey() {
        return this.tsigKey;
    }
        
    /**
     * Set the host name.
     * 
     * @param  hostName  the host name.
     */
    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }
    
    /**
     * Set the local host name.
     * 
     * @param  localHostName  the local host name.
     */
    public void setLocalHostName(final String localHostName) {
        this.localHostName = localHostName;
    }
   
    /**
     * Set the port.
     * 
     * @param  port  the port.
     */
    public void setPort(final Integer port) {
        this.port = port;
    }
    
    /**
     * Set the TSIG key.
     * 
     * @param  tsigKey  the TSIG key.
     */
    public void setTSIGKey(final TSIGKey tsigKey) {
        this.tsigKey = tsigKey;
    }
}
