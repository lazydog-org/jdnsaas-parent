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
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Zones wrapper.
 * 
 * @author  Ron Rickard
 */
public class ZonesWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @JsonProperty("zones")
    private List<ZoneWrapper> zoneWrappers = new ArrayList<ZoneWrapper>();
    
    /**
     * Get the zone wrappers.
     * 
     * @return  the zone wrappers.
     */
    public List<ZoneWrapper> getZoneWrappers() {
        return this.zoneWrappers;
    }
        
    /**
     * Create a new instance of the zones wrapper class.
     * 
     * @param  zoneWrappers  the zone wrappers.
     * 
     * @return  a new instance of the zones wrapper class.
     */
    public static ZonesWrapper newInstance(final List<ZoneWrapper> zoneWrappers) {
        ZonesWrapper zonesWrapper = new ZonesWrapper();
        zonesWrapper.setZoneWrappers(zoneWrappers);
        return zonesWrapper;
    }
    
    /**
     * Set the zone wrappers.
     * 
     * @param  zoneWrappers  the zone wrappers.
     */
    public void setZoneWrappers(final List<ZoneWrapper> zoneWrappers) {
        this.zoneWrappers = zoneWrappers;
    }
}
