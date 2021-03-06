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

import org.lazydog.jdnsaas.utility.ZoneUtility;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.Resolver;
import org.lazydog.jdnsaas.model.SOARecord;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Update;
import org.xbill.DNS.ZoneTransferException;
import org.xbill.DNS.ZoneTransferIn;

/**
 * DNS server executor.
 * 
 * @author  Ron Rickard
 */
public final class DNSServerExecutor {
  
    private static final Logger logger = LoggerFactory.getLogger(DNSServerExecutor.class);
    private TSIGKey queryTSIGKey;
    private RecordConverter recordConverter;
    private List<Resolver> resolvers;
    private TSIGKey transferTSIGKey;
    private TSIGKey updateTSIGKey;
    private ZoneUtility zoneUtility;
    
    /**
     * Hide the constructor.
     * 
     * @param  zone  the zone.
     */
    private DNSServerExecutor(final Zone zone) {
        this.resolvers = zone.getView().getResolvers();
        this.queryTSIGKey = zone.getQueryTSIGKey();
        this.transferTSIGKey = zone.getTransferTSIGKey();
        this.updateTSIGKey = zone.getUpdateTSIGKey();
        this.recordConverter = RecordConverter.newInstance(zone.getName());
        this.zoneUtility = ZoneUtility.newInstance(zone.getName());
    }

    /**
     * Create the IP socket address.
     * 
     * @param  address  the address.
     * @param  port     the port.
     * 
     * @return  the IP socket address.
     * 
     * @throws  UnknownHostException  if the address is invalid.
     */
    private static InetSocketAddress createInetSocketAddress(final String address, final int port) throws UnknownHostException {
        return (address != null) ? new InetSocketAddress(InetAddress.getByName(address), port) : null;
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
    private Lookup createLookup(final RecordType recordType, final String recordName) throws TextParseException, UnknownHostException {
        
        // Create the lookup.
        Lookup lookup = new Lookup(recordName, RecordConverter.getDnsRecordType((recordType == null) ? RecordType.ANY : recordType));
        lookup.setCache(null);
        lookup.setResolver(this.createExtendedResolver(this.queryTSIGKey));
        lookup.setSearchPath(new Name[] {Name.fromString(this.zoneUtility.getAbsoluteZoneName())});
        
        return lookup;
    }

    /**
     * Create the extended resolver.
     * 
     * @param  tsigKey  the transaction signature (TSIG) key.
     * 
     * @return  the extended resolver.
     * 
     * @throws  UnknownHostException  if the host name or local host name is invalid.
     */
    private ExtendedResolver createExtendedResolver(final TSIGKey tsigKey) throws UnknownHostException {

        List<SimpleResolver> simpleResolvers = new ArrayList<SimpleResolver>();
        
        // Loop through the resolvers.
        for (Resolver resolver : this.resolvers) {

            // Create the simple resolver.
            SimpleResolver simpleResolver = new SimpleResolver();
            simpleResolver.setAddress(createInetSocketAddress(resolver.getAddress(), resolver.getPort()));
            simpleResolver.setLocalAddress(createInetSocketAddress(resolver.getLocalAddress(), 0));
            simpleResolver.setTCP(true);
            simpleResolver.setTSIGKey(createTSIGKey(tsigKey));
            
            // Add the simple resolver to the list.
            simpleResolvers.add(simpleResolver);
        }
        
        return new ExtendedResolver(simpleResolvers.toArray(new SimpleResolver[simpleResolvers.size()]));
    }
        
    /**
     * Create the TSIG key.
     * 
     * @param  tsigKey  the transaction signature (TSIG) key.
     * 
     * @return  the TSIG key.
     */
    private static TSIG createTSIGKey(final TSIGKey tsigKey) {
        return (tsigKey != null && tsigKey.getAlgorithm() != null && tsigKey.getName() != null && tsigKey.getValue() != null) ? new TSIG(tsigKey.getAlgorithm().asString(), tsigKey.getName(), tsigKey.getValue()) : null;
    }
        
    /**
     * Create the full zone transfer.
     * 
     * @param  resolver  the resolver.
     * 
     * @return  the full zone transfer.
     * 
     * @throws  TextParseException    if the zone name is invalid.
     * @throws  UnknownHostException  if the host name or local host name is invalid.
     */
    private ZoneTransferIn createFullZoneTransfer(final Resolver resolver) throws TextParseException, UnknownHostException {
        
        // Create the zone transfer.
        ZoneTransferIn zoneTransfer = ZoneTransferIn.newAXFR(Name.fromString(this.zoneUtility.getAbsoluteZoneName()), resolver.getAddress(), resolver.getPort(), createTSIGKey(this.transferTSIGKey));
        zoneTransfer.setLocalAddress(createInetSocketAddress(resolver.getLocalAddress(), 0));
        
        return zoneTransfer;
    }

    /**
     * Create the incremental zone transfer.
     * 
     * @param  resolver      the resolver.
     * @param  serialNumber  the serial number.
     * 
     * @return  the incremental zone transfer.
     * 
     * @throws  TextParseException    if the zone name is invalid.
     * @throws  UnknownHostException  if the host name or local host name is invalid. 
     */
    private ZoneTransferIn createIncrementalZoneTransfer(final Resolver resolver, final long serialNumber) throws TextParseException, UnknownHostException {
        
        // Create the zone transfer.
        ZoneTransferIn zoneTransfer = ZoneTransferIn.newIXFR(Name.fromString(this.zoneUtility.getAbsoluteZoneName()), serialNumber, false, resolver.getAddress(), resolver.getPort(), createTSIGKey(this.transferTSIGKey));
        zoneTransfer.setLocalAddress(createInetSocketAddress(resolver.getLocalAddress(), 0));
        
        return zoneTransfer;
    }

    /**
     * Find the records.
     * 
     * @return  the records.
     * 
     * @throws  DNSServerExecutorException  if unable to find the records.
     */
    public List<Record> findRecords() throws DNSServerExecutorException {
        
        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
        
        try {

            // Find the records with a zone transfer.
            records = this.findRecordsWithFullZoneTransfer();
            records = removeDuplicateSOARecord(records);
        } catch (Exception e) {
            throw new DNSServerExecutorException("Unable to find the records.", e);
        }
        
        return records;
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
    public List<Record> findRecords(final RecordType recordType, final String recordName) throws DNSServerExecutorException {

        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
        
        try {

            // Find the records with a lookup.
            records = this.findRecordsWithLookup(recordType, recordName);
        } catch (Exception e) {
            throw new DNSServerExecutorException("Unable to find the records for the record type " + recordType + " and the record name " + recordName + ".", e);
        }
        
        return records;
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
    private List<Record> findRecordsWithLookup(final RecordType recordType, final String recordName) throws TextParseException, UnknownHostException {
        org.xbill.DNS.Record[] records = this.createLookup(recordType, recordName).run();
        return (records != null) ? this.recordConverter.fromDnsRecords(Arrays.asList(records), true) : new ArrayList<Record>();
    }

    /**
     * Find the records using a full (AXFR) zone transfer.
     * 
     * @return  the records.
     * 
     * @throws  IOException            if the zone transfer fails due to an IO problem.
     * @throws  TextParseException     if the zone name is invalid.
     * @throws  UnknownHostException   if the host name or local host name is invalid.
     * @throws  ZoneTransferException  if the zone transfer fails.
     */
    @SuppressWarnings("unchecked")
    private List<Record> findRecordsWithFullZoneTransfer() throws IOException, TextParseException, UnknownHostException, ZoneTransferException {
        
        List<org.xbill.DNS.Record> records = null;
        
        // Loop through the resolvers.
        for (Resolver resolver : this.resolvers) {
            
            try {
                records = this.createFullZoneTransfer(resolver).run();
                break;
            } catch (ZoneTransferException e) {
                logger.error("Unable to find the records with a full zone transfer using resolver {}.", resolver, e);
            }
        }
        
        return (records != null) ? this.recordConverter.fromDnsRecords(records, true) : new ArrayList<Record>();
    }
    
    /**
     * Get the serial number from the records representing a zone.
     * 
     * @param  records  the records representing a zone.
     * 
     * @return  the serial number.
     */
    private static long getSerialNumber(List<Record> records) {
        
        long serialNumber = 0L;

        for (Record record : records) {
            
            if (record.getType() == RecordType.SOA) {
                serialNumber = ((SOARecord)record).getSerialNumber();
                break;
            }
        }
        
        return serialNumber;
    }
    
    /**
     * Create a new instance of the DNS server executor class.
     * 
     * @param  zone  the zone.
     * 
     * @return  a new instance of the DNS server executor class.
     */
    public static DNSServerExecutor newInstance(final Zone zone) {
        return new DNSServerExecutor(zone);
    }
    
    /**
     * Process the record operations.
     * 
     * @param  records  the records.
     * 
     * @return  true if the record operations are processed successfully, otherwise false.
     * 
     * @throws  DNSServerExecutorException  if unable to process the record operations due to an exception.
     */
    public boolean processRecordOperations(final List<Record> records) throws DNSServerExecutorException {
        
        // Initialize success to false.
        boolean success = false;
        
        try {

            // Create the update.
            Update update = new Update(Name.fromString(this.zoneUtility.getAbsoluteZoneName()));
            logger.debug("Processing record operations...");

            // Loop through the record operation map.
            for (Record record : records) {
                
                // Get the record and operation.
                logger.debug("  {}: {}", record.getOperation().toString(), record);
                
                if ("ADD".equals(record.getOperation().toString().toUpperCase())) {
                    update.add(this.recordConverter.toDnsRecord(record, false));
                } else if ("DELETE".equals(record.getOperation().toString().toUpperCase())) {
                    update.delete(this.recordConverter.toDnsRecord(record, false));
                } else if ("REPLACE".equals(record.getOperation().toString().toUpperCase())) {
                    update.replace(this.recordConverter.toDnsRecord(record, false));
                }
            }
            
            // Perform the operations and check if the operations were successful.
            int errorCode = createExtendedResolver(this.updateTSIGKey).send(update).getRcode();
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
     * When performing a full (AXFR) zone transfer, the SOA record is the first and last record in the returned records.
     * 
     * @param  records     the records.
     * 
     * @return  the records with the duplicate SOA record removed.
     */
    private static List<Record> removeDuplicateSOARecord(List<Record> records) {

        // Remove the last record in the records since it is the duplicate SOA record.
        records.remove(records.size() - 1);

        return records;
    }    

    /**
     * Update the records representing a zone with the current records in DNS for the zone.
     * 
     * @param  records  the records representing a zone.
     * 
     * @return  the new serial number of the SOA record in the updated records.
     * 
     * @throws  DNSServerExecutorException  if unable to update the records.
     */
    @SuppressWarnings("unchecked")
    public long updateRecords(List<Record> records) throws DNSServerExecutorException {

        // Get the serial number.
        long serialNumber = getSerialNumber(records);
        
        // Loop through the resolvers.
        for (Resolver resolver : this.resolvers) {
            
            try {
                List<org.xbill.DNS.ZoneTransferIn.Delta> deltas = this.createIncrementalZoneTransfer(resolver, serialNumber).run();
                logger.debug("{} deltas total.", deltas.size());
                for (org.xbill.DNS.ZoneTransferIn.Delta delta : deltas) {

                    logger.debug("  delta {}/{} - {} delete records found.", delta.start, delta.end, delta.deletes.size());
                    for (org.xbill.DNS.Record dnsRecord : (List<org.xbill.DNS.Record>)delta.deletes) {
                        Record record = this.recordConverter.fromDnsRecord(dnsRecord, true);
                        if (records.remove(record)) {
                            logger.debug("    Deleted record {}.", record);
                        } else {
                            logger.warn("    Unable to delete the record {}.", record);
                        }
                    }
                    
                    logger.debug("  delta {}/{} - {} add records found.", delta.start, delta.end, delta.adds.size());
                    for (org.xbill.DNS.Record dnsRecord : (List<org.xbill.DNS.Record>)delta.adds) {
                        Record record = this.recordConverter.fromDnsRecord(dnsRecord, true);
                        if (records.add(record)) {
                            logger.debug("    Added record {}.", record);
                        } else {
                            logger.warn("    Unable to add the record {}.", record);
                        }
                    }
                    
                    serialNumber = delta.end;
                }
                break;
            } catch (Exception e) {
                logger.error("Unable to update the records with an incremental zone transfer using resolver {}.", resolver, e);
            }
        }
        
        return serialNumber;
    }
}
