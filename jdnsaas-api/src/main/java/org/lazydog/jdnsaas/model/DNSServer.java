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

import java.util.ArrayList;
import java.util.List;

/**
 * DNS server.
 * 
 * @author  Ron Rickard
 */
public class DNSServer extends Model {
    
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer port;
    private List<Zone> zones = new ArrayList<Zone>();

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
     * Get the zones.
     * 
     * @return  the zones.
     */
    public List<Zone> getZones() {
        return this.zones;
    }

    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(String name) {
        this.name = name;
    }
       
    /**
     * Set the port.
     * 
     * @param  port  the port.
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Set the zones.
     * 
     * @param  zones  the zones.
     */
    public void setZones(List<Zone> zones) {
        this.zones = replaceNull(zones, new ArrayList<Zone>());
    }
}
