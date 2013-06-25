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

import org.lazydog.jdnsaas.model.Model;
import org.lazydog.jdnsaas.model.Zone;

/**
 * Zone identity.
 * 
 * @author  Ron Rickard
 */
public class ZoneIdentity extends Model {
    
    private static final long serialVersionUID = 1L;
    private String viewName;
    private String zoneName;
    
    /**
     * Hide the constructor.
     * 
     * @param  viewName  the view name.
     * @param  zoneName  the zone name.
     */
    private ZoneIdentity(String viewName, String zoneName) {
        this.viewName = viewName;
        this.zoneName = zoneName;
    }
    
    /**
     * Get the view name.
     * 
     * @return  the view name.
     */
    public String getViewName() {
        return this.viewName;
    }
    
    /**
     * Get the zone name.
     * 
     * @return  the zone name.
     */
    public String getZoneName() {
        return this.zoneName;
    }
    
    /**
     * Set the view name.
     * 
     * @param  viewName  the view name.
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    
    /**
     * Set the zone name.
     * 
     * @param  zoneName  the zone name.
     */
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
    
    /**
     * Create the zone identity.
     * 
     * @param  zone  the zone.
     * 
     * @return  the zone identity.
     */
    public static ZoneIdentity newInstance(Zone zone) {
        return new ZoneIdentity(zone.getViewName(), zone.getName());
    }
}
