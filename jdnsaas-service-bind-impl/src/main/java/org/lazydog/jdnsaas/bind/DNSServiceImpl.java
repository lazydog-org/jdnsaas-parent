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
package org.lazydog.jdnsaas.bind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.lazydog.jdnsaas.DNSService;
import org.lazydog.jdnsaas.DNSServiceException;
import org.lazydog.jdnsaas.ResourceNotFoundException;
import org.lazydog.jdnsaas.bind.cache.ZoneCache;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.Resolver;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.View;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.model.ZoneType;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepository;
import org.lazydog.jdnsaas.spi.repository.JDNSaaSRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DNS service implementation.
 * 
 * @author  Ron Rickard
 */
@ApplicationScoped 
public class DNSServiceImpl implements DNSService {
    
    private static final Logger logger = LoggerFactory.getLogger(DNSServiceImpl.class);
    @Inject private JDNSaaSRepository jdnsaasRepository;
    @Inject private ZoneCache zoneCache;

    /**
     * Find the records.
     * 
     * @param  viewName    the view name.
     * @param  zoneName    the zone name.
     * @param  recordType  the record type.
     * @param  recordName  the record name.
     * 
     * @return  the records.
     * 
     * @throws  DNSServiceException        if unable to find the records due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    @Override
    public List<Record> findRecords(final String viewName, final String zoneName, final RecordType recordType, final String recordName) throws DNSServiceException, ResourceNotFoundException {
        
        // Initialize the records.
        List<Record> records = new ArrayList<Record>();
    
        try {

            // Find the zone.
            Zone zone = this.jdnsaasRepository.findZone(viewName, zoneName);
            if (zone == null) {
                throw new ResourceNotFoundException("The zone (" + zoneName + ") for the view (" + viewName + ") is not found.");
            }

            // Find the DNS records.
            records = DNSServerExecutor.newInstance(zone.getView().getResolvers(), zone.getQueryTSIGKey(), zone.getTransferTSIGKey(), null, zoneName).findRecords(recordType, recordName);
        } catch (JDNSaaSRepositoryException e) {
            throw new DNSServiceException("Unable to find the records for the view (" + viewName + "), the zone (" + zoneName + "), the record type (" + recordType + "), and the record name (" + recordName + ").", e);
        } catch (DNSServerExecutorException e) {
            throw new DNSServiceException("Unable to find the records for the view (" + viewName + "), the zone (" + zoneName + "), the record type (" + recordType + "), and the record name (" + recordName + ").", e);
        }
        
        return records;
    }
    
    /**
     * Find the resolvers.
     * 
     * @return  the resolvers.
     * 
     * @throws  DNSServiceException  if unable to find the resolvers due to an exception.
     */
    @Override
    public List<Resolver> findResolvers() throws DNSServiceException {
        
        List<Resolver> resolvers;
        
        try {
            
            // Find the resolvers.
            resolvers = this.jdnsaasRepository.findResolvers();
        } catch (JDNSaaSRepositoryException e) {
            throw new DNSServiceException("unable to find the resolvers.", e);
        }
 
        return resolvers;
    }
     
    /**
     * Find the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     * 
     * @throws  DNSServiceException  if unable to find the transaction signature (TSIG) keys due to an exception.
     */
    @Override
    public List<TSIGKey> findTSIGKeys() throws DNSServiceException {
        
        List<TSIGKey> tsigKeys;
        
        try {
            
            // Find the TSIG keys.
            tsigKeys = this.jdnsaasRepository.findTSIGKeys();
        } catch (JDNSaaSRepositoryException e) {
            throw new DNSServiceException("unable to find the TSIG keys.", e);
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
     * @throws  DNSServiceException        if unable to find the view due to an exception.
     * @throws  ResourceNotFoundException  if the view is not found.
     */
    @Override
    public View findView(final String viewName) throws DNSServiceException, ResourceNotFoundException {

        View view;
            
        try {
            
            // Find the view.
            view = this.jdnsaasRepository.findView(viewName);
            if (view == null) {
                throw new ResourceNotFoundException("The view (" + viewName + ") is not found.");
            }
        } catch (JDNSaaSRepositoryException e) {
            throw new DNSServiceException("Unable to find the view (" + viewName + ").", e);
        }
    
        return view;
    }
    
    /**
     * Find the view names.
     * 
     * @return  the view names.
     * 
     * @throws  DNSServiceException  if unable to find the view names due to an exception.
     */
    @Override
    public List<String> findViewNames() throws DNSServiceException {

        List<String> viewNames;
        
        try {
            
            // Find the view names.
            viewNames = this.jdnsaasRepository.findViewNames();
        } catch (JDNSaaSRepositoryException e) {
            throw new DNSServiceException("unable to find the view names.", e);
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
     * @throws  DNSServiceException        if unable to find the zone due to an exception.
     * @throws  ResourceNotFoundException  if the zone is not found.
     */
    @Override
    public Zone findZone(final String viewName, final String zoneName) throws DNSServiceException, ResourceNotFoundException {

        Zone zone;
        
        try {
            
            // Find the zone.
            zone = this.jdnsaasRepository.findZone(viewName, zoneName);
            if (zone == null) {
                throw new ResourceNotFoundException("The zone (" + zoneName + ") for the view (" + viewName + ") is not found.");
            }

            // Set some zone properties.
            zone.setType(ZoneUtility.newInstance(zoneName).isForwardZone() ? ZoneType.FORWARD : ZoneType.REVERSE);
            zone.setSupportedRecordTypes(Arrays.asList(RecordType.values(zone.getType())));
        } catch (JDNSaaSRepositoryException e) {
            throw new DNSServiceException("Unable to find the zone (" + zoneName + ") for the view (" + viewName + ").", e);
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
     * @throws  DNSServiceException        if unable to find the zone names due to an exception.
     * @throws  ResourceNotFoundException  if the view is not found.
     */
    @Override
    public List<String> findZoneNames(final String viewName) throws DNSServiceException, ResourceNotFoundException {

        List<String> zoneNames;
        
       try {
           
            // Find the view.
            View view = this.jdnsaasRepository.findView(viewName);
            if (view == null) {
                throw new ResourceNotFoundException("The view (" + viewName + ") is not found.");
            }
            
           // Find the zone names.
           zoneNames = this.jdnsaasRepository.findZoneNames(viewName);
       } catch (JDNSaaSRepositoryException e) {
           throw new DNSServiceException("Unable to find the zone names for the view (" + viewName + ").", e);
       }

        return zoneNames;
    }

    /**
     * Create a new instance of the DNS service class.
     * 
     * @return  a new instance of the DNS service class.
     * 
     * @throws DNSServiceException  if unable to create a new instance of the DNS service class due to an exception.
     */
    public static DNSService newInstance() throws DNSServiceException {
        return new DNSServiceImpl();
    }
    
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
    @Override
    public boolean processRecordOperations(final String viewName, final String zoneName, final List<Record> records) throws DNSServiceException, ResourceNotFoundException {

        boolean success = false;

        try {

            // Find the zone.
            Zone zone = this.jdnsaasRepository.findZone(viewName, zoneName);
            if (zone == null) {
                throw new ResourceNotFoundException("The zone (" + zoneName + ") for view (" + viewName + ") is not found.");
            }

            // Process the records.
            success = DNSServerExecutor.newInstance(zone.getView().getResolvers(), null, null, zone.getUpdateTSIGKey(), zoneName).processRecordOperations(records);
        } catch (JDNSaaSRepositoryException e) {
            throw new DNSServiceException("Unable to process the record operations for the view (" + viewName + ") and the zone (" + zoneName + ") due to an exception.", e);
        } catch (DNSServerExecutorException e) {
            throw new DNSServiceException("Unable to process the record operations for the view (" + viewName + ") and the zone (" + zoneName + ") due to an exception.", e);
        }
        
        return success;
    }
}
