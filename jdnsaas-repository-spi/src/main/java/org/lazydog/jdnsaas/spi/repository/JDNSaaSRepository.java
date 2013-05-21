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
import org.lazydog.jdnsaas.model.Resolver;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.View;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.repository.Repository;

/**
 * Java DNS as a Service (JDNSaaS) repository.
 * 
 * @author  Ron Rickard
 */
public interface JDNSaaSRepository extends Repository {

    /**
     * Find the resolvers.
     * 
     * @return  the resolvers.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the resolvers due to an exception.
     */
    List<Resolver> findResolvers() throws JDNSaaSRepositoryException;
    
    /**
     * Find the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the transaction signature (TSIG) keys due to an exception.
     */
    List<TSIGKey> findTSIGKeys() throws JDNSaaSRepositoryException;
    
    /**
     * Find the view.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the view.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the view due to an exception.
     */
    View findView(String viewName) throws JDNSaaSRepositoryException;
    
    /**
     * Find the view names.
     * 
     * @return  the view names.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the view names due to an exception.
     */
    List<String> findViewNames() throws JDNSaaSRepositoryException;
    
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
    Zone findZone(String viewName, String zoneName) throws JDNSaaSRepositoryException;
    
    /**
     * Find the zone names.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the zone names.
     * 
     * @throws  JDNSaaSRepositoryException  if unable to find the zone names due to an exception.
     */
    List<String> findZoneNames(String viewName) throws JDNSaaSRepositoryException;
}
