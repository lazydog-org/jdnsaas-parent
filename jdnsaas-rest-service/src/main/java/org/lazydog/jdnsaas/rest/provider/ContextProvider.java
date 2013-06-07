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
package org.lazydog.jdnsaas.rest.provider;

import java.text.SimpleDateFormat;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Context provider.
 * 
 * @author  Ron Rickard
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ContextProvider implements ContextResolver<ObjectMapper> {
    
    /**
     * Get the object mapper that will convert an object to and from JSON.
     * 
     * @param  type  the object type.
     * 
     * @return  the object mapper.
     */
    @Override
    public ObjectMapper getContext(final Class<?> type) {
        
        // Initialize the object mapper.
        ObjectMapper objectMapper = new ObjectMapper();

        // Set the date format.
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        
        // Do not include null values in the JSON.
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        
        // Include fields, not getters.
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        objectMapper.setVisibility(JsonMethod.GETTER, Visibility.NONE);
        objectMapper.setVisibility(JsonMethod.IS_GETTER, Visibility.NONE);

        return objectMapper;
    }
}
