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
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.lazydog.jdnsaas.model.Resolver;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.View;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepository;
import org.lazydog.jdnsaas.spi.repository.PersistenceUnitName;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;
import org.lazydog.repository.criterion.Logical;
import org.lazydog.repository.jpa.AbstractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java DNS as a Service (JDNSaaS) repository.
 * 
 * @author  Ron Rickard
 */
@ApplicationScoped
public class JDNSaaSRepositoryImpl extends AbstractRepository implements JDNSaaSRepository {

    private static final Logger logger = LoggerFactory.getLogger(JDNSaaSRepositoryImpl.class);
    private String persistenceUnitName;

    /**
     * Find the resolvers.
     * 
     * @return  the resolvers.
     */
    @Override
    public List<Resolver> findResolvers() {
        return this.findList(Resolver.class);
    }
    
    /**
     * Find the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     */
    @Override
    public List<TSIGKey> findTSIGKeys() {     
        return this.findList(TSIGKey.class);
    }
    
    /**
     * Find the view.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the view.
     */
    @Override
    public View findView(final String viewName) {

        // Set the view criteria.
        Criteria<View> criteria = this.getCriteria(View.class);
        criteria.add(Comparison.eq("name", viewName));

        // Find the view.
        return this.find(View.class, criteria);
    }
    
    /**
     * Find the view names.
     * 
     * @return  the view names.
     */
    @Override
    public List<String> findViewNames() {

        List<String> viewNames = new ArrayList<String>();

        // Loop through the views.
        for (View view : this.findList(View.class)) {

            // Add the view name to the list.
            viewNames.add(view.getName());
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
     */
    @Override
    public Zone findZone(final String viewName, final String zoneName) {

        // Set the zone criteria.
        Criteria<Zone> criteria = this.getCriteria(Zone.class);
        criteria.add(Comparison.eq("name", zoneName));
        criteria.add(Logical.and(Comparison.eq("view.name", viewName)));

        return this.find(Zone.class, criteria);
    }
    
    /**
     * Find the zone names.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the zone names.
     */
    @Override
    public List<String> findZoneNames(final String viewName) {

        List<String> zoneNames = new ArrayList<String>();

        // Loop through the zones.
        for (Zone zone : this.findList(Zone.class)) {

            // Check if the zone's view name is the desire view name.
            if (zone.getView().getName().equals(viewName)) {

                // Add the zone name to the list.
                zoneNames.add(zone.getName());
            }
        }
        
        return zoneNames;
    }
        
    /**
     * Find the zones.
     * 
     * @return  the zones.
     */
    @Override
    public List<Zone> findZones() {        
        return this.findList(Zone.class);
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
     * Set the persistence unit name.
     * 
     * @param  persistenceUnitName  the persistence unit name.
     */
    @Inject 
    public void setPersistenceUnitName(@PersistenceUnitName final String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
        logger.info("Set the persistence unit name to {}.", persistenceUnitName);
    }
    
    /**
     * Startup the JDNSaaS repository.
     */
    @PostConstruct
    public void startup() {
        
        logger.info("Startup the JDNSaaS repository.");

        // Set the entity manager.
        this.setEntityManager(Persistence.createEntityManagerFactory(this.persistenceUnitName).createEntityManager());
    }
}
