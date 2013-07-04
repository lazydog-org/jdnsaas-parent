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
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.Resolver;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.View;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.utility.RecordFilter;

/**
 * DNS service.
 * 
 * @author  Ron Rickard
 */
public interface DNSService {

    /**
     * Find the records.
     * 
     * @param  viewName      the view name.
     * @param  zoneName      the zone name.
     * @param  recordFilter  the record filter.
     * @param  useCache      true if the zone cache should be used, otherwise false. 
     * 
     * @return  the records.
     * 
     * @throws  DNSServiceException        if unable to find the records due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    List<Record> findRecords(String viewName, String zoneName, RecordFilter recordFilter, boolean useCache) throws DNSServiceException, ResourceNotFoundException;
    
    /**
     * Find the resolvers.
     * 
     * @return  the resolvers.
     * 
     * @throws  DNSServiceException  if unable to find the resolvers due to an exception.
     */
    List<Resolver> findResolvers() throws DNSServiceException;
    
    /**
     * Find the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     * 
     * @throws  DNSServiceException  if unable to find the transaction signature (TSIG) keys due to an exception.
     */
    List<TSIGKey> findTSIGKeys() throws DNSServiceException;
    
    /**
     * Find the view.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the view.
     * 
     * @throws  DNSServiceException        if unable to find the view due to an exception.
     * @throws  ResourceNotFoundException  if the view is not found.
     */
    View findView(String viewName) throws DNSServiceException, ResourceNotFoundException;
    
    /**
     * Find the view names.
     * 
     * @return  the view names.
     * 
     * @throws  DNSServiceException  if unable to find the view names due to an exception.
     */
    List<String> findViewNames() throws DNSServiceException;

    /**
     * Find the zone.
     * 
     * @param  viewName  the view name.
     * @param  zoneName  the zone name.
     * 
     * @return  the zone.
     * 
     * @throws  DNSServiceException        if unable to find the zone due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    Zone findZone(String viewName, String zoneName) throws DNSServiceException, ResourceNotFoundException;
        
    /**
     * Find the zone names.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the zone names.
     * 
     * @throws  DNSServiceException        if unable to find the zone names due to an exception.
     * @throws  ResourceNotFoundException  if the view is not found.
     */
    List<String> findZoneNames(String viewName) throws DNSServiceException, ResourceNotFoundException;
        
    /**
     * Process the record operations.
     * 
     * @param  viewName  the view name.
     * @param  zoneName  the zone name.
     * @param  records   the records.
     * 
     * @return  true if the record operations are processed successfully, otherwise false.
     * 
     * @throws  DNSServiceException        if unable to process the record operations due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    boolean processRecordOperations(String viewName, String zoneName, List<Record> records) throws DNSServiceException, ResourceNotFoundException;
}