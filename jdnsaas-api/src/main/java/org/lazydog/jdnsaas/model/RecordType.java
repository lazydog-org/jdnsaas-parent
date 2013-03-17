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
 * Record type.
 * 
 * @author  Ron Rickard
 */
public enum RecordType {
    AAAA    (true,      false),
    A       (true,      false),
    ANY     (true,      true),
    CNAME   (true,      false),
    MX      (true,      false),
    NS      (true,      true),
    PTR     (false,     true),
    SRV     (true,      false),
    TXT     (true,      false);

    private boolean forwardType;
    private boolean reverseType;
    
    /**
     * Constructor.
     * 
     * @param  forwardType  true if this is a forward type, otherwise false.
     * @param  reverseType  true if this is a reverse type, otherwise false.
     */
    private RecordType(boolean forwardType, boolean reverseType) {
        this.forwardType = forwardType;
        this.reverseType = reverseType;
    }

    /**
     * Get the record type from the String.
     * 
     * @param  asString  the record type as a String.
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
     * Get the forward types.
     * 
     * @return  the forward types.
     */
    public static List<RecordType> getForwardTypes() {
        
        // Initialize the forward types.
        List<RecordType> forwardTypes = new ArrayList<RecordType>();
        
        // Loop through the record types.
        for (RecordType type: RecordType.values()) {
            
            // Check if the type is a forward type.
            if (type.isForwardType()) {
                
                // Add the type to the list.
                forwardTypes.add(type);
            }
        }
        
        return forwardTypes;
    }
        
    /**
     * Get the reverse types.
     * 
     * @return  the reverse types.
     */
    public static List<RecordType> getReverseTypes() {
        
        // Initialize the forward types.
        List<RecordType> reverseTypes = new ArrayList<RecordType>();
        
        // Loop through the record types.
        for (RecordType type: RecordType.values()) {
            
            // Check if the type is a reverse type.
            if (type.isReverseType()) {
                
                // Add the type to the list.
                reverseTypes.add(type);
            }
        }
        
        return reverseTypes;
    }
    
    /**
     * Determine if this is a forward type.
     * 
     * @return  true if this is a forward type, otherwise false.
     */
    public boolean isForwardType() {
        return this.forwardType;
    }
    
    /**
     * Determine if this is a reverse type.
     * 
     * @return  true if this is a reverse type, otherwise false.
     */
    public boolean isReverseType() {
        return this.reverseType;
    }
}
