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

import org.lazydog.jdnsaas.model.View;

/**
 * View wrapper.
 * 
 * @author  Ron Rickard
 */
public class ViewWrapper extends View {

    private static final long serialVersionUID = 1L;
    private String url;
    private String zonesUrl;

    /**
     * Get the URL.
     * 
     * @return  the URL.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Get the zones URL.
     * 
     * @return  the zones URL.
     */
    public String getZonesUrl() {
        return this.zonesUrl;
    }
    
    /**
     * Create a new instance of the view wrapper class.
     * 
     * @param  viewName  the view name.
     * @param  url       the URL.
     * 
     * @return  a new instance of the view wrapper class.
     */
    public static ViewWrapper newInstance(final String viewName, final String url) {

        ViewWrapper viewWrapper = new ViewWrapper();
        viewWrapper.setName(viewName);
        viewWrapper.setUrl(url);
        
        return viewWrapper;
    }
    
    /**
     * Create a new instance of the view wrapper class.
     * 
     * @param  view      the view name.
     * @param  url       the URL.
     * @param  zonesUrl  the zones URL.
     * 
     * @return  a new instance of the view wrapper class.
     */
    public static ViewWrapper newInstance(final View view, final String url, final String zonesUrl) {
        
        ViewWrapper viewWrapper = new ViewWrapper();
        viewWrapper.setDnsServer(view.getDnsServer());
        viewWrapper.setName(view.getName());
        viewWrapper.setUrl(url);
        viewWrapper.setZonesUrl(zonesUrl);

        return viewWrapper;
    }
    
    /**
     * Set the URL.
     * 
     * @param  url  the URL.
     */
    public void setUrl(final String url) {
        this.url = url;
    }
    
    /**
     * Set the zones URL.
     * 
     * @param  zonesUrl  the zones URL.
     */
    public void setZonesUrl(final String zonesUrl) {
        this.zonesUrl = zonesUrl;
    }
}
