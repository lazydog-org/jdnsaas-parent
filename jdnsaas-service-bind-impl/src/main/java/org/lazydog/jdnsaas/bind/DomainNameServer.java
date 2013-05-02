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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  
    private final Logger logger = LoggerFactory.getLogger(DomainNameServer.class);
    private String dnsServerName;
    private int dnsServerPort;
    private String localAddress;
    private String tsigKeyAlgorithm;
    private String tsigKeyName;
    private String tsigKeyValue;
    private ZoneResolver zoneResolver;
    
    /**
     * Hide the constructor.
     * 
     * @param  dnsServerName     the DNS server name.
     * @param  dnsServerPort     the DNS server port.
     * @param  localAddress      the local address.
     * @param  tsigKeyAlgorithm  the transaction signature (TSIG) key algorithm name.
     * @param  tsigKeyName       the transaction signature (TSIG) key name.
     * @param  tsigKeyValue      the transaction signature (TSIG) key value.
     * @param  zoneName          the zone name.
     */
    private DomainNameServer(final String dnsServerName, final int dnsServerPort, final String localAddress, final String tsigKeyAlgorithm, final String tsigKeyName, final String tsigKeyValue, final String zoneName) {
        this.dnsServerName = dnsServerName;
        this.dnsServerPort = dnsServerPort;
        this.localAddress = localAddress;
        this.tsigKeyAlgorithm = tsigKeyAlgorithm;
        this.tsigKeyName = tsigKeyName;
        this.tsigKeyValue = tsigKeyValue;
        this.zoneResolver = ZoneResolver.newInstance(zoneName);
    }

    /**
     * Create the IP socket address.
     * 
     * @return  the IP socket address.
     * 
     * @throws  UnknownHostException  if the local address is invalid.
     */
    private InetSocketAddress createInetSocketAddress() throws UnknownHostException {
        return (this.localAddress != null) ? new InetSocketAddress(InetAddress.getByName(this.localAddress), 0) : null;
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
        lookup.setResolver(this.createResolver());
        lookup.setSearchPath(new Name[] {new Name(this.zoneResolver.getAbsoluteZoneName())});
        
        return lookup;
    }

    /**
     * Create the resolver.
     * 
     * @return  the created resolver.
     * 
     * @throws  UnknownHostException  if the DNS server name is invalid.
     */
    private Resolver createResolver() throws UnknownHostException {

        // Create the resolver.
        SimpleResolver resolver = new SimpleResolver(this.dnsServerName);
        if (this.localAddress != null) {
            resolver.setLocalAddress(createInetSocketAddress());
        }
        resolver.setPort(this.dnsServerPort);
        resolver.setTCP(true);
        resolver.setTSIGKey(this.createTSIGKey());
        
        return resolver;
    }
        
    /**
     * Create the TSIG key.
     * 
     * @return  the TSIG key.
     */
    private TSIG createTSIGKey() {
        return (this.tsigKeyName != null) ? new TSIG(this.tsigKeyAlgorithm, this.tsigKeyName, this.tsigKeyValue) : null;
    }
        
    /**
     * Create the zone transfer.
     * 
     * @return  the zone transfer.
     * 
     * @throws TextParseException    if the zone name is invalid.
     * @throws UnknownHostException  if the DNS server name is invalid.
     */
    private ZoneTransferIn createZoneTransfer() throws TextParseException, UnknownHostException {
        
        // Create the zone transfer.
        ZoneTransferIn zoneTransfer = ZoneTransferIn.newAXFR(new Name(this.zoneResolver.getAbsoluteZoneName()), this.dnsServerName, this.dnsServerPort, this.createTSIGKey());
        if (this.localAddress != null) {
            zoneTransfer.setLocalAddress(createInetSocketAddress());
        }
        
        return zoneTransfer;
    }
    
    /**
     * Find the records.
     * 
     * @param  recordType  the record type.
     * 
     * @return  the records.
     * 
     * @throws  DomainNameServiceException  if unable to find the records.
     */
    public List<Record> findRecords(final int recordType) throws DomainNameServerException {
        
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
            throw new DomainNameServerException("Unable to find the records for record type, " + recordType + ".", e);
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
     * @throws  DomainNameServiceException  if unable to find the records.
     */
    public List<Record> findRecords(final int recordType, final String recordName) throws DomainNameServerException {

        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
        
        try {

            // Find the records with a lookup.
            records = this.findRecordsWithLookup(recordType, recordName);
        } catch (Exception e) {
            throw new DomainNameServerException("Unable to find the records for record type, " + recordType + ", and record name, " + recordName + ".", e);
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
     * @throws  UnknownHostException  if the DNS server name is invalid.
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
     * @throws  UnknownHostException   if the DNS server name is invalid.
     * @throws  ZoneTransferException  if the zone transfer fails.
     */
    @SuppressWarnings("unchecked")
    private List<Record> findRecordsWithZoneTransfer() throws IOException, TextParseException, UnknownHostException, ZoneTransferException {
        List<Record> records = this.createZoneTransfer().run();
        return (records != null) ? records : new ArrayList<Record>();
    }

    /**
     * Create a new instance of the domain name server class.
     * 
     * @param  dnsServerName     the DNS server name.
     * @param  dnsServerPort     the DNS server port.
     * @param  localAddress      the local address.
     * @param  zoneName          the zone name.
     * 
     * @return  a new instance of the domain name server class.
     */
    public static DomainNameServer newInstance(final String dnsServerName, final int dnsServerPort, final String localAddress, final String zoneName) {
        return newInstance(dnsServerName, dnsServerPort, localAddress, null, null, null, zoneName);
    }
    
    /**
     * Create a new instance of the domain name server class.
     * 
     * @param  dnsServerName     the DNS server name.
     * @param  dnsServerPort     the DNS server port.
     * @param  localAddress      the local address.
     * @param  tsigKeyAlgorithm  the transaction signature (TSIG) key algorithm name.
     * @param  tsigKeyName       the transaction signature (TSIG) key name.
     * @param  tsigKeyValue      the transaction signature (TSIG) key value.
     * @param  zoneName          the zone name.
     * 
     * @return  a new instance of the domain name server class.
     */
    public static DomainNameServer newInstance(final String dnsServerName, final int dnsServerPort, final String localAddress, final String tsigKeyAlgorithm, final String tsigKeyName, final String tsigKeyValue, final String zoneName) {
        return new DomainNameServer(dnsServerName, dnsServerPort, localAddress, tsigKeyAlgorithm, tsigKeyName, tsigKeyValue, zoneName);
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
            if (createResolver().send(update).getRcode() == Rcode.NOERROR) {
                success = true;
            }
        } catch (Exception e) {
            throw new DomainNameServerException("Unable to process the records due to an exception.", e);
        }
        
        return success;
    }
}
