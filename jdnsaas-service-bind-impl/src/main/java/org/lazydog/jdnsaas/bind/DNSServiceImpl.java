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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lazydog.jdnsaas.DNSService;
import org.lazydog.jdnsaas.DNSServiceException;
import org.lazydog.jdnsaas.ResourceNotFoundException;
import org.lazydog.jdnsaas.internal.repository.DNSRepositoryImpl;
import org.lazydog.jdnsaas.model.DNSServer;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.spi.repository.DNSRepository;
import org.lazydog.jdnsaas.spi.repository.DNSRepositoryException;

/**
 * DNS service implementation.
 * 
 * @author  Ron Rickard
 */
public class DNSServiceImpl implements DNSService {
    
    private DNSRepository dnsRepository;

    /**
     * Hide the constructor.
     * 
     * @throws  DNSServiceException  if unable to initialize the DNS service due to an exception.
     */
    private DNSServiceImpl() throws DNSServiceException {
        
        try {
            
            // Initialize the DNS repository.
            this.dnsRepository = DNSRepositoryImpl.newInstance("jdnsaas");
        } catch (DNSRepositoryException e) {
            throw new DNSServiceException("Unable to initialize the DNS service.", e);
        }
    }

    /**
     * Create the domain name server.
     * 
     * @param  dnsServer  the DNS server.
     * @param  zoneName   the zone name.
     * 
     * @return  the domain name server.
     */
    private static DomainNameServer createDomainNameServer(final DNSServer dnsServer, final String zoneName) {
        return DomainNameServer.newInstance(dnsServer.getName(), dnsServer.getPort(), dnsServer.getTransactionSignature().getAlgorithm().getName(), dnsServer.getTransactionSignature().getName(), dnsServer.getTransactionSignature().getSecret(), zoneName);
    }

    /**
     * Get the DNS server.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server.
     * 
     * @throws  DNSServiceException        if unable to get the DNS server due to an exception.
     * @throws  ResourceNotFoundException  if the DNS server is not found.
     */
    @Override
    public DNSServer getDnsServer(final String dnsServerName) throws DNSServiceException, ResourceNotFoundException {

        DNSServer dnsServer;
            
        try {
            
            // Find the DNS server.
            dnsServer = this.dnsRepository.findDnsServer(dnsServerName);
            if (dnsServer == null) {
                throw new ResourceNotFoundException("TheDNS server (" + dnsServerName + ") is not found.");
            }
        } catch (DNSRepositoryException e) {
            throw new DNSServiceException("Unable to get the DNS server (" + dnsServerName + ").", e);
        }
    
        return dnsServer;
    }
    
    /**
     * Get the DNS server names.
     * 
     * @return  the DNS server names.
     * 
     * @throws  DNSServiceException  if unable to get the DNS server names due to an exception.
     */
    @Override
    public List<String> getDnsServerNames() throws DNSServiceException {

        List<String> dnsServerNames;
        
        try {
            
            // Find the DNS server names.
            dnsServerNames = this.dnsRepository.findDnsServerNames();
        } catch (DNSRepositoryException e) {
            throw new DNSServiceException("unable to get the DNS server names.", e);
        }
 
        return dnsServerNames;
    }

    /**
     * Get the records.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  zoneName       the zone name.
     * @param  recordType     the record type.
     * @param  recordName     the record name.
     * 
     * @return  the records.
     * 
     * @throws  DNSServiceException        if unable to get the records due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    @Override
    public List<Record> getRecords(final String dnsServerName, final String zoneName, final RecordType recordType, final String recordName) throws DNSServiceException, ResourceNotFoundException {
        
        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
    
        try {

            // Find the zone.
            Zone zone = this.dnsRepository.findZone(dnsServerName, zoneName);
            if (zone == null) {
                throw new ResourceNotFoundException("The zone (" + zoneName + ") for DNS server (" + dnsServerName + ") is not found.");
            }
            
            // Get the DNS record type.
            int dnsRecordType = RecordConverter.getDnsRecordType((recordType == null) ? RecordType.ANY : recordType);
            
            // Get the DNS records.
            List<org.xbill.DNS.Record> dnsRecords = createDomainNameServer(zone.getDnsServer(), zoneName).getRecords(dnsRecordType, recordName);

            // Initialize the record converter.
            RecordConverter recordConverter = RecordConverter.newInstance(zoneName);
            
            // Loop through the DNS records.
            for (org.xbill.DNS.Record dnsRecord : dnsRecords) {

                // Get the record from the DNS record.
                Record record = recordConverter.fromDnsRecord(dnsRecord);

                // Check if the record exists.
                if (record != null) {

                    // Add the record to the list.
                    records.add(record);
                }
            } 
        } catch (DNSRepositoryException e) {
            throw new DNSServiceException("Unable to get the records for the DNS server (" + dnsServerName + "), the zone (" + zoneName + "), the record type (" + recordType + "), and the record name (" + recordName + ").", e);
        } catch (DomainNameServerException e) {
            throw new DNSServiceException("Unable to get the records for the DNS server (" + dnsServerName + "), the zone (" + zoneName + "), the record type (" + recordType + "), and the record name (" + recordName + ").", e);
        } catch (RecordConverterException e) {
            throw new DNSServiceException("Unable to get the records for the DNS server (" + dnsServerName + "), the zone (" + zoneName + "), the record type (" + recordType + "), and the record name (" + recordName + ").", e);
        }
        
        return records;
    }

    /**
     * Get the zone.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  zoneName       the zone name.
     * 
     * @return  the zone.
     * 
     * @throws  DNSServiceException        if unable to get the zone due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    @Override
    public Zone getZone(final String dnsServerName, final String zoneName) throws DNSServiceException, ResourceNotFoundException {

        Zone zone;
        
        try {
            
            // Find the zone.
            zone = this.dnsRepository.findZone(dnsServerName, zoneName);
            if (zone == null) {
                throw new ResourceNotFoundException("The zone (" + zoneName + ") for DNS server (" + dnsServerName + ") is not found.");
            }
        } catch (DNSRepositoryException e) {
            throw new DNSServiceException("Unable to get the zone (" + zoneName + ") for the DNS server (" + dnsServerName + ").", e);
        }
        
        return zone;
    }
       
    /**
     * Get the zone names.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the zone names.
     * 
     * @throws  DNSServiceException        if unable to get the zone names due to an exception.
     * @throws  ResourceNotFoundException  if the DNS server is not found.
     */
    @Override
    public List<String> getZoneNames(final String dnsServerName) throws DNSServiceException, ResourceNotFoundException {

        List<String> zoneNames;
        
       try {
           
           // Find the DNS server.
            DNSServer dnsServer = this.dnsRepository.findDnsServer(dnsServerName);
            if (dnsServer == null) {
                throw new ResourceNotFoundException("TheDNS server (" + dnsServerName + ") is not found.");
            }
            
           // Find the zone names.
           zoneNames = this.dnsRepository.findZoneNames(dnsServerName);
       } catch (DNSRepositoryException e) {
           throw new DNSServiceException("Unable to get the zone names for the DNS server (" + dnsServerName + ").", e);
       }

        return zoneNames;
    }
    
    /**
     * Create a new instance of the DNS service class.
     * 
     * @return  a new instance of the DNS service class.
     * 
     * @throws DNSServiceException  if unable to create a new instance of the DNS service class due to an exception.
     */
    public static DNSService newInstance() throws DNSServiceException {
        return new DNSServiceImpl();
    }
    
    /**
     * Process the records.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  zoneName       the zone name.
     * @param  records        the records.
     * 
     * @return  true if the records are processed successfully, otherwise false.
     * 
     * @throws  DNSServiceException        if unable to process the records due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    @Override
    public boolean processRecords(final String dnsServerName, final String zoneName, final List<Record> records) throws DNSServiceException, ResourceNotFoundException {

        boolean success = false;

        try {

            // Find the zone.
            Zone zone = this.dnsRepository.findZone(dnsServerName, zoneName);
            if (zone == null) {
                throw new ResourceNotFoundException("The zone (" + zoneName + ") for DNS server (" + dnsServerName + ") is not found.");
            }
            
            // Initialize the record converter.
            RecordConverter recordConverter = RecordConverter.newInstance(zoneName);
           
            // Initialize the record operation map.
            Map<org.xbill.DNS.Record,String> recordOperationMap = new HashMap<org.xbill.DNS.Record,String>();
            
            // Loop through the records.
            for (Record record : records) {

                // Get the DNS record from the record.
                org.xbill.DNS.Record dnsRecord = recordConverter.toDnsRecord(record);
                
                // Check if the DNS record exists.
                if (dnsRecord != null) {
                   
                    // Add the record and operation to the map.
                    recordOperationMap.put(dnsRecord, record.getOperation().toString());
                }
            }

            // Process the records.
            success = createDomainNameServer(zone.getDnsServer(), zoneName).processRecords(recordOperationMap);
        } catch (DNSRepositoryException e) {
            throw new DNSServiceException("Unable to process the records for the DNS server (" + dnsServerName + ") and the zone (" + zoneName + ") due to an exception.", e);
        } catch (DomainNameServerException e) {
            throw new DNSServiceException("Unable to process the records for the DNS server (" + dnsServerName + ") and the zone (" + zoneName + ") due to an exception.", e);
        } catch (RecordConverterException e) {
            throw new DNSServiceException("Unable to process the records for the DNS server (" + dnsServerName + ") and the zone (" + zoneName + ") due to an exception.", e);
        }
        
        return success;
    }
}
