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
 * Zone.
 * 
 * @author  Ron Rickard
 */
public class Zone extends Entity implements IdentityEqualator<Zone> {
    
    private static final long serialVersionUID = 1L;
    private String name;
    private TSIGKey queryTSIGKey;
    private List<Record> records = new ArrayList<Record>();
    private List<RecordType> supportedRecordTypes = new ArrayList<RecordType>();
    private TSIGKey transferTSIGKey;
    private ZoneType type;
    private TSIGKey updateTSIGKey;
    private View view;

    /**
     * Get the name.
     * 
     * @return  the name.
     */
    public String getName() {
        return this.name;
    }
         
    /**
     * Get the query TSIG key.
     * 
     * @return  the query TSIG key.
     */
    public TSIGKey getQueryTSIGKey() {
        return this.queryTSIGKey;
    } 
    
    /**
     * Get the records.
     * 
     * @return  the records.
     */
    public List<Record> getRecords() {
        return this.records;
    }
      
    /**
     * Get the records for the record type.
     * 
     * @param  recordType  the record type.
     * 
     * @return  the records.
     */
    public List<Record> getRecords(RecordType recordType) {
        
        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
        
        // Loop through the records.
        for (Record record : this.records) {
            
            // Check if the record is the desired record type.
            if (record.getType() == recordType) {
                
                // Add the record to the list.
                records.add(record);
            }
        }
        
        return records;
    }
    
    /**
     * Get the supported record types.
     * 
     * @return  the supported record types.
     */
    public List<RecordType> getSupportedRecordTypes() {
        return this.supportedRecordTypes;
    }
    
    /**
     * Get the transfer TSIG key.
     * 
     * @return  the transfer TSIG key.
     */
    public TSIGKey getTransferTSIGKey() {
        return this.transferTSIGKey;
    }
    
    /**
     * Get the type.
     * 
     * @param  type  the type.
     */
    public ZoneType getType() {
        return this.type;
    }
      
    /**
     * Get the update TSIG key.
     * 
     * @return  the update TSIG key.
     */
    public TSIGKey getUpdateTSIGKey() {
        return this.updateTSIGKey;
    } 

    /**
     * Get the view.
     * 
     * @return  the view.
     */
    public View getView() {
        return this.view;
    }

    /**
     * Compare this object's identity to the specified object's identity.
     * 
     * @param  object  the object's identity to compare this object's identity against.
     * 
     * @return  true if the objects' identities are equal; false otherwise.
     */
    @Override
    public boolean identityEquals(Zone object) {

        boolean identityEquals = false;
        
        if (object != null) {
            String thatName = replaceNull(object.getName(), "");
            View thatView = replaceNull(object.getView(), new View());
            String thisName = replaceNull(this.getName(), "");
            View thisView = replaceNull(this.getView(), new View());

            identityEquals = thisName.equals(thatName);
            identityEquals = (identityEquals) ? thisView.identityEquals(thatView) : identityEquals;
        }
        
        return identityEquals;
    }
    
    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(final String name) {
        this.name = name;
    }
             
    /**
     * Set the query TSIG key.
     * 
     * @param  queryTSIGKey  the query TSIG key.
     */
    public void setQueryTSIGKey(final TSIGKey queryTSIGKey) {
        this.queryTSIGKey = queryTSIGKey;
    }
     
    /**
     * Set the records.
     * 
     * @param  records  the records.
     */
    public void setRecords(final List<Record> records) {
        this.records = replaceNull(records, new ArrayList<Record>());
    }
    
    /**
     * Set the supported record types.
     * 
     * @param  supportedRecordTypes  the supported record types.
     */
    public void setSupportedRecordTypes(final List<RecordType> supportedRecordTypes) {
        this.supportedRecordTypes = replaceNull(supportedRecordTypes, new ArrayList<RecordType>());
    }
        
    /**
     * Set the transfer TSIG key.
     * 
     * @param  transferTSIGKey  the transfer TSIG key.
     */
    public void setTransferTSIGKey(final TSIGKey transferTSIGKey) {
        this.transferTSIGKey = transferTSIGKey;
    }
    
    /**
     * Set the type.
     * 
     * @param  type  the type.
     */
    public void setType(final ZoneType type) {
        this.type = type;
    }
             
    /**
     * Set the update TSIG key.
     * 
     * @param  updateTSIGKey  the update TSIG key.
     */
    public void setUpdateTSIGKey(final TSIGKey updateTSIGKey) {
        this.updateTSIGKey = updateTSIGKey;
    }
       
    /**
     * Set the view.
     * 
     * @param  view  the view.
     */
    public void setView(final View view) {
        this.view = view;
    }
}
