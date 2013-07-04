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
import org.lazydog.jdnsaas.model.RecordOperation;

/**
 * Record operations wrapper.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement                                         // Needed by Enunciate.
public class RecordOperationsWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<RecordOperation> recordOperations = new ArrayList<RecordOperation>();
    
    /**
     * Get the record operations.
     * 
     * @return  the record operations.
     */
    public List<RecordOperation> getRecordOperations() {
        return this.recordOperations;
    }
        
    /**
     * Create a new instance of the record operations wrapper class.
     * 
     * @param  recordOperations  the record operations.
     * 
     * @return  a new instance of the record operations wrapper class.
     */
    public static RecordOperationsWrapper newInstance(final List<RecordOperation> recordOperations) {
        RecordOperationsWrapper recordOperationsWrapper = new RecordOperationsWrapper();
        recordOperationsWrapper.setRecordOperations(recordOperations);
        return recordOperationsWrapper;
    }
    
    /**
     * Set the record operations.
     * 
     * @param  recordOperations  the record operations.
     */
    public void setRecordOperations(final List<RecordOperation> recordOperations) {
        this.recordOperations = recordOperations;
    }
}
