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
import org.lazydog.jdnsaas.model.ZoneType;

/**
 * Zone types wrapper.
 * 
 * @author  Ron Rickard
 */
public class ZoneTypesWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<ZoneType> zoneTypes = new ArrayList<ZoneType>();
    
    /**
     * Get the zone types.
     * 
     * @return  the zone types.
     */
    public List<ZoneType> getZoneTypes() {
        return this.zoneTypes;
    }
        
    /**
     * Create a new instance of the zone types wrapper class.
     * 
     * @param  zoneTypes  the zone types.
     * 
     * @return  a new instance of the zone types wrapper class.
     */
    public static ZoneTypesWrapper newInstance(final List<ZoneType> zoneTypes) {
        ZoneTypesWrapper zoneTypesWrapper = new ZoneTypesWrapper();
        zoneTypesWrapper.setZoneTypes(zoneTypes);
        return zoneTypesWrapper;
    }
    
    /**
     * Set the zone types.
     * 
     * @param  zoneTypes  the zone types.
     */
    public void setZoneTypes(final List<ZoneType> zoneTypes) {
        this.zoneTypes = zoneTypes;
    }
}
