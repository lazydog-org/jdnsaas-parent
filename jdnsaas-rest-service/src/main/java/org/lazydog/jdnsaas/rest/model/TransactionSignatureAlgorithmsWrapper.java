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
import org.lazydog.jdnsaas.model.TransactionSignatureAlgorithm;

/**
 * Transaction signature algorithms wrapper.
 * 
 * @author  Ron Rickard
 */
public class TransactionSignatureAlgorithmsWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private List<TransactionSignatureAlgorithm> transactionSignatureAlgorithms = new ArrayList<TransactionSignatureAlgorithm>();
    
    /**
     * Get the transaction signature algorithms.
     * 
     * @return  the transaction signature algorithms.
     */
    public List<TransactionSignatureAlgorithm> getTransactionSignatureAlgorithms() {
        return this.transactionSignatureAlgorithms;
    }
        
    /**
     * Create a new instance of the transaction signature algorithms wrapper class.
     * 
     * @param  transactionSignatureAlgorithms  the transaction signature algorithms.
     * 
     * @return  a new instance of the transaction signature algorithms wrapper class.
     */
    public static TransactionSignatureAlgorithmsWrapper newInstance(final List<TransactionSignatureAlgorithm> transactionSignatureAlgorithms) {
        
        TransactionSignatureAlgorithmsWrapper transactionSignatureAlgorithmsWrapper = new TransactionSignatureAlgorithmsWrapper();
        transactionSignatureAlgorithmsWrapper.setTransactionSignatureAlgorithms(transactionSignatureAlgorithms);
        
        return transactionSignatureAlgorithmsWrapper;
    }
    
    /**
     * Set the transaction signature algorithms.
     * 
     * @param  transactionSignatureAlgorithms  the transaction signature algorithms.
     */
    public void setTransactionSignatureAlgorithms(final List<TransactionSignatureAlgorithm> transactionSignatureAlgorithms) {
        this.transactionSignatureAlgorithms = transactionSignatureAlgorithms;
    }
}
