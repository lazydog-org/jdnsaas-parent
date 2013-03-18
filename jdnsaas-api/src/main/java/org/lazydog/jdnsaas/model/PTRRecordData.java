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
 * PTR record data.
 * 
 * @author  Ron Rickard
 */
public class PTRRecordData extends RecordData {
    
    private static final long serialVersionUID = 1L;
    private String target;

    /**
     * Get the target.
     * 
     * @return  the target.
     */
    public String getTarget() {
        return this.target;
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
