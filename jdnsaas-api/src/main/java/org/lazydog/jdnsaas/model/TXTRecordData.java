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

import java.util.List;

/**
 * TXT record data.
 * 
 * @author  Ron Rickard
 */
public class TXTRecordData extends RecordData {
    
    private static final long serialVersionUID = 1L;
    private List<String> values;

    /**
     * Get the values.
     * 
     * @return  the values.
     */    
    public List<String> getValues() {
        return values;
    }

    /**
     * Set the values.
     * 
     * @param  values  the values.
     */
    public void setValues(final List<String> values) {
        this.values = values;
    }    
}
