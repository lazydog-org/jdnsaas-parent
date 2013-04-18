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

import org.lazydog.jdnsaas.model.Zone;

/**
 * Zone response.
 * 
 * @author  Ron Rickard
 */
public class ZoneWrapper extends Zone {
    
    private static final long serialVersionUID = 1L;
    private String recordsUrl;
    private String url;
    
    /**
     * Get the records URL.
     * 
     * @return  the records URL.
     */
    public String getRecordsUrl() {
        return this.recordsUrl;
    }
    
    /**
     * Get the URL.
     * 
     * @return  the URL.
     */
    public String getUrl() {
        return this.url;
    }
    
    /**
     * Create a new instance of the zone wrapper class.
     * 
     * @param  zoneName  the zone name.
     * @param  url       the URL.
     * 
     * @return  a new instance of the zone wrapper class.
     */
    public static ZoneWrapper newInstance(final String zoneName, final String url) {
        
        ZoneWrapper zoneWrapper = new ZoneWrapper();
        zoneWrapper.setName(zoneName);
        zoneWrapper.setUrl(url);
        
        return zoneWrapper;
    }
    
    /**
     * Create a new instance of the zone wrapper class.
     * 
     * @param  zone        the zone.
     * @param  url         the URL.
     * @param  recordsUrl  the records URL.
     * 
     * @return  a new instance of the zone wrapper class.
     */
    public static ZoneWrapper newInstance(final Zone zone, final String url, final String recordsUrl) {
        
        ZoneWrapper zoneWrapper = new ZoneWrapper();
        zoneWrapper.setName(zone.getName());
        zoneWrapper.setQueryTSIGKey(zone.getQueryTSIGKey());
        zoneWrapper.setRecordsUrl(recordsUrl);
        zoneWrapper.setSupportedRecordTypes(zone.getSupportedRecordTypes());
        zoneWrapper.setTransferTSIGKey(zone.getTransferTSIGKey());
        zoneWrapper.setUpdateTSIGKey(zone.getUpdateTSIGKey());
        zoneWrapper.setUrl(url);

        return zoneWrapper;
    }

    /**
     * Set the records URL.
     * 
     * @param  recordsUrl  the records URL.
     */
    public void setRecordsUrl(final String recordsUrl) {
        this.recordsUrl = recordsUrl;
    }
    
    /**
     * Set the URL.
     * 
     * @param  url  the URL. 
     */
    public void setUrl(final String url) {
        this.url = url;
    }
}
