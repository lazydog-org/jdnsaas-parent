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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A record data.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement
public class ARecordData extends RecordData {
    
    private static final long serialVersionUID = 1L;
    private String ipAddress;

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
