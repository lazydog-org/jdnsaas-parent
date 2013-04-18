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
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Views wrapper.
 * 
 * @author  Ron Rickard
 */
public class ViewsWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @JsonProperty("views")
    private List<ViewWrapper> viewWrappers = new ArrayList<ViewWrapper>();
    
    /**
     * Get the view wrappers.
     * 
     * @return  the view wrappers.
     */
    public List<ViewWrapper> getViewWrappers() {
        return this.viewWrappers;
    }
        
    /**
     * Create a new instance of the views wrapper class.
     * 
     * @param  viewWrappers  the view wrappers.
     * 
     * @return  a new instance of the view wrapper class.
     */
    public static ViewsWrapper newInstance(final List<ViewWrapper> viewWrappers) {
        ViewsWrapper viewsWrapper = new ViewsWrapper();
        viewsWrapper.setViewWrappers(viewWrappers);
        return viewsWrapper;
    }
    
    /**
     * Set the view wrappers.
     * 
     * @param  viewWrappers  the view wrappers.
     */
    public void setViewWrappers(final List<ViewWrapper> viewWrappers) {
        this.viewWrappers = viewWrappers;
    }
}
