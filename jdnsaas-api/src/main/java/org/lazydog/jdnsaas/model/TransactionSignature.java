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
 * Transaction signature (TSIG).
 * 
 * @author  Ron Rickard
 */
public class TransactionSignature extends Entity {
    
    private static final long serialVersionUID = 1L;
    private TransactionSignatureAlgorithm algorithm;
    private String name;
    private String secret;

    /**
     * Get the algorithm.
     * 
     * @return  the algorithm.
     */
    public TransactionSignatureAlgorithm getAlgorithm() {
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
     * Get the secret.
     * 
     * @return  the secret.
     */
    public String getSecret() {
        return this.secret;
    }

    /**
     * Set the algorithm.
     * 
     * @param  algorithm  the algorithm.
     */
    public void setAlgorithm(TransactionSignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(String name) {
        this.name = name;
    }
        
    /**
     * Set the secret.
     * 
     * @param  secret  the secret.
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
