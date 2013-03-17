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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * MX record data.
 * 
 * @author  Ron Rickard
 */
@XmlRootElement
public class MXRecordData extends RecordData {
    
    private static final long serialVersionUID = 1L;
    private Integer priority = new Integer(0);
    private String target;

    /**
     * Get the priority.
     * 
     * @return  the priority.
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
}
