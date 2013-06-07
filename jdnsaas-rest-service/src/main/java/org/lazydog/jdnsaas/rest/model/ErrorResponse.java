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
package org.lazydog.jdnsaas.rest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Error response.
 * 
 * @author  Ron Rickard
 */
@JsonPropertyOrder(value = {"status", "message", "requestUri", "requestMethod", "requestBody", "date"})
public class ErrorResponse {
    
    private Date date;
    private String message;
    private String requestBody;
    private List<RequestHeader> requestHeaders = new ArrayList<RequestHeader>();
    private String requestMethod;
    private String requestUri;
    private String status;
    
    /**
     * Get the date.
     * 
     * @return  the date.
     */
    public Date getDate() {
        return this.date;
    }
    
    /**
     * Get the message.
     * 
     * @return  the message.
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * Get the request body.
     * 
     * @return  the request body.
     */
    public String getRequestBody() {
        return this.requestBody;
    }
    
    /**
     * Get the request headers.
     * 
     * @return  the request headers.
     */
    public List<RequestHeader> getRequestHeaders() {
        return this.requestHeaders;
    }
    
    /**
     * Get the request method.
     * 
     * @return  the request method.
     */
    public String getRequestMethod() {
        return this.requestMethod;
    }
    
    /**
     * Get the request URI.
     * 
     * @return  the request URI.
     */
    public String getRequestUri() {
        return this.requestUri;
    }
    
    /**
     * Get the status.
     * 
     * @return  the status.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Set the date.
     * 
     * @param  date  the date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Set the message.
     * 
     * @param  message  the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Set the request body.
     * 
     * @param  requestBody  the request body.
     */
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
    
    /**
     * Set the request headers.
     * 
     * @param  requestHeaders  the request headers.
     */
    public void setRequestHeaders(List<RequestHeader> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }
    
    /**
     * Set the request method.
     * 
     * @param  requestMethod  the request method.
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    
    /**
     * Set the request URI.
     * 
     * @param  requestUri  the request URI.
     */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
    
    /**
     * Set the status.
     * 
     * @param  status  the status.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Request header.
     */
    public static class RequestHeader {
        
        private String name;
        private List<String> values;
        
        /**
         * Get the name.
         * 
         * @return  the name.
         */
        public String getName() {
            return this.name;
        }
        
        /**
         * Get the values.
         * 
         * @return  the values.
         */
        public List<String> getValues() {
            return this.values;
        }
        
        /**
         * Set the name.
         * 
         * @param  name  the name.
         */
        public void setName(String name) {
            this.name = name;
        }
        
        /**
         * Set the values.
         * 
         * @param  values  the values.
         */
        public void setValues(List<String> values) {
            this.values = values;
        }
    }
}
