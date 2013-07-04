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
 * Record test.
 * 
 * @author  Ron Rickard
 */
public class RecordTest {
    
    @Test(expected=NullPointerException.class)
    @SuppressWarnings("unchecked")
    public void testNewInstanceNullRecordClass() throws Exception {
        Record.<Record>newInstance(null, "name", new Long(0), (Object[])null);
    }
     
    
    @Test
    public void testNewInstanceA() throws Exception {
        ARecord expectedRecord = new ARecord();
        expectedRecord.setIpAddress("0.0.0.0");
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(ARecord.class, "name", new Long(0), "0.0.0.0"));
    }

    @Test
    public void testNewInstanceANulls() throws Exception {
        assertEquals(new ARecord(), Record.newInstance(ARecord.class, null, null, (Object[])null));
    }
       
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAExtraDataElement() throws Exception {
        Record.newInstance(ARecord.class, "name", new Long(0), "0.0.0.0", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAInvalidIpAddressProperty() throws Exception {
        Record.newInstance(ARecord.class, "name", new Long(0), new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceAAAA() throws Exception {
        AAAARecord expectedRecord = new AAAARecord();
        expectedRecord.setIpv6Address("0000:0000:0000:0000:0000:0000:0000:0000");
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(AAAARecord.class, "name", new Long(0), "0000:0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void testNewInstanceAAAANulls() throws Exception {
        assertEquals(new AAAARecord(), Record.newInstance(AAAARecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAAAAExtraDataElement() throws Exception {
        Record.newInstance(AAAARecord.class, "name", new Long(0), "0000:0000:0000:0000:0000:0000:0000:0000", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAAAAInvalidIpv6AddressProperty() throws Exception {
        Record.newInstance(AAAARecord.class, "name", new Long(0), new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceCNAME() throws Exception {
        CNAMERecord expectedRecord = new CNAMERecord();
        expectedRecord.setName("name");
        expectedRecord.setTarget("target");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(CNAMERecord.class, "name", new Long(0), "target"));
    }

    @Test
    public void testNewInstanceCNAMENulls() throws Exception {
        assertEquals(new CNAMERecord(), Record.newInstance(CNAMERecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceCNAMEExtraDataElement() throws Exception {
        Record.newInstance(CNAMERecord.class, "name", new Long(0), "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceCNAMEInvalidTargetProperty() throws Exception {
        Record.newInstance(CNAMERecord.class, "name", new Long(0), new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceMX() throws Exception {
        MXRecord expectedRecord = new MXRecord();
        expectedRecord.setName("name");
        expectedRecord.setPriority(new Integer(0));
        expectedRecord.setTarget("target");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(MXRecord.class, "name", new Long(0), "target", new Integer(0)));
    }

    @Test
    public void testNewInstanceMXNulls() throws Exception {
        assertEquals(new MXRecord(), Record.newInstance(MXRecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXExtraDataElement() throws Exception {
        Record.newInstance(MXRecord.class, "name", new Long(0), "target", new Integer(0), "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXInvalidTargetProperty() throws Exception {
        Record.newInstance(MXRecord.class, "name", new Long(0), new Integer(0), new Integer(0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXInvalidPriorityProperty() throws Exception {
        Record.newInstance(MXRecord.class, "name", new Long(0), new Integer(0), "priority");
    }
    
    
    @Test
    public void testNewInstanceNS() throws Exception {
        NSRecord expectedRecord = new NSRecord();
        expectedRecord.setName("name");
        expectedRecord.setTarget("target");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(NSRecord.class, "name", new Long(0), "target"));
    }

    @Test
    public void testNewInstanceNSNulls() throws Exception {
        assertEquals(new NSRecord(), Record.newInstance(NSRecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceNSExtraDataElement() throws Exception {
        Record.newInstance(NSRecord.class, "name", new Long(0), "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceNSInvalidTargetProperty() throws Exception {
        Record.newInstance(NSRecord.class, "name", new Long(0), new Integer(0));
    }
    
    
    @Test
    public void testNewInstancePTR() throws Exception {
        PTRRecord expectedRecord = new PTRRecord();
        expectedRecord.setName("name");
        expectedRecord.setTarget("target");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(PTRRecord.class, "name", new Long(0), "target"));
    }

    @Test
    public void testNewInstancePTRNulls() throws Exception {
        assertEquals(new PTRRecord(), Record.newInstance(PTRRecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstancePTRExtraDataElement() throws Exception {
        Record.newInstance(PTRRecord.class, "name", new Long(0), "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstancePTRInvalidTargetProperty() throws Exception {
        Record.newInstance(PTRRecord.class, "name", new Long(0), new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceSOA() throws Exception {
        SOARecord expectedRecord = new SOARecord();
        expectedRecord.setEmailAddress("emailAddress");
        expectedRecord.setExpireInterval(new Long(1));
        expectedRecord.setMasterNameServer("masterNameServer");
        expectedRecord.setMinimumTimeToLive(new Long(0));
        expectedRecord.setName("name");
        expectedRecord.setRefreshInterval(new Long(3));
        expectedRecord.setRetryInterval(new Long(2));
        expectedRecord.setSerialNumber(new Long(4));
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), new Long(2), new Long(3), new Long(4), "emailAddress", "masterNameServer"));
    }

    @Test
    public void testNewInstanceSOANulls() throws Exception {
        assertEquals(new SOARecord(), Record.newInstance(SOARecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAExtraDataElement() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), new Long(2), new Long(3), new Long(4), "emailAddress", "masterNameServer", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAInvalidMinumumTimeToLiveProperty() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), "minimumTimeToLive", new Long(1), new Long(2), new Long(3), new Long(4), "emailAddress", "masterNameServer");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAInvalidExpireIntervalProperty() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), "expireInterval", new Long(2), new Long(3), new Long(4), "emailAddress", "masterNameServer");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAInvalidRetryIntervalProperty() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), "retryInterval", new Long(3), new Long(4), "emailAddress", "masterNameServer");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAInvalidRefreshIntervalProperty() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), new Long(2), "refreshInterval", new Long(4), "emailAddress", "masterNameServer");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAInvalidSerialNumberProperty() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), new Long(2), new Long(3), "serialNumber", "emailAddress", "masterNameServer");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAInvalidEmailAddressProperty() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), new Long(2), new Long(3), new Long(4), new Integer(5), "masterNameServer");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSOAInvalidMasterNameServerProperty() throws Exception {
        Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), new Long(2), new Long(3), new Long(4), "emailAddress", new Integer(6));
    }
    
    
    @Test
    public void testNewInstanceSRV() throws Exception {
        SRVRecord expectedRecord = new SRVRecord();
        expectedRecord.setName("name");
        expectedRecord.setPort(new Integer(0));
        expectedRecord.setPriority(new Integer(2));
        expectedRecord.setTarget("target");
        expectedRecord.setTimeToLive(new Long(0));
        expectedRecord.setWeight(new Integer(1));
        assertEquals(expectedRecord, Record.newInstance(SRVRecord.class, "name", new Long(0), "target", new Integer(0), new Integer(1), new Integer(2)));
    }

    @Test
    public void testNewInstanceSRVNulls() throws Exception {
        assertEquals(new SRVRecord(), Record.newInstance(SRVRecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVExtraDataElement() throws Exception {
        Record.newInstance(SRVRecord.class, "name", new Long(0), "target", new Integer(0), new Integer(1), new Integer(2), "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidTargetProperty() throws Exception {
        Record.newInstance(SRVRecord.class, "name", new Long(0), new Integer(3), new Integer(0), new Integer(1), new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidPortProperty() throws Exception {
        Record.newInstance(SRVRecord.class, "name", new Long(0), "target", "port", new Integer(1), new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidWeightProperty() throws Exception {
        Record.newInstance(SRVRecord.class, "name", new Long(0), "target", new Integer(0), "weight", new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidPriorityProperty() throws Exception {
        Record.newInstance(SRVRecord.class, "name", new Long(0), "target", new Integer(0), new Integer(1), "priority");
    }
    
    
    @Test
    public void testNewInstanceTXT() throws Exception {
        TXTRecord expectedRecord = new TXTRecord();
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        expectedRecord.setValues(Arrays.asList("value"));
        assertEquals(expectedRecord, Record.newInstance(TXTRecord.class, "name", new Long(0), Arrays.asList("value")));
    }

    @Test
    public void testNewInstanceTXTNulls() throws Exception {
        assertEquals(new TXTRecord(), Record.newInstance(TXTRecord.class, null, null, (Object[])null));
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceTXTExtraDataElement() throws Exception {
        Record.newInstance(TXTRecord.class, "name", new Long(0), Arrays.asList("value"), "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceTXTInvalidValuesProperty() throws Exception {
        Record.newInstance(TXTRecord.class, "name", new Long(0), new Integer(0));
    }
}
