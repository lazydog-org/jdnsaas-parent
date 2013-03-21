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
import java.util.Arrays;
import java.util.List;

/**
 * Record type.
 * 
 * @author  Ron Rickard
 */
public enum RecordType {
    AAAA    (ZoneType.FORWARD),
    A       (ZoneType.FORWARD),
    ANY     (ZoneType.BOTH),
    CNAME   (ZoneType.FORWARD),
    MX      (ZoneType.FORWARD),
    NS      (ZoneType.BOTH),
    PTR     (ZoneType.REVERSE),
    SRV     (ZoneType.FORWARD),
    TXT     (ZoneType.FORWARD);

    private ZoneType zoneType;
    
    /**
     * Constructor.
     * 
     * @param  zoneType  the zone type.
     */
    private RecordType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    /**
     * Get the record type from the string.
     * 
     * @param  asString  the record type as a string.
     * 
     * @return  the record type.
     * 
     * @throws  IllegalArgumentException  if the desired record type is invalid.
     */
    public static RecordType fromString(final String asString) {
        
        for (RecordType type: RecordType.values()) {
            if(type.toString().equalsIgnoreCase(asString)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("The record type, " + asString + ", is invalid.");
    }
    
    /**
     * Return an array containing the record types for the zone type.
     * 
     * @param  zoneType  the zone type.
     * 
     * @return  the record types for the zone type.
     */
    public static RecordType[] values(final ZoneType zoneType) {
        
        List<RecordType> recordTypes = new ArrayList<RecordType>();
        
        if (zoneType == ZoneType.BOTH) {
            recordTypes = Arrays.asList(RecordType.values());
        } else {

            for (RecordType recordType: RecordType.values()) {

                if (recordType.isForZoneType(zoneType)) {
                    recordTypes.add(recordType);
                }
            }
        }
        
        return recordTypes.toArray(new RecordType[recordTypes.size()]);
    }

    /**
     * Determine if this record type is for the zone type.
     * 
     * @return  true if this record type is for the zone type, otherwise false.
     */
    public boolean isForZoneType(final ZoneType zoneType) {
        return (zoneType == ZoneType.BOTH || this.zoneType == zoneType || this.zoneType == ZoneType.BOTH);
    }
}
