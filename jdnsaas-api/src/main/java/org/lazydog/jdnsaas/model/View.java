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
 * View.
 * 
 * @author  Ron Rickard
 */
public class View extends Entity {
    
    private static final long serialVersionUID = 1L;
    private DNSServer dnsServer;
    private String name;
   
    /**
     * Get the DNS server.
     * 
     * @return  the DNS server.
     */
    public DNSServer getDnsServer() {
        return this.dnsServer;
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
     * Set the DNS server.
     * 
     * @param  dnsServer  the DNS server.
     */
    public void setDnsServer(final DNSServer dnsServer) {
        this.dnsServer = dnsServer;
    }
    
    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(final String name) {
        this.name = name;
    }
}
