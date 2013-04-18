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
import org.lazydog.jdnsaas.model.TSIGKeyAlgorithm;

/**
 * Transaction signature (TSIG) key algorithms wrapper.
 * 
 * @author  Ron Rickard
 */
public class TSIGKeyAlgorithmsWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<TSIGKeyAlgorithm> tsigKeyAlgorithms = new ArrayList<TSIGKeyAlgorithm>();
    
    /**
     * Get the transaction signature (TSIG) key algorithms.
     * 
     * @return  the transaction signature (TSIG) key algorithms.
     */
    public List<TSIGKeyAlgorithm> getTSIGKeyAlgorithms() {
        return this.tsigKeyAlgorithms;
    }
        
    /**
     * Create a new instance of the transaction signature (TSIG) key algorithms wrapper class.
     * 
     * @param  transactionSignatureAlgorithms  the transaction signature (TSIG) key algorithms.
     * 
     * @return  a new instance of the transaction signature (TSIG) key algorithms wrapper class.
     */
    public static TSIGKeyAlgorithmsWrapper newInstance(final List<TSIGKeyAlgorithm> tsigKeyAlgorithms) {
        
        TSIGKeyAlgorithmsWrapper tsigKeyAlgorithmsWrapper = new TSIGKeyAlgorithmsWrapper();
        tsigKeyAlgorithmsWrapper.setTSIGKeyAlgorithms(tsigKeyAlgorithms);
        
        return tsigKeyAlgorithmsWrapper;
    }
    
    /**
     * Set the transaction signature (TSIG) key algorithms.
     * 
     * @param  tsigKeyAlgorithms  the transaction signature (TSIG) key algorithms.
     */
    public void setTSIGKeyAlgorithms(final List<TSIGKeyAlgorithm> tsigKeyAlgorithms) {
        this.tsigKeyAlgorithms = tsigKeyAlgorithms;
    }
}
