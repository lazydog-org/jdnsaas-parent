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
 * Zone resolver test.
 * 
 * @author  Ron Rickard
 */
public class ZoneResolverTest {
    
    @Test
    public void testAbsolutize() {
        assertEquals("name.anotherzone.test.", ZoneResolver.newInstance("zone.test").absolutize("name.anotherzone.test."));
        assertEquals("name.zone.test.", ZoneResolver.newInstance("zone.test").absolutize("name"));
        assertEquals("name.zone.test.", ZoneResolver.newInstance("zone.test").absolutize("name.zone.test"));
        assertEquals("name.zone.test.", ZoneResolver.newInstance("zone.test").absolutize("name.zone.test."));
        assertEquals("name.subdomain.zone.test.", ZoneResolver.newInstance("zone.test").absolutize("name.subdomain"));
        assertEquals("name.subdomain.zone.test.", ZoneResolver.newInstance("zone.test").absolutize("name.subdomain.zone.test"));
        assertEquals("name.subdomain.zone.test.", ZoneResolver.newInstance("zone.test").absolutize("name.subdomain.zone.test."));
        assertEquals("4.3.2.1.in-addr.arpa.", ZoneResolver.newInstance("1.in-addr.arpa").absolutize("4.3.2"));
        assertEquals("4.3.2.1.in-addr.arpa.", ZoneResolver.newInstance("1.in-addr.arpa").absolutize("4.3.2.1.in-addr.arpa"));
        assertEquals("4.3.2.1.in-addr.arpa.", ZoneResolver.newInstance("1.in-addr.arpa").absolutize("4.3.2.1.in-addr.arpa."));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").absolutize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").absolutize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").absolutize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa."));
    }
    
    @Test
    public void testGetAbsoluteZoneName() {
        assertEquals("zone.test.", ZoneResolver.newInstance("zone.test").getAbsoluteZoneName());
        assertEquals("zone.test.", ZoneResolver.newInstance("zone.test.").getAbsoluteZoneName());
        assertEquals("3.2.1.in-addr.arpa.", ZoneResolver.newInstance("3.2.1.in-addr.arpa").getAbsoluteZoneName());
        assertEquals("3.2.1.in-addr.arpa.", ZoneResolver.newInstance("3.2.1.in-addr.arpa.").getAbsoluteZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getAbsoluteZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").getAbsoluteZoneName());
    }
    
    @Test
    public void testGetIpAddress() {
        assertEquals("1.2.3.4", ZoneResolver.newInstance("3.2.1.in-addr.arpa").getIpAddress("4"));
        assertEquals("1.2.3.4", ZoneResolver.newInstance("2.1.in-addr.arpa").getIpAddress("4.3"));
        assertEquals("1.2.3.4", ZoneResolver.newInstance("1.in-addr.arpa").getIpAddress("4.3.2"));
        assertEquals("0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f.0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getIpAddress("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0"));
    }
    
    @Test
    public void testGetRelativeZoneName() {
        assertEquals("zone.test", ZoneResolver.newInstance("zone.test").getRelativeZoneName());
        assertEquals("zone.test", ZoneResolver.newInstance("zone.test.").getRelativeZoneName());
        assertEquals("3.2.1.in-addr.arpa", ZoneResolver.newInstance("3.2.1.in-addr.arpa").getRelativeZoneName());
        assertEquals("3.2.1.in-addr.arpa", ZoneResolver.newInstance("3.2.1.in-addr.arpa.").getRelativeZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getRelativeZoneName());
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").getRelativeZoneName());
    }
        
    @Test
    public void testGetReverseTets() {
        assertEquals("4", ZoneResolver.newInstance("3.2.1.in-addr.arpa").getReverseTets("1.2.3.4"));
        assertEquals("4.3", ZoneResolver.newInstance("2.1.in-addr.arpa").getReverseTets("1.2.3.4"));
        assertEquals("4.3.2", ZoneResolver.newInstance("1.in-addr.arpa").getReverseTets("1.2.3.4"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").getReverseTets("0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f.0.1.2.3.4.5.6.7.8.9.a.b.c.d.e.f"));
    }
            
    @Test
    public void testIsForwardZone() {
        assertTrue(ZoneResolver.newInstance("zone.test").isForwardZone());
        assertTrue(ZoneResolver.newInstance("zone.test.").isForwardZone());
        assertFalse(ZoneResolver.newInstance("3.2.1.in-addr.arpa").isForwardZone());
        assertFalse(ZoneResolver.newInstance("3.2.1.in-addr.arpa.").isForwardZone());
        assertFalse(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isForwardZone());
        assertFalse(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isForwardZone());
    }
                        
    @Test
    public void testIsIpv4ReverseZone() {
        assertFalse(ZoneResolver.newInstance("zone.test").isIpv4ReverseZone());
        assertFalse(ZoneResolver.newInstance("zone.test.").isIpv4ReverseZone());
        assertTrue(ZoneResolver.newInstance("3.2.1.in-addr.arpa").isIpv4ReverseZone());
        assertTrue(ZoneResolver.newInstance("3.2.1.in-addr.arpa.").isIpv4ReverseZone());
        assertFalse(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isIpv4ReverseZone());
        assertFalse(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isIpv4ReverseZone());
    }
                 
    @Test
    public void testIsIpv6ReverseZone() {
        assertFalse(ZoneResolver.newInstance("zone.test").isIpv6ReverseZone());
        assertFalse(ZoneResolver.newInstance("zone.test.").isIpv6ReverseZone());
        assertFalse(ZoneResolver.newInstance("3.2.1.in-addr.arpa").isIpv6ReverseZone());
        assertFalse(ZoneResolver.newInstance("3.2.1.in-addr.arpa.").isIpv6ReverseZone());
        assertTrue(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isIpv6ReverseZone());
        assertTrue(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isIpv6ReverseZone());
    }
          
    @Test
    public void testIsReverseZone() {
        assertFalse(ZoneResolver.newInstance("zone.test").isReverseZone());
        assertFalse(ZoneResolver.newInstance("zone.test.").isReverseZone());
        assertTrue(ZoneResolver.newInstance("3.2.1.in-addr.arpa").isReverseZone());
        assertTrue(ZoneResolver.newInstance("3.2.1.in-addr.arpa.").isReverseZone());
        assertTrue(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").isReverseZone());
        assertTrue(ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa.").isReverseZone());
    }
    
    @Test
    public void testRelativize() {
        assertEquals("name.anotherzone.test", ZoneResolver.newInstance("zone.test").relativize("name.anotherzone.test."));
        assertEquals("name", ZoneResolver.newInstance("zone.test").relativize("name"));
        assertEquals("name", ZoneResolver.newInstance("zone.test").relativize("name.zone.test"));
        assertEquals("name", ZoneResolver.newInstance("zone.test").relativize("name.zone.test."));
        assertEquals("name.subdomain", ZoneResolver.newInstance("zone.test").relativize("name.subdomain"));
        assertEquals("name.subdomain", ZoneResolver.newInstance("zone.test").relativize("name.subdomain.zone.test"));
        assertEquals("name.subdomain", ZoneResolver.newInstance("zone.test").relativize("name.subdomain.zone.test."));
        assertEquals("4.3.2", ZoneResolver.newInstance("1.in-addr.arpa").relativize("4.3.2"));
        assertEquals("4.3.2", ZoneResolver.newInstance("1.in-addr.arpa").relativize("4.3.2.1.in-addr.arpa"));
        assertEquals("4.3.2", ZoneResolver.newInstance("1.in-addr.arpa").relativize("4.3.2.1.in-addr.arpa."));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").relativize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").relativize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa"));
        assertEquals("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0", ZoneResolver.newInstance("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa").relativize("f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.f.e.d.c.b.a.9.8.7.6.5.4.3.2.1.0.ip6.arpa."));
    }
}
