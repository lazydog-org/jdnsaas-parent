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
package org.lazydog.jdnsaas.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.lazydog.jdnsaas.model.AAAARecord;
import org.lazydog.jdnsaas.model.ARecord;
import org.lazydog.jdnsaas.model.CNAMERecord;
import org.lazydog.jdnsaas.model.MXRecord;
import org.lazydog.jdnsaas.model.Model;
import org.lazydog.jdnsaas.model.NSRecord;
import org.lazydog.jdnsaas.model.PTRRecord;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.SOARecord;
import org.lazydog.jdnsaas.model.SRVRecord;
import org.lazydog.jdnsaas.model.TXTRecord;

/**
 * Record filter.
 * 
 * @author  Ron Rickard
 */
public class RecordFilter extends Model {
    
    private static final long serialVersionUID = 1L;
    private static final String SEARCH_STRING_WILDCARD = "*";
    private List<RecordType> recordTypes = new ArrayList<RecordType>(Arrays.asList(RecordType.ANY));
    private String searchString;
    private ZoneUtility zoneUtility;

    /**
     * Hide the constructor.
     * 
     * @param  zoneName      the zone name.
     * @param  recordTypes   the record types.
     * @param  searchString  the search string.
     */
    private RecordFilter(final String zoneName, final List<RecordType> recordTypes, final String searchString) {
        this.zoneUtility = ZoneUtility.newInstance(zoneName);
        this.recordTypes = replaceNull(recordTypes, new ArrayList<RecordType>(Arrays.asList(RecordType.ANY)));
        this.searchString = replaceNull(searchString, SEARCH_STRING_WILDCARD);
    }
    
    /**
     * Filter the records.
     * 
     * @param  records  the records.
     * 
     * @return  the filtered records.
     */
    public List<Record> filter(final List<Record> records) {
        
        List<Record> filteredRecords = new ArrayList<Record>();
        
        // Loop through the records.
        for (Record record : records) {
            
            // Add the record to the filtered records if it matches.
            if (this.matchRecord(record)) {
                filteredRecords.add(record);
            }
        }
        
        return filteredRecords;
    }
    
    /**
     * Does the AAAA record match the search string?
     * 
     * @param  record  the AAAA record.
     * 
     * @return  true if the AAAA record matches, otherwise false.
     */
    private boolean matchAAAARecord(final AAAARecord record) {
        return (this.matchAbsoluteName(record.getName()) || this.matchString(record.getIpv6Address()));
    }
    
    /**
     * Does the A record match the search string?
     * 
     * @param  record  the A record.
     * 
     * @return  true if the A record matches, otherwise false.
     */
    private boolean matchARecord(final ARecord record) {
        return (this.matchAbsoluteName(record.getName()) || this.matchString(record.getIpAddress()));
    }
       
    /**
     * Does the absolute name match the search string?
     * 
     * @param  name  the name.
     * 
     * @return  true if the absolute name matches, otherwise false.
     */
    private boolean matchAbsoluteName(final String name) {
        return this.matchString(this.zoneUtility.absolutize(name));
    }
    
    /**
     * Does the CNAME record match the search string?
     * 
     * @param  record  the CNAME record.
     * 
     * @return  true if the CNAME record matches, otherwise false.
     */
    private boolean matchCNAMERecord(final CNAMERecord record) {
        return (this.matchAbsoluteName(record.getName()) || this.matchAbsoluteName(record.getTarget()));
    }
    
    /**
     * Does the MX record match the search string?
     * 
     * @param  record  the MX record.
     * 
     * @return  true if the MX record matches, otherwise false.
     */
    private boolean matchMXRecord(final MXRecord record) {
        return (this.matchAbsoluteName(record.getName()) || this.matchAbsoluteName(record.getTarget()));
    }
    
    /**
     * Does the NS record match the search string?
     * 
     * @param  record  the NS record.
     * 
     * @return  true if the NS record matches, otherwise false.
     */
    private boolean matchNSRecord(final NSRecord record) {
        return (this.matchAbsoluteName(record.getName()) || this.matchAbsoluteName(record.getTarget()));
    }
    
    /**
     * Does the PTR record match the search string?
     * 
     * @param  record  the PTR record.
     * 
     * @return  true if the PTR record matches, otherwise false.
     */
    private boolean matchPTRRecord(final PTRRecord record) {
        return (this.matchString(record.getName()) || this.matchAbsoluteName(record.getTarget()));
    }
   
    /**
     * Does the record match the record filter?
     * 
     * @param  record  the record.
     * 
     * @return  true if the record matches, otherwise false.
     */
    private boolean matchRecord(final Record record) {
        boolean match = false;
        
        // Check if the record types contains any record type or the record types contains the record type.
        if (this.recordTypes.isEmpty() || this.recordTypes.contains(RecordType.ANY) || this.recordTypes.contains(record.getType())) {
            
            switch(record.getType()) {
                
                case A:
                    match = this.matchARecord((ARecord)record);
                    break;
                case AAAA:
                    match = this.matchAAAARecord((AAAARecord)record);
                    break;
                case CNAME:
                    match = this.matchCNAMERecord((CNAMERecord)record);
                    break;
                case MX:
                    match = this.matchMXRecord((MXRecord)record);
                    break;
                case NS:
                    match = this.matchNSRecord((NSRecord)record);
                    break;
                case PTR:
                    match = this.matchPTRRecord((PTRRecord)record);
                    break;
                case SOA:
                    match = this.matchSOARecord((SOARecord)record);
                    break;
                case SRV:
                    match = this.matchSRVRecord((SRVRecord)record);
                    break;
                case TXT:
                    match = this.matchTXTRecord((TXTRecord)record);
                    break;
            }
        }
        
        return match;
    }
    
    /**
     * Does the SOA record match the search string?
     * 
     * @param  record  the SOA record.
     * 
     * @return  true if the SOA record matches, otherwise false.
     */
    private boolean matchSOARecord(final SOARecord record) {
        return (this.matchAbsoluteName(record.getName()));
    }
   
    /**
     * Does the SRV record match the search string?
     * 
     * @param  record  the SRV record.
     * 
     * @return  true if the SRV record matches, otherwise false.
     */
    private boolean matchSRVRecord(final SRVRecord record) {
        return (this.matchAbsoluteName(record.getName()) || this.matchAbsoluteName(record.getTarget()));
    }

    /**
     * Does the value match the search string?
     * 
     * @param  value  the value.
     * 
     * @return  true if the value matches, otherwise false.
     */
    private boolean matchString(final String value) {
        boolean match = true;
        String[] searchTokens = StringUtils.split(this.searchString, SEARCH_STRING_WILDCARD);
        String stringValue = value;
        for (String searchToken : searchTokens) {
            if (StringUtils.indexOfIgnoreCase(stringValue, searchToken) != -1) {
                stringValue = StringUtils.substringAfter(stringValue, searchToken);
            } else {
                match = false;
                break;
            }
        }
        return match;
    }
     
    /**
     * Does one of the values match the search string?
     * 
     * @param  values  the values.
     * 
     * @return  true if one of the values matches, otherwise false.
     */
    private boolean matchStrings(final List<String> values) {
        boolean match = false;
        for (String value : values) {
            if (this.matchString(value)) {
                match = true;
                break;
            }
        }
        return match;
    }
    
    /**
     * Does the TXT record match the search string?
     * 
     * @param  record  the TXT record.
     * 
     * @return  true if the TXT record matches, otherwise false.
     */
    private boolean matchTXTRecord(final TXTRecord record) {
        return (this.matchAbsoluteName(record.getName()) || this.matchStrings(record.getValues()));
    }

    /**
     * Create a new instance of the record filter class.
     * 
     * @param  zoneName      the zone name.
     * @param  recordTypes   the record types.
     * @param  searchString  the search string.
     * 
     * @return  a new instance of the record filter class.
     */
    public static RecordFilter newInstance(final String zoneName, final List<RecordType> recordTypes, final String searchString) {
        return new RecordFilter(zoneName, recordTypes, searchString);
    }
}
