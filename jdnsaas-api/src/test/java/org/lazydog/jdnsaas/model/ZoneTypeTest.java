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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Zone type test.
 * 
 * @author  Ron Rickard
 */
public class ZoneTypeTest {
        
    @Test
    public void testFromString() {
        assertEquals(ZoneType.BOTH, ZoneType.fromString("both"));
        assertEquals(ZoneType.BOTH, ZoneType.fromString("BOTH"));
        assertEquals(ZoneType.FORWARD, ZoneType.fromString("forward"));
        assertEquals(ZoneType.FORWARD, ZoneType.fromString("FORWARD"));
        assertEquals(ZoneType.REVERSE, ZoneType.fromString("reverse"));
        assertEquals(ZoneType.REVERSE, ZoneType.fromString("REVERSE"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFromStringIllegalArgumentException() {
        ZoneType.fromString("nonExistentValue");
    }
}
