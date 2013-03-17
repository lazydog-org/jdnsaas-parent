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
import org.lazydog.jdnsaas.model.DNSServer;

/**
 * DNS servers wrapper.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement
public class DNSServersWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<DNSServer> dnsServers = new ArrayList<DNSServer>();
    
    /**
     * Get the DNS servers.
     * 
     * @return  the DNS servers.
     */
    public List<DNSServer> getDnsServers() {
        return this.dnsServers;
    }
    
    /**
     * Create a new instance of the DNS servers wrapper class.
     * 
     * @param  dnsServers  the DNS servers.
     * 
     * @return  a new instance of the DNS servers wrapper class.
     */
    public static DNSServersWrapper newInstance(List<DNSServer> dnsServers) {
        
        DNSServersWrapper dnsServersWrapper = new DNSServersWrapper();
        dnsServersWrapper.setDnsServers(dnsServers);
        
        return dnsServersWrapper;
    }
    
    /**
     * Set the DNS servers.
     * 
     * @param  dnsServers  the DNS servers.
     */
    public void setDnsServers(List<DNSServer> dnsServers) {
        this.dnsServers = dnsServers;
    }
}
