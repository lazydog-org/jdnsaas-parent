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
 * Transaction signature (TSIG) key algorithm.
 * 
 * @author  Ron Rickard
 */
public enum TSIGKeyAlgorithm {
    HMAC_MD5,
    HMAC_SHA1,
    HMAC_SHA224,
    HMAC_SHA256,
    HMAC_SHA384,
    HMAC_SHA512;

    /**
     * Get the transaction signature (TSIG) key algorithm as a String.
     * 
     * @return  the transaction signature (TSIG) key algorithm as a String.
     */
    public String asString() {
        return this.toString().replace("_", "-").toLowerCase();
    }
}
