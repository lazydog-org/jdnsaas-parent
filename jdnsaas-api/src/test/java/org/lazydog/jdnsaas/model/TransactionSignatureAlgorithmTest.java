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

import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Record type test.
 * 
 * @author  Ron Rickard
 */
public class TransactionSignatureAlgorithmTest {
        
    @Test
    public void testAsString() {
        assertEquals("hmac-md5", TransactionSignatureAlgorithm.HMAC_MD5.asString());
        assertEquals("hmac-sha1", TransactionSignatureAlgorithm.HMAC_SHA1.asString());
        assertEquals("hmac-sha224", TransactionSignatureAlgorithm.HMAC_SHA224.asString());
        assertEquals("hmac-sha256", TransactionSignatureAlgorithm.HMAC_SHA256.asString());
        assertEquals("hmac-sha384", TransactionSignatureAlgorithm.HMAC_SHA384.asString());
        assertEquals("hmac-sha512", TransactionSignatureAlgorithm.HMAC_SHA512.asString());
    }
}
