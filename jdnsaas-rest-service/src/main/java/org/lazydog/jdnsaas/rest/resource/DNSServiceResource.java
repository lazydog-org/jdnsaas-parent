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
package org.lazydog.jdnsaas.rest.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.lazydog.jdnsaas.DNSService;
import org.lazydog.jdnsaas.DNSServiceException;
import org.lazydog.jdnsaas.ResourceNotFoundException;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordOperation;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.Resolver;
import org.lazydog.jdnsaas.model.TSIGKey;
import org.lazydog.jdnsaas.model.TSIGKeyAlgorithm;
import org.lazydog.jdnsaas.model.View;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.model.ZoneType;
import org.lazydog.jdnsaas.rest.model.RecordOperationsWrapper;
import org.lazydog.jdnsaas.rest.model.RecordTypesWrapper;
import org.lazydog.jdnsaas.rest.model.RecordsWrapper;
import org.lazydog.jdnsaas.rest.model.ResolversWrapper;
import org.lazydog.jdnsaas.rest.model.TSIGKeyAlgorithmsWrapper;
import org.lazydog.jdnsaas.rest.model.TSIGKeysWrapper;
import org.lazydog.jdnsaas.rest.model.ViewWrapper;
import org.lazydog.jdnsaas.rest.model.ViewsWrapper;
import org.lazydog.jdnsaas.rest.model.ZoneTypesWrapper;
import org.lazydog.jdnsaas.rest.model.ZoneWrapper;
import org.lazydog.jdnsaas.rest.model.ZonesWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DNS service resource.
 * 
 * @author  Ron Rickard
 */
@Path("dns")
@RequestScoped
public class DNSServiceResource extends AbstractResource {
      
    private static Logger logger = LoggerFactory.getLogger(DNSServiceResource.class);
    @Inject private DNSService dnsService;

    /**
     * Create the view wrappers.
     * 
     * @param  viewNames  the view names.
     * 
     * @return  the view wrappers.
     */
    private List<ViewWrapper> createViewWrappers(final List<String> viewNames) {
        
        List<ViewWrapper> viewWrappers = new ArrayList<ViewWrapper>();
        
        for (String viewName : viewNames) {
            viewWrappers.add(ViewWrapper.newInstance(viewName, this.getNextUri(viewName)));
        }
        
        return viewWrappers;
    }
    
    /**
     * Create the zone wrappers.
     * 
     * @param  zoneNames  the zone names.
     * 
     * @return  the zone wrappers.
     */
    private List<ZoneWrapper> createZoneWrappers(final List<String> zoneNames) {
        
        List<ZoneWrapper> zoneWrappers = new ArrayList<ZoneWrapper>();
        
        for (String zoneName : zoneNames) {
            zoneWrappers.add(ZoneWrapper.newInstance(zoneName, this.getNextUri(zoneName)));
        }
        
        return zoneWrappers;
    }

    /**
     * Find the record operations.
     * 
     * @return  the record operations.
     */
    @GET
    @Path("recordoperations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRecordOperations() {
        return buildOkResponse(RecordOperationsWrapper.newInstance(Arrays.asList(RecordOperation.values())));       
    }
     
    /**
     * Find the record types.
     * 
     * @return  the record types.
     */
    @GET
    @Path("recordtypes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRecordTypes(@DefaultValue("both") @QueryParam("zoneType") final String zoneType) {
        return buildOkResponse(RecordTypesWrapper.newInstance(Arrays.asList(RecordType.values(ZoneType.fromString(zoneType)))));       
    }
    
    /**
     * Find the records for the view name and zone name.
     * Optionally, filter the records by the record type and/or record name.
     * 
     * @param  viewName    the view name.
     * @param  zoneName    the zone name.
     * @param  recordType  the record type.
     * @param  recordName  the record name.
     * 
     * @return  the records.
     */
    @GET
    @Path("views/{viewName}/zones/{zoneName}/records")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRecords(@PathParam("viewName") final String viewName, @PathParam("zoneName") final String zoneName, @DefaultValue("any") @QueryParam("recordType") final String recordType, @QueryParam("recordName") final String recordName) {
        
        Response response;
        
        try {
            List<Record> records = this.dnsService.findRecords(viewName, zoneName, RecordType.fromString(recordType), recordName);
            response = buildOkResponse(RecordsWrapper.newInstance(records)); 
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse(e.getMessage(), null);
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), null);
e.printStackTrace();
        }
        
        return response;
    }

    /**
     * Find the resolvers.
     * 
     * @return  the resolvers.
     */
    @GET
    @Path("resolvers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findResolvers() {
        
        Response response;
        
        try {
            List<Resolver> resolvers = this.dnsService.findResolvers();
            return buildOkResponse(ResolversWrapper.newInstance(resolvers));
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), null);
e.printStackTrace();
        }
        
        return response;
    }

    /**
     * Find the transaction signature (TSIG) key algorithms.
     * 
     * @return  the transaction signature (TSIG) key algorithms.
     */
    @GET
    @Path("tsigkeyalgorithms")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTSIGKeyAlgorithms() {
        return buildOkResponse(TSIGKeyAlgorithmsWrapper.newInstance(Arrays.asList(TSIGKeyAlgorithm.values())));       
    }
    
    /**
     * Find the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     */
    @GET
    @Path("tsigkeys")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTSIGKeys() {
        
        Response response;
        
        try {
            List<TSIGKey> tsigKeys = this.dnsService.findTSIGKeys();
            return buildOkResponse(TSIGKeysWrapper.newInstance(tsigKeys));
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), null);
e.printStackTrace();
        }
        
        return response;
    }

    /**
     * Find the view.
     * 
     * @param  viewName  the view.
     * 
     * @return  the view.
     */
    @GET
    @Path("views/{viewName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findView(@PathParam("viewName") final String viewName) {
        
        Response response;
        
        try {
            View view = this.dnsService.findView(viewName);
            response = buildOkResponse(ViewWrapper.newInstance(view, this.getRequestUri(), this.getNextUri("zones")));
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse(e.getMessage(), null);
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), null);
e.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Find the view names.
     * 
     * @return  the view names.
     */
    @GET
    @Path("views")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findViewNames() {
        
        Response response;
        
        try {
            List<String> viewNames = this.dnsService.findViewNames();
            return buildOkResponse(ViewsWrapper.newInstance(createViewWrappers(viewNames)));
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), null);
e.printStackTrace(); 
        }
        
        return response;
    }

    /**
     * Find the zone.
     * 
     * @param  viewName  the view name.
     * @param  zoneName  the zone name.
     * 
     * @return  the zone.
     */
    @GET
    @Path("views/{viewName}/zones/{zoneName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findZone(@PathParam("viewName") final String viewName, @PathParam("zoneName") final String zoneName) {
        
        Response response;
        
        try {
            Zone zone = this.dnsService.findZone(viewName, zoneName);
            response = buildOkResponse(ZoneWrapper.newInstance(zone, this.getRequestUri(), this.getNextUri("records")));
        }  catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse(e.getMessage(), null);
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), null);
e.printStackTrace();
        }
        
        return response;
    }
 
    /**
     * Find the zone names.
     * 
     * @param  viewName  the view name.
     * 
     * @return  the zone names.
     */
    @GET
    @Path("views/{viewName}/zones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findZoneNames(@PathParam("viewName") final String viewName) {
        
        Response response;
        
        try {
            List<String> zoneNames = this.dnsService.findZoneNames(viewName);
            response = buildOkResponse(ZonesWrapper.newInstance(createZoneWrappers(zoneNames)));
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse(e.getMessage(), null);
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), null);
e.printStackTrace();
        }
        
        return response;
    }
         
    /**
     * Find the zone types.
     * 
     * @return  the zone types.
     */
    @GET
    @Path("zonetypes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findZoneTypes() {
        return buildOkResponse(ZoneTypesWrapper.newInstance(Arrays.asList(ZoneType.values())));       
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
     * @throws  DNSServiceException        if unable to process the records due to an exception.
     * @throws  ResourceNotFoundException  if the view cannot be found.
     */
    @POST
    @Path("views/{viewName}/zones/{zoneName}/records")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processRecordOperations(@PathParam("viewName") final String viewName, @PathParam("zoneName") final String zoneName, final RecordsWrapper recordsWrapper) {

        Response response;
      
        try {
            boolean success = this.dnsService.processRecordOperations(viewName, zoneName, recordsWrapper.getRecords());
            if (success) {
                response = buildOkResponse("Processed records successfully.");
            } else {
                response = buildBadRequestResponse("Records not processed.", recordsWrapper);
            }
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse(e.getMessage(), recordsWrapper);
        } catch (Exception e) {
            response = buildInternalServerErrorResponse(e.getMessage(), recordsWrapper);
e.printStackTrace();
        }

        return response;
    }
}