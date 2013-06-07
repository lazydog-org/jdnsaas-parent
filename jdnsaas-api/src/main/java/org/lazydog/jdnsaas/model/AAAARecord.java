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
package org.lazydog.jdnsaas.model;

/**
 * AAAA record.
 * 
 * @author  Ron Rickard
 */
public class AAAARecord extends Record<AAAARecord.Data> {
    
    private static final long serialVersionUID = 1L;

    /**
     * Initialize the record with the record type.
     */
    public AAAARecord() {
        this.setType(RecordType.AAAA);
    }
    
    /**
     * AAAA record data.
     */
    public static class Data extends Record.Data {

        private static final long serialVersionUID = 1L;
        private String ipv6Address;

        /**
         * Get the IPv6 address.
         * 
         * @return  the IPv6 address.
         */
        public String getIpv6Address() {
            return this.ipv6Address;
        }

        /**
         * Set the IPv6 address.
         * 
         * @param  ipv6Address  the IPv6 address.
         */
        public void setIpv6Address(final String ipv6Address) {
            this.ipv6Address = ipv6Address;
        }
    }
}
