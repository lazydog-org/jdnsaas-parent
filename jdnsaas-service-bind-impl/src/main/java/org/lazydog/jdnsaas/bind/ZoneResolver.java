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
package org.lazydog.jdnsaas.bind;

import org.apache.commons.lang3.StringUtils;

/**
 * Zone resolver.
 * 
 * @author  Ron Rickard
 */
final class ZoneResolver {
    
    private static final String IPV4_REVERSE_DOMAIN = "in-addr.arpa";
    private static final String IPV6_REVERSE_DOMAIN = "ip6.arpa";
    private static final String LABEL_SEPARATOR = ".";
    private String relativeZoneName;
    
    /**
     * Hide the constructor.
     * 
     * @param  zoneName  the zone name.
     */
    private ZoneResolver(final String zoneName) {
        this.relativeZoneName = (zoneName.endsWith(LABEL_SEPARATOR)) ? StringUtils.removeEnd(zoneName, LABEL_SEPARATOR) : zoneName;
    }

    /**
     * Absolutize the name.  
     * The absolute name is the name followed by the zone name and a trailing dot.
     * 
     * @param  name  the name.
     * 
     * @return  the absolute name.
     */
    public String absolutize(final String name) {

        String absoluteName = name;

        if (!name.endsWith(LABEL_SEPARATOR) && name.endsWith(this.getRelativeZoneName())) {
            absoluteName = name + LABEL_SEPARATOR;
        }
        else if (!name.endsWith(LABEL_SEPARATOR)) {
            absoluteName = name + LABEL_SEPARATOR + this.getAbsoluteZoneName();
        }
        
        return absoluteName;
    }

    /**
     * Get the absolute zone name.  
     * The absolute zone name includes the trailing dot.
     * 
     * @return  the absolute zone name.
     */
    public String getAbsoluteZoneName() {
        return this.relativeZoneName + LABEL_SEPARATOR;
    }
        
    /**
     * Get the IP (v4 or v6) address.
     * 
     * @param  reverseTets  the IP address tets in reverse.  These are the octets (IPv4) or hextets (IPv6) not in the zone name.
     * 
     * @return  the IP (v4 or v6) address.
     */
    public String getIpAddress(String reverseTets) {
        String reverseDomain = (this.isIpv4ReverseZone()) ? IPV4_REVERSE_DOMAIN : IPV6_REVERSE_DOMAIN;
        return StringUtils.reverseDelimited(reverseTets + LABEL_SEPARATOR + StringUtils.removeEnd(this.relativeZoneName, LABEL_SEPARATOR + reverseDomain), LABEL_SEPARATOR.charAt(0));
    }
    
    /**
     * Get the relative zone name.  
     * The relative zone name does not include the trailing dot.
     * 
     * @return  the relative zone name.
     */
    public String getRelativeZoneName() {
        return this.relativeZoneName;
    }

    /**
     * Get the reverse tets.  
     * These are the octets (IPv4) or hextets (IPv6) not in the zone name.
     * 
     * @param  ipAddress  the IP (v4 or v6) address.
     * 
     * @return  the reverse tets.
     */
    public String getReverseTets(String ipAddress) {
        String reverseDomain = (this.isIpv4ReverseZone()) ? IPV4_REVERSE_DOMAIN : IPV6_REVERSE_DOMAIN;
        return StringUtils.removeEnd(StringUtils.reverseDelimited(ipAddress, LABEL_SEPARATOR.charAt(0)), LABEL_SEPARATOR + StringUtils.removeEnd(this.relativeZoneName, LABEL_SEPARATOR + reverseDomain));
    }
    
    /**
     * Is the zone a forward zone?
     * 
     * @return  true if the zone is a forward zone, otherwise false.
     */
    public boolean isForwardZone() {
        return !isReverseZone();
    }
      
    /**
     * Is the zone an IPv4 reverse zone.
     * 
     * @return  true if the zone is an IPv4 reverse zone, otherwise false.
     */
    public boolean isIpv4ReverseZone() {
        return this.relativeZoneName.endsWith(IPV4_REVERSE_DOMAIN);
    }
        
    /**
     * Is the zone an IPv6 reverse zone.
     * 
     * @return  true if the zone is an IPv6 reverse zone, otherwise false.
     */
    public boolean isIpv6ReverseZone() {
        return this.relativeZoneName.endsWith(IPV6_REVERSE_DOMAIN);
    }
    
    /**
     * Is the zone a reverse zone?
     * 
     * @return  true if the zone is a reverse zone, otherwise false.
     */
    public boolean isReverseZone() {
        return this.relativeZoneName.endsWith(IPV4_REVERSE_DOMAIN) || this.relativeZoneName.endsWith(IPV6_REVERSE_DOMAIN);
    }

    /**
     * Create a new instance of the zone resolver class.
     * 
     * @param  zoneName  the zone name.
     * 
     * @return  a new instance of the zone resolver class.
     */
    public static ZoneResolver newInstance(final String zoneName) {
        return new ZoneResolver(zoneName);
    }
   
    /**
     * Relativize the name.
     * The relative name is the name without the zone name and trailing dot.
     * 
     * @param  name  the name.
     * 
     * @return  the relative name.
     */
    public String relativize(final String name) {

        String relativeName = name;
        
        if (name.endsWith(LABEL_SEPARATOR + this.getAbsoluteZoneName())) {
            relativeName = StringUtils.removeEnd(name, LABEL_SEPARATOR + this.getAbsoluteZoneName());
        }
        else if (name.endsWith(LABEL_SEPARATOR + this.getRelativeZoneName())) {
            relativeName = StringUtils.removeEnd(name, LABEL_SEPARATOR + this.getRelativeZoneName());
        }
        else if (name.endsWith(LABEL_SEPARATOR)) {
            relativeName = StringUtils.removeEnd(name, LABEL_SEPARATOR);
        }
        
        return relativeName;
    }
}
