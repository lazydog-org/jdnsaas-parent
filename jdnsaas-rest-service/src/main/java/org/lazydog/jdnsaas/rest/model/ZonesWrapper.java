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
package org.lazydog.jdnsaas.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.lazydog.jdnsaas.model.Zone;

/**
 * Zones wrapper.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement
public class ZonesWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<Zone> zones = new ArrayList<Zone>();
    
    /**
     * Get the zones.
     * 
     * @return  the zones.
     */
    public List<Zone> getZones() {
        return this.zones;
    }
        
    /**
     * Create a new instance of the zones wrapper class.
     * 
     * @param  zones  the zones.
     * 
     * @return  a new instance of the zones wrapper class.
     */
    public static ZonesWrapper newInstance(List<Zone> zones) {
        
        ZonesWrapper zonesWrapper = new ZonesWrapper();
        zonesWrapper.setZones(zones);
        
        return zonesWrapper;
    }
    
    /**
     * Set the zones.
     * 
     * @param  zones  the zones.
     */
    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }
}
