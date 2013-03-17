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
package org.lazydog.jdnsaas.bind;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import org.xbill.DNS.Update;
import org.xbill.DNS.ZoneTransferException;
import org.xbill.DNS.ZoneTransferIn;

/**
 * Domain name server.
 * 
 * @author  Ron Rickard
 */
final class DomainNameServer {
  
    private String dnsServerName;
    private int dnsServerPort;
    private String transactionSignatureAlgorithmName;
    private String transactionSignatureName;
    private String transactionSignatureSecret;
    private ZoneResolver zoneResolver;
    
    /**
     * Hide the constructor.
     * 
     * @param  dnsServerName                      the DNS server name.
     * @param  dnsServerPort                      the DNS server port.
     * @param  transactionSignatureAlgorithmName  the transaction signature algorithm name.
     * @param  transactionSignatureName           the transaction signature name.
     * @param  transactionSignatureSecret         the transaction signature secret.
     * @param  zoneName                           the zone name.
     */
    private DomainNameServer(final String dnsServerName, final int dnsServerPort, final String transactionSignatureAlgorithmName, final String transactionSignatureName, final String transactionSignatureSecret, final String zoneName) {
        this.dnsServerName = dnsServerName;
        this.dnsServerPort = dnsServerPort;
        this.transactionSignatureAlgorithmName = transactionSignatureAlgorithmName;
        this.transactionSignatureName = transactionSignatureName;
        this.transactionSignatureSecret = transactionSignatureSecret;
        this.zoneResolver = ZoneResolver.newInstance(zoneName);
    }

    /**
     * Create the lookup.
     * 
     * @param  recordType  the record type.
     * @param  recordName  the record name.
     * 
     * @return  the created lookup.
     * 
     * @throws  TextParseException    if the zone name or record name is invalid.
     * @throws  UnknownHostException  if the DNS server name is invalid.
     */
    private Lookup createLookup(final int recordType, final String recordName) throws TextParseException, UnknownHostException {
        
        // Create the lookup.
        Lookup lookup = new Lookup(recordName, recordType);
        lookup.setCache(null);
        lookup.setResolver(createResolver(false));
        lookup.setSearchPath(new Name[] {new Name(this.zoneResolver.getAbsoluteZoneName())});
        
        return lookup;
    }
    
    /**
     * Create the resolver.
     * 
     * @param  useDynamicDns  true if Dynamic DNS will be used, otherwise false.
     * 
     * @return  the created resolver.
     * 
     * @throws  UnknownHostException  if the DNS server name is invalid.
     */
    private Resolver createResolver(final boolean useDynamicDns) throws UnknownHostException {

        // Create the resolver.
        Resolver resolver = new SimpleResolver(this.dnsServerName);
        resolver.setPort(this.dnsServerPort);
        resolver.setTCP(true);

        // Check if Dynamic DNS will be used.
        if (useDynamicDns) {

            // Set the transaction signature on the resolver.
            TSIG tsig = new TSIG(this.transactionSignatureAlgorithmName, this.transactionSignatureName, this.transactionSignatureSecret);
            resolver.setTSIGKey(tsig);
        } 
        
        return resolver;
    }
     
    /**
     * Get the records using a zone transfer.
     * 
     * @return  the records.
     * 
     * @throws  IOException            if the zone transfer fails due to an IO problem.
     * @throws  TextParseException     if the zone name is invalid.
     * @throws  UnknownHostException   if the DNS server name is invalid.
     * @throws  ZoneTransferException  if the zone transfer fails.
     */
    @SuppressWarnings("unchecked")
    private List<Record> getRecordsWithZoneTransfer() throws IOException, TextParseException, UnknownHostException, ZoneTransferException {
        return ZoneTransferIn.newAXFR(new Name(this.zoneResolver.getAbsoluteZoneName()), this.dnsServerName, this.dnsServerPort, null).run();
    }
    
    /**
     * Get the records using a lookup.
     * 
     * @param  recordType  the record type.
     * @param  recordName  the record name.
     * 
     * @return  the records.
     * 
     * @throws  TextParseException    if the zone name or record name is invalid.
     * @throws  UnknownHostException  if the DNS server name is invalid.
     */
    private List<Record> getRecordsWithLookup(final int recordType, final String recordName) throws TextParseException, UnknownHostException {
        return Arrays.asList(createLookup(recordType, recordName).run());
    }

    /**
     * Get the records.
     * 
     * @param  recordType  the record type.
     * @param  recordName  the record name.
     * 
     * @return  the records.
     * 
     * @throws  DomainNameServiceException  if unable to get the records.
     */
    public List<Record> getRecords(final int recordType, final String recordName) throws DomainNameServerException {

        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
        
        try {
            
            // Check if the record name does not exist.
            if (recordName == null) {
                
                // Get all the records with a zone transfer.
                List<Record> allRecords = this.getRecordsWithZoneTransfer();
                
                // Check if the desired record type is not all records.
                if (recordType != Type.ANY) {
                    
                    // Loop through all the records.
                    for (Record record : allRecords) {
                        
                        // Check if the record is the desired record type.
                        if (recordType == record.getType()) {
                            
                            // Add the record to the list.
                            records.add(record);
                        }
                    }
                } else {
                    records = allRecords;
                }
            } else {
                
                // Get the records with a lookup.
                records = this.getRecordsWithLookup(recordType, recordName);
            }
        } catch (Exception e) {
            throw new DomainNameServerException("Unable to get the records for record type, " + recordType + ", and record name, " + recordName + ".", e);
        }
        
        return records;
    }
    
    /**
     * Create a new instance of the domain name server class.
     * 
     * @param  dnsServerName                      the DNS server name.
     * @param  dnsServerPort                      the DNS server port.
     * @param  transactionSignatureAlgorithmName  the transaction signature algorithm name.
     * @param  transactionSignatureName           the transaction signature name.
     * @param  transactionSignatureSecret         the transaction signature secret.
     * @param  zoneName                           the zone name.
     * 
     * @return  a new instance of the domain name server class.
     */
    public static DomainNameServer newInstance(final String dnsServerName, final int dnsServerPort, final String transactionSignatureAlgorithmName, final String transactionSignatureName, final String transactionSignatureSecret, final String zoneName) {
        return new DomainNameServer(dnsServerName, dnsServerPort, transactionSignatureAlgorithmName, transactionSignatureName, transactionSignatureSecret, zoneName);
    }
    
    /**
     * Process the records.
     * 
     * @param  recordOperationMap  the record operation map.
     * 
     * @return  true if the records are processed successfully, otherwise false.
     * 
     * @throws  DomainNameServiceException  if unable to process the records due to an exception.
     */
    public boolean processRecords(final Map<Record,String> recordOperationMap) throws DomainNameServerException {
        
        // Initialize success to false.
        boolean success = false;
        
        try {

            // Create the update.
            Update update = new Update(new Name(this.zoneResolver.getAbsoluteZoneName()));

            // Loop through the record operation map.
            for (Map.Entry<Record,String> entry : recordOperationMap.entrySet()) {
                
                // Get the record and operation.
                Record record = entry.getKey();
                String operation = entry.getValue();
                
                if ("ADD".equals(operation.toUpperCase())) {
                    update.add(record);
                } else if ("DELETE".equals(operation.toUpperCase())) {
                    update.delete(record);
                } else if ("REPLACE".equals(operation.toUpperCase())) {
                    update.replace(record);
                }
            }
            
            // Perform the operations and check if the operations were successful.
            if (createResolver(true).send(update).getRcode() == Rcode.NOERROR) {
                success = true;
            }
        } catch (Exception e) {
            throw new DomainNameServerException("Unable to process the records due to an exception.", e);
        }
        
        return success;
    }
}
