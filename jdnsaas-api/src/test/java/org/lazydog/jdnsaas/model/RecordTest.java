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
        Record.<Record,Record.Data>newInstance(null, "name", new Long(0), (Object[])null);
    }
     
    
    @Test
    public void testNewInstanceAData() throws Exception {
        ARecord.Data recordData = new ARecord.Data();
        recordData.setIpAddress("0.0.0.0");
        ARecord expectedRecord = new ARecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(ARecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceADataElements() throws Exception {
        ARecord.Data recordData = new ARecord.Data();
        recordData.setIpAddress("0.0.0.0");
        ARecord expectedRecord = new ARecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(ARecord.class, "name", new Long(0), "0.0.0.0"));
    }
        
    @Test
    public void testNewInstanceANullData() throws Exception {
        ARecord expectedRecord = new ARecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(ARecord.class, "name", new Long(0), (ARecord.Data)null));
    }
    
    @Test
    public void testNewInstanceANullDataElements() throws Exception {
        ARecord expectedRecord = new ARecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(ARecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceANullsData() throws Exception {
        assertEquals(new ARecord(), Record.newInstance(ARecord.class, null, null, (ARecord.Data)null));
    }
    
    @Test
    public void testNewInstanceANullsDataElements() throws Exception {
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
    public void testNewInstanceAAAAData() throws Exception {
        AAAARecord.Data recordData = new AAAARecord.Data();
        recordData.setIpv6Address("0000:0000:0000:0000:0000:0000:0000:0000");
        AAAARecord expectedRecord = new AAAARecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(AAAARecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceAAAADataElements() throws Exception {
        AAAARecord.Data recordData = new AAAARecord.Data();
        recordData.setIpv6Address("0000:0000:0000:0000:0000:0000:0000:0000");
        AAAARecord expectedRecord = new AAAARecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(AAAARecord.class, "name", new Long(0), "0000:0000:0000:0000:0000:0000:0000:0000"));
    }
        
    @Test
    public void testNewInstanceAAAANullData() throws Exception {
        AAAARecord expectedRecord = new AAAARecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(AAAARecord.class, "name", new Long(0), (AAAARecord.Data)null));
    }
    
    @Test
    public void testNewInstanceAAAANullDataElements() throws Exception {
        AAAARecord expectedRecord = new AAAARecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(AAAARecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceAAAANullsData() throws Exception {
        assertEquals(new AAAARecord(), Record.newInstance(AAAARecord.class, null, null, (AAAARecord.Data)null));
    }
    
    @Test
    public void testNewInstanceAAAANullsDataElements() throws Exception {
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
    public void testNewInstanceCNAMEData() throws Exception {
        CNAMERecord.Data recordData = new CNAMERecord.Data();
        recordData.setTarget("target");
        CNAMERecord expectedRecord = new CNAMERecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(CNAMERecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceCNAMEDataElements() throws Exception {
        CNAMERecord.Data recordData = new CNAMERecord.Data();
        recordData.setTarget("target");
        CNAMERecord expectedRecord = new CNAMERecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(CNAMERecord.class, "name", new Long(0), "target"));
    }
        
    @Test
    public void testNewInstanceCNAMENullData() throws Exception {
        CNAMERecord expectedRecord = new CNAMERecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(CNAMERecord.class, "name", new Long(0), (CNAMERecord.Data)null));
    }
    
    @Test
    public void testNewInstanceCNAMENullDataElements() throws Exception {
        CNAMERecord expectedRecord = new CNAMERecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(CNAMERecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceCNAMENullsData() throws Exception {
        assertEquals(new CNAMERecord(), Record.newInstance(CNAMERecord.class, null, null, (CNAMERecord.Data)null));
    }
    
    @Test
    public void testNewInstanceCNAMENullsDataElements() throws Exception {
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
    public void testNewInstanceMXData() throws Exception {
        MXRecord.Data recordData = new MXRecord.Data();
        recordData.setPriority(new Integer(0));
        recordData.setTarget("target");
        MXRecord expectedRecord = new MXRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(MXRecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceMXDataElements() throws Exception {
        MXRecord.Data recordData = new MXRecord.Data();
        recordData.setPriority(new Integer(0));
        recordData.setTarget("target");
        MXRecord expectedRecord = new MXRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(MXRecord.class, "name", new Long(0), "target", new Integer(0)));
    }
        
    @Test
    public void testNewInstanceMXNullData() throws Exception {
        MXRecord expectedRecord = new MXRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(MXRecord.class, "name", new Long(0), (MXRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceMXNullDataElements() throws Exception {
        MXRecord expectedRecord = new MXRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(MXRecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceMXNullsData() throws Exception {
        assertEquals(new MXRecord(), Record.newInstance(MXRecord.class, null, null, (MXRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceMXNullsDataElements() throws Exception {
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
    public void testNewInstanceNSData() throws Exception {
        NSRecord.Data recordData = new NSRecord.Data();
        recordData.setTarget("target");
        NSRecord expectedRecord = new NSRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(NSRecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceNSDataElements() throws Exception {
        NSRecord.Data recordData = new NSRecord.Data();
        recordData.setTarget("target");
        NSRecord expectedRecord = new NSRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(NSRecord.class, "name", new Long(0), "target"));
    }
        
    @Test
    public void testNewInstanceNSNullData() throws Exception {
        NSRecord expectedRecord = new NSRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(NSRecord.class, "name", new Long(0), (NSRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceNSNullDataElements() throws Exception {
        NSRecord expectedRecord = new NSRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(NSRecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceNSNullsData() throws Exception {
        assertEquals(new NSRecord(), Record.newInstance(NSRecord.class, null, null, (NSRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceNSNullsDataElements() throws Exception {
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
    public void testNewInstancePTRData() throws Exception {
        PTRRecord.Data recordData = new PTRRecord.Data();
        recordData.setTarget("target");
        PTRRecord expectedRecord = new PTRRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(PTRRecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstancePTRDataElements() throws Exception {
        PTRRecord.Data recordData = new PTRRecord.Data();
        recordData.setTarget("target");
        PTRRecord expectedRecord = new PTRRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(PTRRecord.class, "name", new Long(0), "target"));
    }
        
    @Test
    public void testNewInstancePTRNullData() throws Exception {
        PTRRecord expectedRecord = new PTRRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(PTRRecord.class, "name", new Long(0), (PTRRecord.Data)null));
    }
    
    @Test
    public void testNewInstancePTRNullDataElements() throws Exception {
        PTRRecord expectedRecord = new PTRRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(PTRRecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstancePTRNullsData() throws Exception {
        assertEquals(new PTRRecord(), Record.newInstance(PTRRecord.class, null, null, (PTRRecord.Data)null));
    }
    
    @Test
    public void testNewInstancePTRNullsDataElements() throws Exception {
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
    public void testNewInstanceSOAData() throws Exception {
        SOARecord.Data recordData = new SOARecord.Data();
        recordData.setEmailAddress("emailAddress");
        recordData.setExpireInterval(new Long(1));
        recordData.setMasterNameServer("masterNameServer");
        recordData.setMinimumTimeToLive(new Long(0));
        recordData.setRefreshInterval(new Long(3));
        recordData.setRetryInterval(new Long(2));
        recordData.setSerialNumber(new Long(4));
        SOARecord expectedRecord = new SOARecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SOARecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceSOADataElements() throws Exception {
        SOARecord.Data recordData = new SOARecord.Data();
        recordData.setEmailAddress("emailAddress");
        recordData.setExpireInterval(new Long(1));
        recordData.setMasterNameServer("masterNameServer");
        recordData.setMinimumTimeToLive(new Long(0));
        recordData.setRefreshInterval(new Long(3));
        recordData.setRetryInterval(new Long(2));
        recordData.setSerialNumber(new Long(4));
        SOARecord expectedRecord = new SOARecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SOARecord.class, "name", new Long(0), new Long(0), new Long(1), new Long(2), new Long(3), new Long(4), "emailAddress", "masterNameServer"));
    }
        
    @Test
    public void testNewInstanceSOANullData() throws Exception {
        SOARecord expectedRecord = new SOARecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SOARecord.class, "name", new Long(0), (SOARecord.Data)null));
    }
    
    @Test
    public void testNewInstanceSOANullDataElements() throws Exception {
        SOARecord expectedRecord = new SOARecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SOARecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceSOANullsData() throws Exception {
        assertEquals(new SOARecord(), Record.newInstance(SOARecord.class, null, null, (SOARecord.Data)null));
    }
    
    @Test
    public void testNewInstanceSOANullsDataElements() throws Exception {
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
    public void testNewInstanceSRVData() throws Exception {
        SRVRecord.Data recordData = new SRVRecord.Data();
        recordData.setPort(new Integer(0));
        recordData.setPriority(new Integer(2));
        recordData.setTarget("target");
        recordData.setWeight(new Integer(1));
        SRVRecord expectedRecord = new SRVRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SRVRecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceSRVDataElements() throws Exception {
        SRVRecord.Data recordData = new SRVRecord.Data();
        recordData.setPort(new Integer(0));
        recordData.setPriority(new Integer(2));
        recordData.setTarget("target");
        recordData.setWeight(new Integer(1));
        SRVRecord expectedRecord = new SRVRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SRVRecord.class, "name", new Long(0), "target", new Integer(0), new Integer(1), new Integer(2)));
    }
        
    @Test
    public void testNewInstanceSRVNullData() throws Exception {
        SRVRecord expectedRecord = new SRVRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SRVRecord.class, "name", new Long(0), (SRVRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceSRVNullDataElements() throws Exception {
        SRVRecord expectedRecord = new SRVRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(SRVRecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceSRVNullsData() throws Exception {
        assertEquals(new SRVRecord(), Record.newInstance(SRVRecord.class, null, null, (SRVRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceSRVNullsDataElements() throws Exception {
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
    public void testNewInstanceTXTData() throws Exception {
        TXTRecord.Data recordData = new TXTRecord.Data();
        recordData.setValues(Arrays.asList("value"));
        TXTRecord expectedRecord = new TXTRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(TXTRecord.class, "name", new Long(0), recordData));
    }
     
    @Test
    public void testNewInstanceTXTDataElements() throws Exception {
        TXTRecord.Data recordData = new TXTRecord.Data();
        recordData.setValues(Arrays.asList("value"));
        TXTRecord expectedRecord = new TXTRecord();
        expectedRecord.setData(recordData);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(TXTRecord.class, "name", new Long(0), Arrays.asList("value")));
    }
        
    @Test
    public void testNewInstanceTXTNullData() throws Exception {
        TXTRecord expectedRecord = new TXTRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(TXTRecord.class, "name", new Long(0), (TXTRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceTXTNullDataElements() throws Exception {
        TXTRecord expectedRecord = new TXTRecord();
        expectedRecord.setData(null);
        expectedRecord.setName("name");
        expectedRecord.setTimeToLive(new Long(0));
        assertEquals(expectedRecord, Record.newInstance(TXTRecord.class, "name", new Long(0), (Object[])null));
    }
    
    @Test
    public void testNewInstanceTXTNullsData() throws Exception {
        assertEquals(new TXTRecord(), Record.newInstance(TXTRecord.class, null, null, (TXTRecord.Data)null));
    }
    
    @Test
    public void testNewInstanceTXTNullsDataElements() throws Exception {
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
