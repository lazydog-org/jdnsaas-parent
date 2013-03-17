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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Record type test.
 * 
 * @author  Ron Rickard
 */
public class RecordTypeTest {
        
    @Test
    public void testFromString() {
        assertEquals(RecordType.AAAA, RecordType.fromString("aaaa"));
        assertEquals(RecordType.AAAA, RecordType.fromString("AAAA"));
        assertEquals(RecordType.A, RecordType.fromString("a"));
        assertEquals(RecordType.A, RecordType.fromString("A"));
        assertEquals(RecordType.ANY, RecordType.fromString("any"));
        assertEquals(RecordType.ANY, RecordType.fromString("ANY"));
        assertEquals(RecordType.CNAME, RecordType.fromString("cname"));
        assertEquals(RecordType.CNAME, RecordType.fromString("CNAME"));
        assertEquals(RecordType.MX, RecordType.fromString("mx"));
        assertEquals(RecordType.MX, RecordType.fromString("MX"));
        assertEquals(RecordType.NS, RecordType.fromString("ns"));
        assertEquals(RecordType.NS, RecordType.fromString("NS"));
        assertEquals(RecordType.PTR, RecordType.fromString("ptr"));
        assertEquals(RecordType.PTR, RecordType.fromString("PTR"));
        assertEquals(RecordType.SRV, RecordType.fromString("srv"));
        assertEquals(RecordType.SRV, RecordType.fromString("SRV"));
        assertEquals(RecordType.TXT, RecordType.fromString("txt"));
        assertEquals(RecordType.TXT, RecordType.fromString("TXT"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFromStringIllegalArgumentException() {
        RecordType.fromString("nonExistentValue");
    }
    
    @Test
    public void testGetForwardTypes() {
        assertEquals(Arrays.asList(RecordType.AAAA, RecordType.A, RecordType.ANY, RecordType.CNAME, RecordType.MX, RecordType.NS, RecordType.SRV, RecordType.TXT), RecordType.getForwardTypes());
    }
    
    @Test
    public void testGetReverseTypes() {
        assertEquals(Arrays.asList(RecordType.ANY, RecordType.NS, RecordType.PTR), RecordType.getReverseTypes());
    }
    
    @Test
    public void testIsForwardType() {
        assertTrue(RecordType.AAAA.isForwardType());
        assertTrue(RecordType.A.isForwardType());
        assertTrue(RecordType.ANY.isForwardType());
        assertTrue(RecordType.CNAME.isForwardType());
        assertTrue(RecordType.MX.isForwardType());
        assertTrue(RecordType.NS.isForwardType());
        assertFalse(RecordType.PTR.isForwardType());
        assertTrue(RecordType.SRV.isForwardType());
        assertTrue(RecordType.TXT.isForwardType());
    }
        
    @Test
    public void testIsReverseType() {
        assertFalse(RecordType.AAAA.isReverseType());
        assertFalse(RecordType.A.isReverseType());
        assertTrue(RecordType.ANY.isReverseType());
        assertFalse(RecordType.CNAME.isReverseType());
        assertFalse(RecordType.MX.isReverseType());
        assertTrue(RecordType.NS.isReverseType());
        assertTrue(RecordType.PTR.isReverseType());
        assertFalse(RecordType.SRV.isReverseType());
        assertFalse(RecordType.TXT.isReverseType());
    }
}
