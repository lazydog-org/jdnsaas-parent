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
import org.lazydog.jdnsaas.spi.repository.model.DNSServerEntity;
import org.lazydog.jdnsaas.spi.repository.model.ZoneEntity;

/**
 * DNS service implementation.
 * 
 * @author  Ron Rickard
 */
public class DNSServiceImpl implements DNSService {
    
    private DNSRepository dnsRepository;

    /**
     * Hide the constructor.
     */
    private DNSServiceImpl() {
        this.dnsRepository = DNSRepositoryImpl.newInstance("jdnsaas");
    }
    
    /**
     * Create the DNS server.
     * 
     * @param  dnsServerEntity  the DNS server entity.
     * @param  includeZones     true to include the zones, otherwise false.
     * 
     * @return  the created DNS server.
     */
    private static DNSServer createDnsServer(final DNSServerEntity dnsServerEntity, boolean includeZones) {

        // Create the DNS server.
        DNSServer dnsServer = new DNSServer();
        dnsServer.setName(dnsServerEntity.getName());
        dnsServer.setPort(dnsServerEntity.getPort());
        
        // Check if the zones will be included.
        if (includeZones) {
            dnsServer.setZones(createZones(dnsServerEntity.getZones(), false));
        }
        
        return dnsServer;
    }
  
    /**
     * Create the domain name server.
     * 
     * @param  dnsServerEntity  the DNS server entity.
     * @param  zoneName         the zone name.
     * 
     * @return  the domain name server.
     */
    private static DomainNameServer createDomainNameServer(final DNSServerEntity dnsServerEntity, final String zoneName) {
        return DomainNameServer.newInstance(dnsServerEntity.getName(), dnsServerEntity.getPort(), dnsServerEntity.getTransactionSignature().getAlgorithm().getName(), dnsServerEntity.getTransactionSignature().getName(), dnsServerEntity.getTransactionSignature().getSecret(), zoneName);
    }
    
    /**
     * Create the zone.
     * 
     * @param  zoneEntity                   the zone entity.
     * @param  includeSupportedRecordTypes  true to include the supported record types, otherwise false.
     * 
     * @return  the created zone.
     */
    private static Zone createZone(final ZoneEntity zoneEntity, final boolean includeSupportedRecordTypes) {

        // Create the zone.
        Zone zone = new Zone();
        zone.setName(zoneEntity.getName());
        
        // Check if the supported record types will be included.
        if (includeSupportedRecordTypes) {
            zone.setSupportedRecordTypes(ZoneResolver.newInstance(zoneEntity.getName()).isForwardZone() ? RecordType.getForwardTypes() : RecordType.getReverseTypes());
        }
        
        return zone;
    }
    
    /**
     * Create the zones.
     * 
     * @param  zoneEntities                 the zone entities.
     * @param  includeSupportedRecordTypes  true to include the supported record types, otherwise false.
     * 
     * @return  the created zones.
     */
    private static List<Zone> createZones(final List<ZoneEntity> zoneEntities, final boolean includeSupportedRecordTypes) {
        
        // Create the zones.
        List<Zone> zones = new ArrayList<Zone>();
        
        // Loop through the zone entities.
        for (ZoneEntity zoneEntity : zoneEntities) {
            
            // Add the zone to the list.
            zones.add(createZone(zoneEntity, includeSupportedRecordTypes));
        }
        
        return zones;
    }
    
    /**
     * Find the DNS server entity.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server entity.
     * 
     * @throws  ResourceNotFoundException  if unable to find the DNS server entity.
     */
    private DNSServerEntity findDnsServerEntity(final String dnsServerName) throws ResourceNotFoundException {
        
        // Find the DNS server entity.
        DNSServerEntity dnsServerEntity = this.dnsRepository.findDnsServer(dnsServerName);

        // Check if the DNS server entity was found.
        if (dnsServerEntity == null) {
            throw new ResourceNotFoundException("The DNS server, " + dnsServerName + ", cannot be found.", dnsServerName);
        }
        
        return dnsServerEntity;
    }
    
    /**
     * Get the DNS server.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server.
     * 
     *  @throws  ResourceNotFoundException  if the DNS server cannot be found.
     */
    @Override
    public DNSServer getDnsServer(final String dnsServerName) throws ResourceNotFoundException {
        return createDnsServer(this.findDnsServerEntity(dnsServerName), true);
    }
    
    /**
     * Get the DNS servers.
     * 
     * @return  the DNS servers.
     */
    @Override
    public List<DNSServer> getDnsServers() {
        
        // Initialize the DNS servers.
        List<DNSServer> dnsServers = new ArrayList<DNSServer>();
        
        // Loop through the DNS server entities.
        for (DNSServerEntity dnsServerEntity : this.dnsRepository.findDnsServers()) {
            
            // Add the DNS server to the list.
            dnsServers.add(createDnsServer(dnsServerEntity, false));
        }
        
        return dnsServers;
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
     * @throws  DNSServiceException        if unable to get the records.
     * @throws  ResourceNotFoundException  if the DNS server cannot be found.
     */
    @Override
    public List<Record> getRecords(final String dnsServerName, final String zoneName, final RecordType recordType, final String recordName) throws DNSServiceException, ResourceNotFoundException {
        
        // Initialize the records.
        List<Record> records = new ArrayList<Record>();

        // Find the DNS server entity.
        DNSServerEntity dnsServerEntity = this.findDnsServerEntity(dnsServerName);
            
        try {

            // Get the DNS record type.
            int dnsRecordType = RecordConverter.getDnsRecordType((recordType == null) ? RecordType.ANY : recordType);
            
            // Get the DNS records.
            List<org.xbill.DNS.Record> dnsRecords = createDomainNameServer(dnsServerEntity, zoneName).getRecords(dnsRecordType, recordName);

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
        } catch (Exception e) {
            throw new DNSServiceException("Unable to get the records for record type, " + recordType + ", and record name, " + recordName + ".", e);
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
     * @throws  DNSServiceException        if unable to get the records for the zone.
     * @throws  ResourceNotFoundException  if the DNS server or zone cannot be found.
     */
    @Override
    public Zone getZone(final String dnsServerName, final String zoneName) throws DNSServiceException, ResourceNotFoundException {
       
        Zone zone = null;
        
        // Loop through the DNS server zone entities.
        for (ZoneEntity zoneEntity : this.findDnsServerEntity(dnsServerName).getZones()) {
            
            // Check if the zone entity is the desired zone.
            if (zoneEntity.getName().equals(zoneName)) {
                
                // Get the zone.
                zone = createZone(zoneEntity, true);
                zone.setRecords(this.getRecords(dnsServerName, zoneName, RecordType.ANY, null));
                break;
            }
        }

        // Check if the zone was found.
        if (zone == null) {
            throw new ResourceNotFoundException("The zone, " + zoneName + ", cannot be found.", zoneName);
        }
        
        return zone;
    }
       
    /**
     * Get the zones.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the zones.
     * 
     * @throws  ResourceNotFoundException  if the DNS server cannot be found.
     */
    @Override
    public List<Zone> getZones(final String dnsServerName) throws ResourceNotFoundException {
        return createZones(this.findDnsServerEntity(dnsServerName).getZones(), true);
    }
    
    /**
     * Create a new instance of the DNS service class.
     * 
     * @return  a new instance of the DNS service class.
     */
    public static DNSService newInstance() {
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
     * @throws  ResourceNotFoundException  if the DNS server cannot be found.
     */
    @Override
    public boolean processRecords(final String dnsServerName, final String zoneName, final List<Record> records) throws DNSServiceException, ResourceNotFoundException {

        // Initialize the success.
        boolean success = false;
           
        // Find the DNS server entity.
        DNSServerEntity dnsServerEntity = this.findDnsServerEntity(dnsServerName);

        try {

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
            success = createDomainNameServer(dnsServerEntity, zoneName).processRecords(recordOperationMap);
        } catch (Exception e) {
            throw new DNSServiceException("Unable to process the records due to an exception.", e);
        }
        
        return success;
    }
}
