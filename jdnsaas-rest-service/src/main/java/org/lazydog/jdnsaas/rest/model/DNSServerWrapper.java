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

import javax.xml.bind.annotation.XmlRootElement;
import org.lazydog.jdnsaas.model.DNSServer;

/**
 * DNS server wrapper.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement
public class DNSServerWrapper extends DNSServer {

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
     * Create a new instance of the DNS server wrapper class.
     * 
     * @param  dnsServerName  the DNS server name.
     * @param  url            the URL.
     * 
     * @return  a new instance of the DNS server wrapper class.
     */
    public static DNSServerWrapper newInstance(String dnsServerName, String url) {

        DNSServerWrapper dnsServerResponse = new DNSServerWrapper();
        dnsServerResponse.setName(dnsServerName);
        dnsServerResponse.setUrl(url);
        
        return dnsServerResponse;
    }
    
    /**
     * Create a new instance of the DNS server wrapper class.
     * 
     * @param  dnsServer  the DNS server name.
     * @param  url        the URL.
     * @param  zonesUrl   the zones URL.
     * 
     * @return  a new instance of the DNS server wrapper class.
     */
    public static DNSServerWrapper newInstance(DNSServer dnsServer, String url, String zonesUrl) {
        
        DNSServerWrapper dnsServerResponse = new DNSServerWrapper();
        dnsServerResponse.setName(dnsServer.getName());
        dnsServerResponse.setPort(dnsServer.getPort());
        dnsServerResponse.setUrl(url);
        dnsServerResponse.setZonesUrl(zonesUrl);

        return dnsServerResponse;
    }
    
    /**
     * Set the URL.
     * 
     * @param  url  the URL.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Set the zones URL.
     * 
     * @param  zonesUrl  the zones URL.
     */
    public void setZonesUrl(String zonesUrl) {
        this.zonesUrl = zonesUrl;
    }
}
