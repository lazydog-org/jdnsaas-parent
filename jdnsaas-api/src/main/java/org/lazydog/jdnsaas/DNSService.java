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
package org.lazydog.jdnsaas;

import java.util.List;
import org.lazydog.jdnsaas.model.DNSServer;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.Zone;

/**
 * DNS service.
 * 
 * @author  Ron Rickard
 */
public interface DNSService {

    /**
     * Get the DNS server.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server.
     * 
     * @throws  ResourceNotFoundException  if the DNS server cannot be found.
     */
    DNSServer getDnsServer(String dnsServerName) throws ResourceNotFoundException;
    
    /**
     * Get the DNS servers.
     * 
     * @return  the DNS servers.
     */
    List<DNSServer> getDnsServers();

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
    List<Record> getRecords(String dnsServerName, String zoneName, RecordType recordType, String recordName) throws DNSServiceException, ResourceNotFoundException;
    
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
    Zone getZone(String dnsServerName, String zoneName) throws DNSServiceException, ResourceNotFoundException;
        
    /**
     * Get the zones.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the zones.
     * 
     * @throws  ResourceNotFoundException  if the DNS server cannot be found.
     */
    List<Zone> getZones(String dnsServerName) throws ResourceNotFoundException;
        
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
    boolean processRecords(String dnsServerName, String zoneName, List<Record> records) throws DNSServiceException, ResourceNotFoundException;
}