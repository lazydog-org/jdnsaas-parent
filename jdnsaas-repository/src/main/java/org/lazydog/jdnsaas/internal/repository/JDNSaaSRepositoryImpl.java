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
import org.lazydog.jdnsaas.model.DNSServer;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.View;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepository;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepositoryException;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;
import org.lazydog.repository.criterion.Logical;
import org.lazydog.repository.jpa.AbstractRepository;

/**
 * Java DNS as a Service (JDNSaaS) repository.
 * 
 * @author  Ron Rickard
 */
public class JDNSaaSRepositoryImpl extends AbstractRepository implements JDNSaaSRepository {

    /**
     * Hide the constructor.
     * 
     * @param  persistenceUnitName  the persistence unit name.
     * 
     * @throws JDNSaaSRepositoryException  if unable to initialize the JDNSaaS repository due to an exception.
     */
    private JDNSaaSRepositoryImpl(final String persistenceUnitName) throws JDNSaaSRepositoryException {
        
        try {
            
            // Set the entity manager.
            this.setEntityManager(Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager());
        } catch (Exception e) {
            throw new JDNSaaSRepositoryException("Unable to initialize the JDNSaaS repository with persistence unit " + persistenceUnitName + ".", e);
        }
    }
    /**
     * Find the DNS servers.
     * 
     * @return  the DNS servers.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the DNS servers due to an exception.
     */
    @Override
    public List<DNSServer> findDNSServers() throws JDNSaaSRepositoryException {
        
        List<DNSServer> dnsServers = new ArrayList<DNSServer>();
        
        try {
            
            // Find the DNS servers.
            dnsServers = this.findList(DNSServer.class);
        } catch (Exception e) {
            throw new JDNSaaSRepositoryException("Unable to find the DNS servers.", e);
        }
        
        return dnsServers;
    }
    
    /**
     * Find the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the transaction signature (TSIG) keys due to an exception.
     */
    @Override
    public List<TSIGKey> findTSIGKeys() throws JDNSaaSRepositoryException {
        
        List<TSIGKey> tsigKeys = new ArrayList<TSIGKey>();
        
        try {
            
            // Find the transaction signature (TSIG) keys.
            tsigKeys = this.findList(TSIGKey.class);
        } catch (Exception e) {
            throw new JDNSaaSRepositoryException("Unable to find the TSIG keys.", e);
        }
        
        return tsigKeys;
    }
    
    /**
     * Find the view.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the view.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the view due to an exception.
     */
    @Override
    public View findView(final String viewName) throws JDNSaaSRepositoryException {
        
        View view;
        
        try {
            
            // Set the DNS server criteria.
            Criteria<View> criteria = this.getCriteria(View.class);
            criteria.add(Comparison.eq("name", viewName));
            
            // Find the view.
            view = this.find(View.class, criteria);
        } catch (Exception e) {
            throw new JDNSaaSRepositoryException("Unable to find the view " + viewName + ".", e);
        }
        
        return view;
    }
    
    /**
     * Find the view names.
     * 
     * @return  the view names.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the view names due to an exception.
     */
    @Override
    public List<String> findViewNames() throws JDNSaaSRepositoryException {

        List<String> viewNames = new ArrayList<String>();
        
        try {
            
            // Loop through the views.
            for (View view : this.findList(View.class)) {

                // Add the view name to the list.
                viewNames.add(view.getName());
            }
        } catch (Exception e) {
            throw new JDNSaaSRepositoryException("Unable to find the view names.", e);
        }
        
        return viewNames;
    }
    
    /**
     * Find the zone.
     * 
     * @param  viewName  the view name.
     * @param  zoneName  the zone name.
     * 
     * @return  the zone.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the zone due to an exception.
     */
    @Override
    public Zone findZone(final String viewName, final String zoneName) throws JDNSaaSRepositoryException {

        Zone zone;
        
        try {
            
            // Set the zone criteria.
            Criteria<Zone> criteria = this.getCriteria(Zone.class);
            criteria.add(Comparison.eq("name", zoneName));
            criteria.add(Logical.and(Comparison.eq("view.name", viewName)));
            
            // Find the zone.
            zone = this.find(Zone.class, criteria);
        } catch (Exception e) {
            throw new JDNSaaSRepositoryException("Unable to find the zone " + zoneName + " for the view " + viewName + ".", e);
        }
        
        return zone;
    }
    
    /**
     * Find the zone names.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the zone names.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the zone names due to an exception.
     */
    @Override
    public List<String> findZoneNames(final String viewName) throws JDNSaaSRepositoryException {

        List<String> zoneNames = new ArrayList<String>();
        
        try {
            
            // Loop through the zones.
            for (Zone zone : this.findList(Zone.class)) {

                // Check if the zone's view name is the desire view name.
                if (zone.getView().getName().equals(viewName)) {
                    
                    // Add the zone name to the list.
                    zoneNames.add(zone.getName());
                }
            }
        } catch (Exception e) {
            throw new JDNSaaSRepositoryException("Unable to find the zone names for the view " + viewName + ".", e);
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
     * @throws JDNSaaSRepositoryException  if unable to create a new instance of the JDNSaaS repository class due to an exception.
     */
    public static JDNSaaSRepository newInstance(final String persistenceUnitName) throws JDNSaaSRepositoryException {
        return new JDNSaaSRepositoryImpl(persistenceUnitName);
    }
}
