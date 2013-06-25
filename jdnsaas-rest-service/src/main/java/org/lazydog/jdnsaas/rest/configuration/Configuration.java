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
package org.lazydog.jdnsaas.rest.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.lazydog.jdnsaas.NotifyMessageMonitorAddress;
import org.lazydog.jdnsaas.NotifyMessageMonitorPort;
import org.lazydog.jdnsaas.NotifyMessageMonitorSocketTimeout;
import org.lazydog.jdnsaas.NotifyMessageMonitorThreads;
import org.lazydog.jdnsaas.spi.repository.PersistenceUnitName;

/**
 * Configuration.
 * 
 * @author  Ron Rickard
 */
@ApplicationScoped
public class Configuration {
        
    @Produces
    @NotifyMessageMonitorAddress
    public String getNotifyMessageMonitorAddress() {
        return "192.168.0.25";
    }
        
    @Produces
    @NotifyMessageMonitorPort
    public int getNotifyMessageMonitorPort() {
        return 10053;
    }
        
    @Produces
    @NotifyMessageMonitorSocketTimeout
    public int getNotifyMessageMonitorSocketTimeout() {
        return 20000;
    }
    
    @Produces
    @NotifyMessageMonitorThreads
    public int getNotifyMessageMonitorThreads() {
        return 10;
    }
    
    @Produces
    @PersistenceUnitName
    public String getPersistenceUnitName() {
        return "jdnsaas";
    }
    
    
}
