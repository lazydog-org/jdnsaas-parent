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
 * A record.
 * 
 * @author  Ron Rickard
 */
public class ARecord extends Record {
    
    private static final long serialVersionUID = 1L;
    private String ipAddress;
   
    /**
     * Initialize the record with the record type.
     */
    public ARecord() {
        this.setType(RecordType.A);
    }

    /**
     * Get the IP address.
     * 
     * @return  the IP address.
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * Set the IP address.
     * 
     * @param  ipAddress  the IP address.
     */
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
