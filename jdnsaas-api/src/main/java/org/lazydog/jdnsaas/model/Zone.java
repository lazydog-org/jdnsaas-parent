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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Zone.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement
public class Zone extends Entity {
    
    private static final long serialVersionUID = 1L;
    private DNSServer dnsServer;
    private String name;
    private List<Record> records = new ArrayList<Record>();
    private List<RecordType> supportedRecordTypes = new ArrayList<RecordType>();
    private ZoneType type;
    
    /**
     * Get the DNS server.
     * 
     * @return  the DNS server.
     */
    public DNSServer getDnsServer() {
        return this.dnsServer;
    }
    
    /**
     * Get the name.
     * 
     * @return  the name.
     */
    public String getName() {
        return this.name;
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
     * Get the type.
     * 
     * @param  type  the type.
     */
    public ZoneType getType() {
        return this.type;
    }
    
    /**
     * Set the DNS server.
     * 
     * @param  dnsServer  the DNS server.
     */
    public void setDnsServer(DNSServer dnsServer) {
        this.dnsServer = dnsServer;
    }
    
    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Set the records.
     * 
     * @param  records  the records.
     */
    public void setRecords(List<Record> records) {
        this.records = replaceNull(records, new ArrayList<Record>());
    }
    
    /**
     * Set the supported record types.
     * 
     * @param  supportedRecordTypes  the supported record types.
     */
    public void setSupportedRecordTypes(List<RecordType> supportedRecordTypes) {
        this.supportedRecordTypes = replaceNull(supportedRecordTypes, new ArrayList<RecordType>());
    }
    
    /**
     * Set the type.
     * 
     * @param  type  the type.
     */
    public void setType(ZoneType type) {
        this.type = type;
    }
}
