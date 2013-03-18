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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.lazydog.jdnsaas.spi.repository.DNSRepository;
import org.lazydog.jdnsaas.spi.repository.DNSRepositoryException;
import org.lazydog.jdnsaas.spi.repository.model.DNSServerEntity;
import org.lazydog.jdnsaas.spi.repository.model.ZoneEntity;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;
import org.lazydog.repository.criterion.Logical;
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
     * 
     * @throws DNSRepositoryException  if unable to initialize the DNS repository due to an exception.
     */
    private DNSRepositoryImpl(String persistenceUnitName) throws DNSRepositoryException {
        
        try {
            
            // Set the entity manager.
            this.setEntityManager(Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager());
        } catch (Exception e) {
            throw new DNSRepositoryException("Unable to initialize the DNS repository with persistence unit " + persistenceUnitName + ".", e);
        }
    }
    
    /**
     * Find the DNS server.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the DNS server.
     * 
     * @throws  DNSRepositoryException  if unable to find the DNS server due to an exception.
     */
    @Override
    public DNSServerEntity findDnsServer(final String dnsServerName) throws DNSRepositoryException {
        
        DNSServerEntity dnsServerEntity;
        
        try {
            
            // Set the DNS server entity criteria.
            Criteria<DNSServerEntity> criteria = this.getCriteria(DNSServerEntity.class);
            criteria.add(Comparison.eq("name", dnsServerName));
            
            // Find the DNS server entity.
            dnsServerEntity = this.find(DNSServerEntity.class, criteria);
        } catch (Exception e) {
            throw new DNSRepositoryException("Unable to find the DNS server " + dnsServerName + ".", e);
        }
        
        return dnsServerEntity;
    }
    
    /**
     * Find the DNS server names.
     * 
     * @return  the DNS server names.
     * 
     * @throws  DNSRepositoryException  if unable to find the DNS server names due to an exception.
     */
    @Override
    public List<String> findDnsServerNames() throws DNSRepositoryException {

        List<String> dnsServerNames = new ArrayList<String>();
        
        try {
            
            // Loop through the DNS server entities.
            for (DNSServerEntity dnsServerEntity : this.findList(DNSServerEntity.class)) {

                // Add the DNS server name to the list.
                dnsServerNames.add(dnsServerEntity.getName());
            }
        } catch (Exception e) {
            throw new DNSRepositoryException("Unable to find the DNS server names.", e);
        }
        
        return dnsServerNames;
    }
    
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
    @Override
    public ZoneEntity findZone(final String dnsServerName, final String zoneName) throws DNSRepositoryException {

        ZoneEntity zoneEntity;
        
        try {
            
            // Set the zone entity criteria.
            Criteria<ZoneEntity> criteria = this.getCriteria(ZoneEntity.class);
            criteria.add(Comparison.eq("name", zoneName));
            criteria.add(Logical.and(Comparison.eq("dnsServerEntity.name", dnsServerName)));
            
            // Find the zone entity.
            zoneEntity = this.find(ZoneEntity.class, criteria);
        } catch (Exception e) {
            throw new DNSRepositoryException("Unable to find the zone " + zoneName + " for DNS server " + dnsServerName + ".", e);
        }
        
        return zoneEntity;
    }
    
    /**
     * Find the zone names.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the zone names.
     * 
     * @throws  DNSRepositoryException  if unable to find the zone names due to an exception.
     */
    @Override
    public List<String> findZoneNames(final String dnsServerName) throws DNSRepositoryException {

        List<String> zoneNames = new ArrayList<String>();
        
        try {
            
            // Loop through the zone entities.
            for (ZoneEntity zoneEntity : this.findList(ZoneEntity.class)) {

                // Check if the DNS server entity name is the desire DNS server name.
                if (zoneEntity.getDnsServerEntity().getName().equals(dnsServerName)) {
                    
                    // Add the zone name to the list.
                    zoneNames.add(zoneEntity.getName());
                }
            }
        } catch (Exception e) {
            throw new DNSRepositoryException("Unable to find the zone names for DNS server " + dnsServerName + ".", e);
        }
        
        return zoneNames;
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
     * 
     * @throws DNSRepositoryException  if unable to create a new instance of the DNS repository class due to an exception.
     */
    public static DNSRepository newInstance(String persistenceUnitName) throws DNSRepositoryException {
        return new DNSRepositoryImpl(persistenceUnitName);
    }
}
