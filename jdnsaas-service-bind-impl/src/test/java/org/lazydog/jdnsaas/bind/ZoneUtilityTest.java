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
package org.lazydog.jdnsaas.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Zone utility test.
 * 
 * @author  Ron Rickard
 */
public class ZoneUtilityTest {
    
    @Test
    public void testAbsolutize() {
        assertEquals("name.anotherzone.test.", ZoneUtility.newInstance("zone.test").absolutize("name.anotherzone.test."));
        assertEquals("name.zone.test.", ZoneUtility.newInstance("zone.test").absolutize("name"));
        assertEquals("name.zone.test.", ZoneUtility.newInstance("zone.test").absolutize("name.zone.test"));
        assertEquals("name.zone.test.", ZoneUtility.newInstance("zone.test").absolutize("name.zone.test."));
        assertEquals("name.subdomain.zone.test.", ZoneUtility.newInstance("zone.test").absolutize("name.subdomain"));
        assertEquals("name.subdomain.zone.test.", ZoneUtility.newInstance("zone.test").absolutize("name.subdomain.zone.test"));
        assertEquals("name.subdomain.zone.test.", ZoneUtility.newInstance("zone.test").absolutize("name.subdomain.zone.test."));
        assertEquals("4.3.2.1.in-addr.arpa.", ZoneUtility.newInstance("1.in-addr.arpa").absolutize("4.3.2"));
        assertEquals("4.3.2.1.in-addr.arpa.", ZoneUtility.newInstance("1.in-addr.arpa").absolutize("4.3.2.1.in-addr.arpa"));
        assertEquals("4.3.2.1.in-addr.arpa.", ZoneUtility.newInstance("1.in-addr.arpa").absolutize("4.3.2.1.in-addr.arpa."));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").absolutize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").absolutize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").absolutize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa."));
    }
    
    @Test
    public void testGetAbsoluteZoneName() {
        assertEquals("zone.test.", ZoneUtility.newInstance("zone.test").getAbsoluteZoneName());
        assertEquals("zone.test.", ZoneUtility.newInstance("zone.test.").getAbsoluteZoneName());
        assertEquals("3.2.1.in-addr.arpa.", ZoneUtility.newInstance("3.2.1.in-addr.arpa").getAbsoluteZoneName());
        assertEquals("3.2.1.in-addr.arpa.", ZoneUtility.newInstance("3.2.1.in-addr.arpa.").getAbsoluteZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getAbsoluteZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").getAbsoluteZoneName());
    }
    
    @Test
    public void testGetIpAddress() {
        assertEquals("1.2.3.4", ZoneUtility.newInstance("3.2.1.in-addr.arpa").getIpAddress("4"));
        assertEquals("1.2.3.4", ZoneUtility.newInstance("2.1.in-addr.arpa").getIpAddress("4.3"));
        assertEquals("1.2.3.4", ZoneUtility.newInstance("1.in-addr.arpa").getIpAddress("4.3.2"));
        assertEquals("0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f.0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getIpAddress("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0"));
    }
    
    @Test
    public void testGetRelativeZoneName() {
        assertEquals("zone.test", ZoneUtility.newInstance("zone.test").getRelativeZoneName());
        assertEquals("zone.test", ZoneUtility.newInstance("zone.test.").getRelativeZoneName());
        assertEquals("3.2.1.in-addr.arpa", ZoneUtility.newInstance("3.2.1.in-addr.arpa").getRelativeZoneName());
        assertEquals("3.2.1.in-addr.arpa", ZoneUtility.newInstance("3.2.1.in-addr.arpa.").getRelativeZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getRelativeZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").getRelativeZoneName());
    }
        
    @Test
    public void testGetReverseTets() {
        assertEquals("4", ZoneUtility.newInstance("3.2.1.in-addr.arpa").getReverseTets("1.2.3.4"));
        assertEquals("4.3", ZoneUtility.newInstance("2.1.in-addr.arpa").getReverseTets("1.2.3.4"));
        assertEquals("4.3.2", ZoneUtility.newInstance("1.in-addr.arpa").getReverseTets("1.2.3.4"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getReverseTets("0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f.0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f"));
    }
            
    @Test
    public void testIsForwardZone() {
        assertTrue(ZoneUtility.newInstance("zone.test").isForwardZone());
        assertTrue(ZoneUtility.newInstance("zone.test.").isForwardZone());
        assertFalse(ZoneUtility.newInstance("3.2.1.in-addr.arpa").isForwardZone());
        assertFalse(ZoneUtility.newInstance("3.2.1.in-addr.arpa.").isForwardZone());
        assertFalse(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isForwardZone());
        assertFalse(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isForwardZone());
    }
                        
    @Test
    public void testIsIpv4ReverseZone() {
        assertFalse(ZoneUtility.newInstance("zone.test").isIpv4ReverseZone());
        assertFalse(ZoneUtility.newInstance("zone.test.").isIpv4ReverseZone());
        assertTrue(ZoneUtility.newInstance("3.2.1.in-addr.arpa").isIpv4ReverseZone());
        assertTrue(ZoneUtility.newInstance("3.2.1.in-addr.arpa.").isIpv4ReverseZone());
        assertFalse(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isIpv4ReverseZone());
        assertFalse(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isIpv4ReverseZone());
    }
                 
    @Test
    public void testIsIpv6ReverseZone() {
        assertFalse(ZoneUtility.newInstance("zone.test").isIpv6ReverseZone());
        assertFalse(ZoneUtility.newInstance("zone.test.").isIpv6ReverseZone());
        assertFalse(ZoneUtility.newInstance("3.2.1.in-addr.arpa").isIpv6ReverseZone());
        assertFalse(ZoneUtility.newInstance("3.2.1.in-addr.arpa.").isIpv6ReverseZone());
        assertTrue(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isIpv6ReverseZone());
        assertTrue(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isIpv6ReverseZone());
    }
          
    @Test
    public void testIsReverseZone() {
        assertFalse(ZoneUtility.newInstance("zone.test").isReverseZone());
        assertFalse(ZoneUtility.newInstance("zone.test.").isReverseZone());
        assertTrue(ZoneUtility.newInstance("3.2.1.in-addr.arpa").isReverseZone());
        assertTrue(ZoneUtility.newInstance("3.2.1.in-addr.arpa.").isReverseZone());
        assertTrue(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isReverseZone());
        assertTrue(ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isReverseZone());
    }
    
    @Test
    public void testRelativize() {
        assertEquals("name.anotherzone.test", ZoneUtility.newInstance("zone.test").relativize("name.anotherzone.test."));
        assertEquals("name", ZoneUtility.newInstance("zone.test").relativize("name"));
        assertEquals("name", ZoneUtility.newInstance("zone.test").relativize("name.zone.test"));
        assertEquals("name", ZoneUtility.newInstance("zone.test").relativize("name.zone.test."));
        assertEquals("name.subdomain", ZoneUtility.newInstance("zone.test").relativize("name.subdomain"));
        assertEquals("name.subdomain", ZoneUtility.newInstance("zone.test").relativize("name.subdomain.zone.test"));
        assertEquals("name.subdomain", ZoneUtility.newInstance("zone.test").relativize("name.subdomain.zone.test."));
        assertEquals("4.3.2", ZoneUtility.newInstance("1.in-addr.arpa").relativize("4.3.2"));
        assertEquals("4.3.2", ZoneUtility.newInstance("1.in-addr.arpa").relativize("4.3.2.1.in-addr.arpa"));
        assertEquals("4.3.2", ZoneUtility.newInstance("1.in-addr.arpa").relativize("4.3.2.1.in-addr.arpa."));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").relativize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").relativize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneUtility.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").relativize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa."));
    }
}
