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

import static org.junit.Assert.assertArrayEquals;
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
    public void testValues() {
        assertArrayEquals(new RecordType[]{RecordType.AAAA, RecordType.A, RecordType.ANY, RecordType.CNAME, RecordType.MX, RecordType.NS, RecordType.PTR, RecordType.SRV, RecordType.TXT}, RecordType.values(ZoneType.BOTH));
        assertArrayEquals(new RecordType[]{RecordType.AAAA, RecordType.A, RecordType.ANY, RecordType.CNAME, RecordType.MX, RecordType.NS, RecordType.SRV, RecordType.TXT}, RecordType.values(ZoneType.FORWARD));
        assertArrayEquals(new RecordType[]{RecordType.ANY, RecordType.NS, RecordType.PTR}, RecordType.values(ZoneType.REVERSE));
    }

    @Test
    public void testIsForZoneType() {
        assertTrue(RecordType.AAAA.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.A.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.ANY.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.CNAME.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.MX.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.NS.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.PTR.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.SRV.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.TXT.isForZoneType(ZoneType.BOTH));
        assertTrue(RecordType.AAAA.isForZoneType(ZoneType.FORWARD));
        assertTrue(RecordType.A.isForZoneType(ZoneType.FORWARD));
        assertTrue(RecordType.ANY.isForZoneType(ZoneType.FORWARD));
        assertTrue(RecordType.CNAME.isForZoneType(ZoneType.FORWARD));
        assertTrue(RecordType.MX.isForZoneType(ZoneType.FORWARD));
        assertTrue(RecordType.NS.isForZoneType(ZoneType.FORWARD));
        assertFalse(RecordType.PTR.isForZoneType(ZoneType.FORWARD));
        assertTrue(RecordType.SRV.isForZoneType(ZoneType.FORWARD));
        assertTrue(RecordType.TXT.isForZoneType(ZoneType.FORWARD));
        assertFalse(RecordType.AAAA.isForZoneType(ZoneType.REVERSE));
        assertFalse(RecordType.A.isForZoneType(ZoneType.REVERSE));
        assertTrue(RecordType.ANY.isForZoneType(ZoneType.REVERSE));
        assertFalse(RecordType.CNAME.isForZoneType(ZoneType.REVERSE));
        assertFalse(RecordType.MX.isForZoneType(ZoneType.REVERSE));
        assertTrue(RecordType.NS.isForZoneType(ZoneType.REVERSE));
        assertTrue(RecordType.PTR.isForZoneType(ZoneType.REVERSE));
        assertFalse(RecordType.SRV.isForZoneType(ZoneType.REVERSE));
        assertFalse(RecordType.TXT.isForZoneType(ZoneType.REVERSE));
    }
}
