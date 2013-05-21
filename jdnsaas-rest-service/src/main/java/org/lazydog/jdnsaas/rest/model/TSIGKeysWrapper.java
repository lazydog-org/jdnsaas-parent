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
import org.lazydog.jdnsaas.model.TSIGKey;

/**
 * Transaction signature (TSIG) keys wrapper.
 * 
 * @author  Ron Rickard
 */
public class TSIGKeysWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<TSIGKey> tsigKeys = new ArrayList<TSIGKey>();
    
    /**
     * Get the transaction signature (TSIG) keys.
     * 
     * @return  the transaction signature (TSIG) keys.
     */
    public List<TSIGKey> getTsigKeys() {
        return this.tsigKeys;
    }
        
    /**
     * Create a new instance of the transaction signature (TSIG) keys wrapper class.
     * 
     * @param  tsigKeys  the transaction signature (TSIG) keys.
     * 
     * @return  a new instance of the transaction signature (TSIG) keys wrapper class.
     */
    public static TSIGKeysWrapper newInstance(final List<TSIGKey> tsigKeys) {
        TSIGKeysWrapper tsigKeysWrapper = new TSIGKeysWrapper();
        tsigKeysWrapper.setTsigKeys(tsigKeys);
        return tsigKeysWrapper;
    }
    
    /**
     * Set the transaction signature (TSIG) keys.
     * 
     * @param  tsigKeys  the transaction signature (TSIG) keys.
     */
    public void setTsigKeys(final List<TSIGKey> tsigKeys) {
        this.tsigKeys = tsigKeys;
    }
}
