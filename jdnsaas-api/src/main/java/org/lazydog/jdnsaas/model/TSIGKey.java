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
 * Transaction signature (TSIG) key.
 * 
 * @author  Ron Rickard
 */
public class TSIGKey extends Entity {
    
    private static final long serialVersionUID = 1L;
    private TSIGKeyAlgorithm algorithm;
    private String name;
    private String value;

    /**
     * Get the algorithm.
     * 
     * @return  the algorithm.
     */
    public TSIGKeyAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Get the name.
     * 
     * @return  the name.
     */
    public String getName() {
        return this.name;
    }
     
    /**
     * Get the value.
     * 
     * @return  the value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set the algorithm.
     * 
     * @param  algorithm  the algorithm.
     */
    public void setAlgorithm(final TSIGKeyAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(final String name) {
        this.name = name;
    }
        
    /**
     * Set the value.
     * 
     * @param  value  the value.
     */
    public void setValue(final String value) {
        this.value = value;
    }
}
