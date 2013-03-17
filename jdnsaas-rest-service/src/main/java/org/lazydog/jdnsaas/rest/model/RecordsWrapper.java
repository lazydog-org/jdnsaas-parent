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
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.lazydog.jdnsaas.model.AAAARecord;
import org.lazydog.jdnsaas.model.ARecord;
import org.lazydog.jdnsaas.model.CNAMERecord;
import org.lazydog.jdnsaas.model.MXRecord;
import org.lazydog.jdnsaas.model.NSRecord;
import org.lazydog.jdnsaas.model.PTRRecord;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.SRVRecord;
import org.lazydog.jdnsaas.model.TXTRecord;

/**
 * Records wrapper.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement
public class RecordsWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
    @JsonSubTypes({
        @Type(value=AAAARecord.class, name="AAAA"),
        @Type(value=ARecord.class, name="A"),
        @Type(value=CNAMERecord.class, name="CNAME"),
        @Type(value=MXRecord.class, name="MX"),
        @Type(value=NSRecord.class, name="NS"),
        @Type(value=PTRRecord.class, name="PTR"),
        @Type(value=SRVRecord.class, name="SRV"),
        @Type(value=TXTRecord.class, name="TXT")
    })
    private List<Record> records = new ArrayList<Record>();
    
    /**
     * Get the records.
     * 
     * @return  the records.
     */
    public List<Record> getRecords() {
        return this.records;
    }
        
    /**
     * Create a new instance of the records wrapper class.
     * 
     * @param  records  the records.
     * 
     * @return  a new instance of the records wrapper class.
     */
    public static RecordsWrapper newInstance(List<Record> records) {
        
        RecordsWrapper recordsWrapper = new RecordsWrapper();
        recordsWrapper.setRecords(records);
        
        return recordsWrapper;
    }
    
    /**
     * Set the records.
     * 
     * @param  records  the records.
     */
    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
