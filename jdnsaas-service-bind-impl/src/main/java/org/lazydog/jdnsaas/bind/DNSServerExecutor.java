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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.lazydog.jdnsaas.model.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import org.xbill.DNS.Update;
import org.xbill.DNS.ZoneTransferException;
import org.xbill.DNS.ZoneTransferIn;

/**
 * DNS server executor.
 * 
 * @author  Ron Rickard
 */
final class DNSServerExecutor {
  
    private static final Logger logger = LoggerFactory.getLogger(DNSServerExecutor.class);
    private List<Resolver> resolvers;
    private ZoneUtility zoneUtility;
    
    /**
     * Hide the constructor.
     * 
     * @param  resolvers  the resolvers.
     * @param  zoneName   the zone name.
     */
    private DNSServerExecutor(final List<Resolver> resolvers, final String zoneName) {
        this.resolvers = resolvers;
        this.zoneUtility = ZoneUtility.newInstance(zoneName);
    }

    /**
     * Create the IP socket address.
     * 
     * @param  hostName  the host name.
     * @param  port      the port.
     * 
     * @return  the IP socket address.
     * 
     * @throws  UnknownHostException  if the host name is invalid.
     */
    private static InetSocketAddress createInetSocketAddress(String hostName, int port) throws UnknownHostException {
        return (hostName != null) ? new InetSocketAddress(InetAddress.getByName(hostName), port) : null;
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
     * @throws  UnknownHostException  if the host name or local host name is invalid.
     */
    private Lookup createLookup(final int recordType, final String recordName) throws TextParseException, UnknownHostException {
        
        // Create the lookup.
        Lookup lookup = new Lookup(recordName, recordType);
        lookup.setCache(null);
        lookup.setResolver(this.createExtendedResolver());
        lookup.setSearchPath(new Name[] {Name.fromString(this.zoneUtility.getAbsoluteZoneName())});
        
        return lookup;
    }

    /**
     * Create the extended resolver.
     * 
     * @return  the created extended resolver.
     * 
     * @throws  UnknownHostException  if the host name or local host name is invalid.
     */
    private ExtendedResolver createExtendedResolver() throws UnknownHostException {

        List<SimpleResolver> simpleResolvers = new ArrayList<SimpleResolver>();
        
        // Loop through the resolvers.
        for (Resolver resolver : this.resolvers) {

            // Create the simple resolver.
            SimpleResolver simpleResolver = new SimpleResolver();
            simpleResolver.setAddress(createInetSocketAddress(resolver.getHostName(), resolver.getPort()));
            simpleResolver.setLocalAddress(createInetSocketAddress(resolver.getLocalHostName(), 0));
            simpleResolver.setTCP(true);
            simpleResolver.setTSIGKey(createTSIGKey(resolver));
            
            // Add the simple resolver to the list.
            simpleResolvers.add(simpleResolver);
        }
        
        return new ExtendedResolver(simpleResolvers.toArray(new SimpleResolver[simpleResolvers.size()]));
    }
        
    /**
     * Create the TSIG key.
     * 
     * @param  resolver  the resolver.
     * 
     * @return  the TSIG key.
     */
    private static TSIG createTSIGKey(Resolver resolver) {
        return (resolver.getTSIGKey() != null && resolver.getTSIGKey().getName() != null) ? new TSIG(resolver.getTSIGKey().getAlgorithm().asString(), resolver.getTSIGKey().getName(), resolver.getTSIGKey().getValue()) : null;
    }
        
    /**
     * Create the zone transfer.
     * 
     * @param  resolver  the resolver.
     * 
     * @return  the zone transfer.
     * 
     * @throws TextParseException    if the zone name is invalid.
     * @throws UnknownHostException  if the host name or local host name is invalid.
     */
    private ZoneTransferIn createZoneTransfer(Resolver resolver) throws TextParseException, UnknownHostException {
        
        // Create the zone transfer.
        ZoneTransferIn zoneTransfer = ZoneTransferIn.newAXFR(Name.fromString(this.zoneUtility.getAbsoluteZoneName()), resolver.getHostName(), resolver.getPort(), createTSIGKey(resolver));
        zoneTransfer.setLocalAddress(createInetSocketAddress(resolver.getLocalHostName(), 0));
        
        return zoneTransfer;
    }
    
    /**
     * Find the records.
     * 
     * @param  recordType  the record type.
     * 
     * @return  the records.
     * 
     * @throws  DNSServerExecutorException  if unable to find the records.
     */
    public List<Record> findRecords(final int recordType) throws DNSServerExecutorException {
        
        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
        
        try {
            
            // Find all the records with a zone transfer.
            List<Record> allRecords = this.findRecordsWithZoneTransfer();

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
        } catch (Exception e) {
            throw new DNSServerExecutorException("Unable to find the records for record type, " + recordType + ".", e);
        }

        return removeDuplicateSOARecord(records, recordType);
    }
    
    /**
     * Find the records.
     * 
     * @param  recordType  the record type.
     * @param  recordName  the record name.
     * 
     * @return  the records.
     * 
     * @throws  DNSServerExecutorException  if unable to find the records.
     */
    public List<Record> findRecords(final int recordType, final String recordName) throws DNSServerExecutorException {

        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
        
        try {

            // Find the records with a lookup.
            records = this.findRecordsWithLookup(recordType, recordName);
        } catch (Exception e) {
            throw new DNSServerExecutorException("Unable to find the records for record type, " + recordType + ", and record name, " + recordName + ".", e);
        }
        
        return removeDuplicateSOARecord(records, recordType);
    }

    /**
     * Find the records using a lookup.
     * 
     * @param  recordType  the record type.
     * @param  recordName  the record name.
     * 
     * @return  the records.
     * 
     * @throws  TextParseException    if the zone name or record name is invalid.
     * @throws  UnknownHostException  if the host name or local host name is invalid.
     */
    private List<Record> findRecordsWithLookup(final int recordType, final String recordName) throws TextParseException, UnknownHostException {
        Record[] records = this.createLookup(recordType, recordName).run();
        return (records != null) ? Arrays.asList(records) : new ArrayList<Record>();
    }

    /**
     * Find the records using a zone transfer.
     * 
     * @return  the records.
     * 
     * @throws  IOException            if the zone transfer fails due to an IO problem.
     * @throws  TextParseException     if the zone name is invalid.
     * @throws  UnknownHostException   if the host name or local host name is invalid.
     * @throws  ZoneTransferException  if the zone transfer fails.
     */
    @SuppressWarnings("unchecked")
    private List<Record> findRecordsWithZoneTransfer() throws IOException, TextParseException, UnknownHostException, ZoneTransferException {
        
        List<Record> records = null;
        
        // Loop through the resolvers.
        for (Resolver resolver : this.resolvers) {
            
            try {
                records = this.createZoneTransfer(resolver).run();
                break;
            } catch (ZoneTransferException e) {
                logger.error("Unable to find records with zone transfer.", e);
            }
        }
        
        return (records != null) ? records : new ArrayList<Record>();
    }

    /**
     * Create a new instance of the DNS server executor class.
     * 
     * @param  resolvers  the resolvers.
     * @param  zoneName   the zone name.
     * 
     * @return  a new instance of the DNS server executor class.
     */
    public static DNSServerExecutor newInstance(final List<Resolver> resolvers, final String zoneName) {
        return new DNSServerExecutor(resolvers, zoneName);
    }
    
    /**
     * Process the record operations.
     * 
     * @param  recordOperationMap  the record operation map.
     * 
     * @return  true if the record operations are processed successfully, otherwise false.
     * 
     * @throws  DNSServerExecutorException  if unable to process the record operations due to an exception.
     */
    public boolean processRecordOperations(final Map<Record,String> recordOperationMap) throws DNSServerExecutorException {
        
        // Initialize success to false.
        boolean success = false;
        
        try {

            // Create the update.
            Update update = new Update(Name.fromString(this.zoneUtility.getAbsoluteZoneName()));
            logger.debug("Processing record operations...");

            // Loop through the record operation map.
            for (Map.Entry<Record,String> entry : recordOperationMap.entrySet()) {
                
                // Get the record and operation.
                Record record = entry.getKey();
                String operation = entry.getValue();
                logger.debug("    {}: {}", operation, record);
                
                if ("ADD".equals(operation.toUpperCase())) {
                    update.add(record);
                } else if ("DELETE".equals(operation.toUpperCase())) {
                    update.delete(record);
                } else if ("REPLACE".equals(operation.toUpperCase())) {
                    update.replace(record);
                }
            }
            
            // Perform the operations and check if the operations were successful.
            int errorCode = createExtendedResolver().send(update).getRcode();
            if (errorCode == Rcode.NOERROR) {
                success = true;
            } else {
                logger.error("Unable to process the record operations due to {}", Rcode.string(errorCode));
            }
        } catch (Exception e) {
            throw new DNSServerExecutorException("Unable to process the record operations due to an exception.", e);
        }
      
        return success;
    }
         
    /**
     * Remove the duplicate SOA record.
     * 
     * @param  records     the records.
     * @param  recordType  the record type.
     * 
     * @return  the records with the duplicate SOA record removed.
     */
    private static List<Record> removeDuplicateSOARecord(List<Record> records, final int recordType) {
        
        // Check if the record type is any or SOA.
        if (recordType == Type.ANY || recordType == Type.SOA) {
            
            // Remove the last record in the records since it is the duplicate SOA record.
            records.remove(records.size() - 1);
        }
        
        return records;
    }
}
