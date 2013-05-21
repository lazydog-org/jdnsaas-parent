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
 * SRV record.
 * 
 * @author  Ron Rickard
 */
public class SRVRecord extends Record<SRVRecord.Data> {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Initialize the record with the record type.
     */
    public SRVRecord() {
        this.setType(RecordType.SRV);
    }
    
    /**
     * SRV record data.
     * 
     * @author  Ron Rickard
     */
    public static class Data extends Record.Data {

        private static final long serialVersionUID = 1L;
        private Integer priority = new Integer(0);
        private Integer port = new Integer(0);
        private String target;
        private Integer weight = new Integer(0);

        /**
         * Initialize the record data.
         */
        public Data() {
            super();
        }
        
        /**
         * Get the port.
         * 
         * @return  the port.
         */
        public Integer getPort() {
            return this.port;
        }

        /**
         * Get the priority.
         * 
         * @return the priority.
         */
        public Integer getPriority() {
            return this.priority;
        }

        /**
         * Get the target.
         * 
         * @return  the target.
         */
        public String getTarget() {
            return this.target;
        }

        /**
         * Get the weight.
         * 
         * @return  the weight.
         */
        public Integer getWeight() {
            return this.weight;
        }

        /**
         * Set the port.
         * 
         * @param  port  the port.
         */
        public void setPort(final Integer port) {
            this.port = replaceNull(port, new Integer(0));
        }

        /**
         * Set the priority.
         * 
         * @param  priority  the priority.
         */
        public void setPriority(final Integer priority) {
            this.priority = replaceNull(priority, new Integer(0));
        }

        /**
         * Set the target.
         * 
         * @param  target  the target.
         */
        public void setTarget(final String target) {
            this.target = target;
        }

        /**
         * Set the weight.
         * 
         * @param  weight  the weight;
         */
        public void setWeight(final Integer weight) {
            this.weight = replaceNull(weight, new Integer(0));
        }
    }
}
