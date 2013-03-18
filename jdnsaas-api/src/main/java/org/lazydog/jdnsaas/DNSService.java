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
     * @throws  DNSServiceException        if unable to get the DNS server due to an exception.
     * @throws  ResourceNotFoundException  if the DNS server is not found.
     */
    DNSServer getDnsServer(String dnsServerName) throws DNSServiceException, ResourceNotFoundException;
    
    /**
     * Get the DNS server names.
     * 
     * @return  the DNS server names.
     */
    List<String> getDnsServerNames() throws DNSServiceException;

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
    List<Record> getRecords(String dnsServerName, String zoneName, RecordType recordType, String recordName) throws DNSServiceException, ResourceNotFoundException;
    
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
    Zone getZone(String dnsServerName, String zoneName) throws DNSServiceException, ResourceNotFoundException;
        
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
    List<String> getZoneNames(String dnsServerName) throws DNSServiceException, ResourceNotFoundException;
        
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
    boolean processRecords(String dnsServerName, String zoneName, List<Record> records) throws DNSServiceException, ResourceNotFoundException;
}