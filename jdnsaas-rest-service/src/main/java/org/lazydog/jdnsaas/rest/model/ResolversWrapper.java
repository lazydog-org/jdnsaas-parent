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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.lazydog.jdnsaas.model.Resolver;

/**
 * Resolvers wrapper.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement                                         // Needed by Enunciate.
public class ResolversWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<Resolver> resolvers = new ArrayList<Resolver>();
    
    /**
     * Get the resolvers.
     * 
     * @return  the resolvers.
     */
    public List<Resolver> getResolvers() {
        return this.resolvers;
    }
        
    /**
     * Create a new instance of the resolvers wrapper class.
     * 
     * @param  resolvers  the resolvers.
     * 
     * @return  a new instance of the resolvers wrapper class.
     */
    public static ResolversWrapper newInstance(final List<Resolver> resolvers) {
        ResolversWrapper resolversWrapper = new ResolversWrapper();
        resolversWrapper.setResolvers(resolvers);
        return resolversWrapper;
    }
    
    /**
     * Set the resolvers.
     * 
     * @param  resolvers  the resolvers.
     */
    public void setResolvers(final List<Resolver> resolvers) {
        this.resolvers = resolvers;
    }
}
