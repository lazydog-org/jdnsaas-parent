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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import org.codehaus.jackson.map.ObjectMapper;
import org.lazydog.jdnsaas.rest.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract resource.
 * 
 * @author  Ron Rickard
 */
public abstract class AbstractResource {
      
    private static final Logger logger = LoggerFactory.getLogger(AbstractResource.class);
    @Context private HttpHeaders httpHeaders;
    @Context private Providers providers;
    @Context private Request request;
    @Context private UriInfo uriInfo;

    protected Response buildBadRequestResponse(final String message, final Object requestEntity) {
        return this.buildResponse(message, requestEntity, Status.BAD_REQUEST);
    }
    
    protected Response buildInternalServerErrorResponse(final String message, final Object requestEntity) {
        return this.buildResponse(message, requestEntity, Status.INTERNAL_SERVER_ERROR);
    }
    
    protected Response buildNotFoundResponse(final String message, final Object requestEntity) {
        return this.buildResponse(message, requestEntity, Status.NOT_FOUND);
    }
    
    protected Response buildOkResponse(final Object responseEntity) {
        return this.buildResponse(responseEntity, Status.OK);
    }
    
    /**
     * Build the response.
     * 
     * @param  message        the message.
     * @param  requestEntity  the request entity.
     * @param  status         the status.
     * 
     * @return  the response.
     */
    private Response buildResponse(final String message, final Object requestEntity, final Status status) {
        
        List<ErrorResponse.RequestHeader> requestHeaders = new ArrayList<ErrorResponse.RequestHeader>();
        
        for (String headerName : this.httpHeaders.getRequestHeaders().keySet()) {

            ErrorResponse.RequestHeader requestHeader = new ErrorResponse.RequestHeader();
            requestHeader.setName(headerName);
            requestHeader.setValues(this.httpHeaders.getRequestHeaders().get(headerName));
            
            requestHeaders.add(requestHeader);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDate(new Date());
        errorResponse.setMessage(message);
        errorResponse.setRequestBody(this.convertToJSONString(requestEntity));
        errorResponse.setRequestHeaders(requestHeaders);
        errorResponse.setRequestMethod(this.request.getMethod());
        errorResponse.setRequestUri(this.getRequestUri());
        errorResponse.setStatus(status.getStatusCode() + " " + status.getReasonPhrase());

        return this.buildResponse(errorResponse, status);
    }

    /**
     * Build the response.
     * 
     * @param  responseEntity  the response entity.
     * @param  status          the status.
     * 
     * @return  the response.
     */
    private Response buildResponse(final Object responseEntity, final Status status) {
        logger.trace("Responding with [{}] {}.", status.getStatusCode(), this.convertToJSONString(responseEntity));
        return Response.status(status).entity(responseEntity).build();
    }
    
    /**
     * Convert the entity to a JSON string.
     * 
     * @param  entity  the entity.
     * 
     * @return  the entity represented as a JSON string.
     */
    protected String convertToJSONString(final Object entity) {
        
        String jsonString = null;
        
        // Check if the entity exists.
        if (entity != null) {
            try {
                
                // Convert the entity to a JSON string.
                jsonString = this.providers.getContextResolver(ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE).getContext(entity.getClass()).writeValueAsString(entity);
            } catch (IOException e) {
                logger.trace("Failed to convert " + entity + " to JSON string.", e);
            }
        }
        
        return jsonString;
    }

    /**
     * Get the request URI.
     * 
     * @return  the request URI.
     */
    protected String getRequestUri() {
        return this.uriInfo.getAbsolutePath().toASCIIString();
    }
       
    /**
     * Get the next URI.
     * 
     * @param  path  the path to add to the request URI.
     * 
     * @return  the next URI.
     */
    protected String getNextUri(final String path) {
        return this.uriInfo.getAbsolutePathBuilder().path(path).build().toASCIIString();
    }
}