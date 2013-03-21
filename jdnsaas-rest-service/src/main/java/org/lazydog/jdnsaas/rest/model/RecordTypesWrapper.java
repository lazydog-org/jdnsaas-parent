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
import org.lazydog.jdnsaas.model.RecordType;

/**
 * Record types wrapper.
 * 
 * @author  Ron Rickard
 */
public class RecordTypesWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<RecordType> recordTypes = new ArrayList<RecordType>();
    
    /**
     * Get the record types.
     * 
     * @return  the record types.
     */
    public List<RecordType> getRecordTypes() {
        return this.recordTypes;
    }
        
    /**
     * Create a new instance of the record types wrapper class.
     * 
     * @param  recordTypes  the record types.
     * 
     * @return  a new instance of the record types wrapper class.
     */
    public static RecordTypesWrapper newInstance(final List<RecordType> recordTypes) {
        
        RecordTypesWrapper recordTypesWrapper = new RecordTypesWrapper();
        recordTypesWrapper.setRecordTypes(recordTypes);
        
        return recordTypesWrapper;
    }
    
    /**
     * Set the record types.
     * 
     * @param  recordTypes  the record types.
     */
    public void setRecordTypes(final List<RecordType> recordTypes) {
        this.recordTypes = recordTypes;
    }
}
