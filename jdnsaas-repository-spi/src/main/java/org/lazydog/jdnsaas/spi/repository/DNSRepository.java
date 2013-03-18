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
package org.lazydog.jdnsaas.spi.repository;

import java.util.List;
import org.lazydog.jdnsaas.spi.repository.model.DNSServerEntity;
import org.lazydog.jdnsaas.spi.repository.model.ZoneEntity;
import org.lazydog.repository.Repository;

/**
 * DNS repository.
 * 
 * @author  Ron Rickard
 */
public interface DNSRepository extends Repository {

    /**
     * Find the DNS server.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server.
     * 
     * @throws  DNSRepositoryException  if unable to find the DNS server due to an exception.
     */
    DNSServerEntity findDnsServer(String dnsServerName) throws DNSRepositoryException;
    
    /**
     * Find the DNS server names.
     * 
     * @return  the DNS server names.
     * 
     * @throws  DNSRepositoryException  if unable to find the DNS server names due to an exception.
     */
    List<String> findDnsServerNames() throws DNSRepositoryException;
    
    /**
     * Find the zone.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  zoneName       the zone name.
     * 
     * @return  the zone.
     * 
     * @throws  DNSRepositoryException  if unable to find the zone due to an exception.
     */
    ZoneEntity findZone(String dnsServerName, String zoneName) throws DNSRepositoryException;
    
    /**
     * Find the zone names.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the zone names.
     * 
     * @throws  DNSRepositoryException  if unable to find the zone names due to an exception.
     */
    List<String> findZoneNames(String dnsServerName) throws DNSRepositoryException;
}
