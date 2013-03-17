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
package org.lazydog.jdnsaas.internal.repository;

import java.sql.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.lazydog.jdnsaas.spi.repository.DNSRepository;
import org.lazydog.jdnsaas.spi.repository.model.DNSServerEntity;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;
import org.lazydog.repository.jpa.AbstractRepository;

/**
 * DNS repository implementation.
 * 
 * @author  Ron Rickard
 */
public class DNSRepositoryImpl extends AbstractRepository implements DNSRepository {

    /**
     * Hide the constructor.
     * 
     * @param  persistenceUnitName  the persistence unit name.
     */
    private DNSRepositoryImpl(String persistenceUnitName) {
        this.setEntityManager(Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager());
    }
    
    /**
     * Find the DNS server for the DNS server name.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server.
     */
    @Override
    public DNSServerEntity findDnsServer(final String dnsServerName) {
        
        // Set the DNS server criteria.
        Criteria<DNSServerEntity> criteria = this.getCriteria(DNSServerEntity.class);
        criteria.add(Comparison.eq("name", dnsServerName));
        
        return this.find(DNSServerEntity.class, criteria);
    }
    
    /**
     * Find the DNS servers.
     * 
     * @return  the DNS servers.
     */
    @Override
    public List<DNSServerEntity> findDnsServers() {
        return this.findList(DNSServerEntity.class);
    }
    
    /**
     * Get the connection.
     * 
     * @return  the connection.
     */
    @Override
    protected Connection getConnection() {
        return super.getConnection();
    }
    
    /**
     * Get the entity manager.
     * 
     * @return  the entity manager.
     */
    @Override
    protected EntityManager getEntityManager() {
        return super.getEntityManager();
    }

    /**
     * Create a new instance of the DNS repository class.
     * 
     * @param  persistenceUnitName  the persistence unit name.
     * 
     * @return  a new instance of the DNS repository class.
     */
    public static DNSRepository newInstance(String persistenceUnitName) {
        return new DNSRepositoryImpl(persistenceUnitName);
    }
}
