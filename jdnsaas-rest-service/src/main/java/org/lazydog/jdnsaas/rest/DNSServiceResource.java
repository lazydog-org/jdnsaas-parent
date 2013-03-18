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
package org.lazydog.jdnsaas.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.lazydog.jdnsaas.DNSService;
import org.lazydog.jdnsaas.DNSServiceException;
import org.lazydog.jdnsaas.ResourceNotFoundException;
import org.lazydog.jdnsaas.bind.DNSServiceImpl;
import org.lazydog.jdnsaas.model.DNSServer;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.Zone;
import org.lazydog.jdnsaas.rest.model.DNSServerWrapper;
import org.lazydog.jdnsaas.rest.model.DNSServersWrapper;
import org.lazydog.jdnsaas.rest.model.RecordsWrapper;
import org.lazydog.jdnsaas.rest.model.ZoneWrapper;
import org.lazydog.jdnsaas.rest.model.ZonesWrapper;

/**
 * DNS service resource.
 * 
 * @author  Ron Rickard
 */
@Path("1.0")
public class DNSServiceResource {
      
    private DNSService dnsService;
    @Context
    private UriInfo uriInfo;
   
    /**
     * Initialize the DNS service resource.
     */
    public DNSServiceResource() throws DNSServiceException {
        dnsService = DNSServiceImpl.newInstance();
    }
    
    /**
     * Build the bad request response.
     * 
     * @return  the bad request response.
     */
    private static Response buildBadRequestResponse() {
        return Response.status(Status.BAD_REQUEST).build();
    }
    
    /**
     * Build the internal server error response.
     * 
     * @return  the internal server error response.
     */
    private static Response buildInternalServerErrorResponse() {
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();    
    }
    
    /**
     * Build the not found response.
     * 
     * @return  the not found response.
     */
    private static Response buildNotFoundResponse() {
        return Response.status(Status.NOT_FOUND).build();
    }
    
    /**
     * Build the ok response.
     * 
     * @param  object  the object.
     * 
     * @return  the ok response.
     */
    private static Response buildOkResponse(final Object object) {
        return Response.ok(object).build();
    }

    /**
     * Create the DNS server wrappers.
     * 
     * @param  dnsServerNames  the DNS server names.
     * 
     * @return  the DNS server wrappers.
     */
    private List<DNSServerWrapper> createDnsServerWrappers(final List<String> dnsServerNames) {
        
        List<DNSServerWrapper> dnsServerWrappers = new ArrayList<DNSServerWrapper>();
        
        for (String dnsServerName : dnsServerNames) {
            dnsServerWrappers.add(DNSServerWrapper.newInstance(dnsServerName, this.getNextPath(dnsServerName)));
        }
        
        return dnsServerWrappers;
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
            zoneWrappers.add(ZoneWrapper.newInstance(zoneName, this.getNextPath(zoneName)));
        }
        
        return zoneWrappers;
    }
     
    /**
     * Get the current path.
     * 
     * @return  the current path.
     */
    private String getCurrentPath() {
        return this.uriInfo.getAbsolutePath().toASCIIString();
    }

    /**
     * Get the DNS server.
     * 
     * @param  dnsServerName  the DNS server.
     * 
     * @return  the DNS server.
     */
    @GET
    @Path("dnsservers/{dnsServerName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDnsServer(@PathParam("dnsServerName") final String dnsServerName) {
        
        Response response;
        
        try {
            DNSServer dnsServer = this.dnsService.getDnsServer(dnsServerName);
            response = buildOkResponse(DNSServerWrapper.newInstance(dnsServer, this.getCurrentPath(), this.getNextPath("zones")));
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse();
        } catch (Exception e) {
            response = buildInternalServerErrorResponse();
System.out.println(e);
        }
        
        return response;
    }
    
    /**
     * Get the DNS servers.
     * 
     * @return  the DNS servers.
     */
    @GET
    @Path("dnsservers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDnsServerNames() {
        
        Response response;
        
        try {
            List<String> dnsServerNames = this.dnsService.getDnsServerNames();
            return buildOkResponse(DNSServersWrapper.newInstance(createDnsServerWrappers(dnsServerNames)));
        } catch (Exception e) {
            response = buildInternalServerErrorResponse();
System.out.println(e); 
        }
        
        return response;
    }
        
    /**
     * Get the next path.
     * 
     * @param  path  the path to add to the current path.
     * 
     * @return  the next path.
     */
    private String getNextPath(final String path) {
        return this.uriInfo.getAbsolutePathBuilder().path(path).build().toASCIIString();
    }
 
    /**
     * Get the records for the DNS server name and zone name.
     * Optionally, filter the records by the record type and/or record name.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  zoneName       the zone name.
     * @param  recordType     the record type.
     * @param  recordName     the record name.
     * 
     * @return  the records.
     */
    @GET
    @Path("dnsservers/{dnsServerName}/zones/{zoneName}/records")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecords(@PathParam("dnsServerName") final String dnsServerName, @PathParam("zoneName") final String zoneName, @DefaultValue("any") @QueryParam("recordType") final String recordType, @QueryParam("recordName") final String recordName) {
        
        Response response;
        
        try {
            List<Record> records = this.dnsService.getRecords(dnsServerName, zoneName, RecordType.fromString(recordType), recordName);
            response = buildOkResponse(RecordsWrapper.newInstance(records)); 
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse();
        } catch (Exception e) {
            response = buildInternalServerErrorResponse();
System.out.println(e);
        }
        
        return response;
    }

    /**
     * Get the zone for the DNS server and zone names.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  zoneName       the zone name.
     * 
     * @return  the zone.
     */
    @GET
    @Path("dnsservers/{dnsServerName}/zones/{zoneName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getZone(@PathParam("dnsServerName") final String dnsServerName, @PathParam("zoneName") final String zoneName) {
        
        Response response;
        
        try {
            Zone zone = this.dnsService.getZone(dnsServerName, zoneName);
            response = buildOkResponse(ZoneWrapper.newInstance(zone, this.getCurrentPath(), this.getNextPath("records")));
        }  catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse();
        } catch (Exception e) {
            response = buildInternalServerErrorResponse();
System.out.println(e);
        }
        
        return response;
    }
 
    /**
     * Get the zones for the DNS server name.
     * 
     * @param  dnsServerName  the DNS server name.
     * 
     * @return  the zones.
     */
    @GET
    @Path("dnsservers/{dnsServerName}/zones")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getZoneNames(@PathParam("dnsServerName") final String dnsServerName) {
        
        Response response;
        
        try {
            List<String> zoneNames = this.dnsService.getZoneNames(dnsServerName);
            response = buildOkResponse(ZonesWrapper.newInstance(createZoneWrappers(zoneNames)));
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse();
        } catch (Exception e) {
            response = buildInternalServerErrorResponse();
System.out.println(e);
        }
        
        return response;
    }
    
    /**
     * Process the records.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  zoneName       the zone name.
     * @param  records        the records.
     * 
     * @return  true if the records are processed successfully, otherwise false.
     * 
     * @throws  DNSServiceException        if unable to process the records due to an exception.
     * @throws  ResourceNotFoundException  if the DNS server cannot be found.
     */
    @POST
    @Path("dnsservers/{dnsServerName}/zones/{zoneName}/records")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processRecords(@PathParam("dnsServerName") final String dnsServerName, @PathParam("zoneName") final String zoneName, final RecordsWrapper recordsWrapper) {

        Response response;
        
        try {
            boolean success = this.dnsService.processRecords(dnsServerName, zoneName, recordsWrapper.getRecords());
            if (success) {
                response = buildOkResponse("Processed records successfully.");
            } else {
                response = buildBadRequestResponse();
            }
        } catch (ResourceNotFoundException e) {
            response = buildNotFoundResponse();
        } catch (Exception e) {
            response = buildInternalServerErrorResponse();
System.out.println(e);
        }

        return response;
    }
}