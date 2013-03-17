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
import org.lazydog.repository.Repository;

/**
 * DNS repository.
 * 
 * @author  Ron Rickard
 */
public interface DNSRepository extends Repository {

    /**
     * Find the DNS server for the DNS server name.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server.
     */
    DNSServerEntity findDnsServer(String dnsServerName);
    
    /**
     * Find the DNS servers.
     * 
     * @return  the DNS servers.
     */
    List<DNSServerEntity> findDnsServers();
}
