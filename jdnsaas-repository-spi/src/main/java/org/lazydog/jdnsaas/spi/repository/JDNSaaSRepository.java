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
     */
    List<Resolver> findResolvers();
    
    /**
     * Find the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     */
    List<TSIGKey> findTSIGKeys();
    
    /**
     * Find the view.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the view.
     */
    View findView(String viewName);
    
    /**
     * Find the view names.
     * 
     * @return  the view names.
     */
    List<String> findViewNames();
    
    /**
     * Find the zone.
     * 
     * @param  viewName  the view name.
     * @param  zoneName  the zone name.
     * 
     * @return  the zone.
     */
    Zone findZone(String viewName, String zoneName);

    /**
     * Find the zone names.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the zone names.
     */
    List<String> findZoneNames(String viewName);
    
    /**
     * Find the zones.
     * 
     * @return  the zones.
     */
    List<Zone> findZones();
}
