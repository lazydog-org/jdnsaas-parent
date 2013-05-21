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
 * Record data test.
 *
 * @author  Ron Rickard
 */
public class RecordDataTest {
    
    @Test(expected=NullPointerException.class) 
    public void testNewInstanceNullRecordDataClass() throws Exception {
        Record.Data.newInstance(null, "dataelement");
    }

    
    @Test
    public void testNewInstanceA() throws Exception {
        ARecord.Data expectedRecordData = new ARecord.Data();
        expectedRecordData.setIpAddress("0.0.0.0");
        assertEquals(expectedRecordData, Record.Data.newInstance(ARecord.Data.class, "0.0.0.0"));
    }

    @Test
    public void testNewInstanceANullDataElements() throws Exception {
        assertEquals(new ARecord.Data(), Record.Data.newInstance(ARecord.Data.class, (Object[])null));
    }
            
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAExtraDataElement() throws Exception {
        Record.Data.newInstance(ARecord.Data.class, "0.0.0.0", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAInvalidIpAddressProperty() throws Exception {
        Record.Data.newInstance(ARecord.Data.class, new Integer(0));
    }

    
    @Test
    public void testNewInstanceAAAA() throws Exception {
        ARecord.Data expectedRecordData = new ARecord.Data();
        expectedRecordData.setIpAddress("0000:0000:0000:0000:0000:0000:0000:0000");
        assertEquals(expectedRecordData, Record.Data.newInstance(ARecord.Data.class, "0000:0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void testNewInstanceAAAANullDataElements() throws Exception {
        assertEquals(new AAAARecord.Data(), Record.Data.newInstance(AAAARecord.Data.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAAAAExtraDataElement() throws Exception {
        Record.Data.newInstance(AAAARecord.Data.class, "0000:0000:0000:0000:0000:0000:0000:0000", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAAAAInvalidIpv6AddressProperty() throws Exception {
        Record.Data.newInstance(AAAARecord.Data.class, new Integer(0));
    }
      
    
    @Test
    public void testNewInstanceCNAME() throws Exception {
        CNAMERecord.Data expectedRecordData = new CNAMERecord.Data();
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, Record.Data.newInstance(CNAMERecord.Data.class, "target"));
    }

    @Test
    public void testNewInstanceCNAMENullDataElements() throws Exception {
        assertEquals(new CNAMERecord.Data(), Record.Data.newInstance(CNAMERecord.Data.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceCNAMEExtraDataElement() throws Exception {
        Record.Data.newInstance(CNAMERecord.Data.class, "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceCNAMEInvalidTargetProperty() throws Exception {
        Record.Data.newInstance(CNAMERecord.Data.class, new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceMX() throws Exception {
        MXRecord.Data expectedRecordData = new MXRecord.Data();
        expectedRecordData.setPriority(new Integer(0));
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, Record.Data.newInstance(MXRecord.Data.class, "target", new Integer(0)));
    }

    @Test
    public void testNewInstanceMXNullDataElements() throws Exception {
        assertEquals(new MXRecord.Data(), Record.Data.newInstance(MXRecord.Data.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXExtraDataElement() throws Exception {
        Record.Data.newInstance(MXRecord.Data.class, "target", new Integer(0), "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXInvalidTargetProperty() throws Exception {
        Record.Data.newInstance(MXRecord.Data.class, new Integer(0), new Integer(0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXInvalidPriorityProperty() throws Exception {
        Record.Data.newInstance(MXRecord.Data.class, "target", "priority");
    }
    
    
    @Test
    public void testNewInstanceNS() throws Exception {
        NSRecord.Data expectedRecordData = new NSRecord.Data();
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, Record.Data.newInstance(NSRecord.Data.class, "target"));
    }

    @Test
    public void testNewInstanceNSNullDataElements() throws Exception {
        assertEquals(new NSRecord.Data(), Record.Data.newInstance(NSRecord.Data.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceNSExtraDataElement() throws Exception {
        Record.Data.newInstance(NSRecord.Data.class, "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceNSInvalidTargetProperty() throws Exception {
        Record.Data.newInstance(NSRecord.Data.class, new Integer(0));
    }
    
    
    @Test
    public void testNewInstancePTR() throws Exception {
        PTRRecord.Data expectedRecordData = new PTRRecord.Data();
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, Record.Data.newInstance(PTRRecord.Data.class, "target"));
    }

    @Test
    public void testNewInstancePTRNullDataElements() throws Exception {
        assertEquals(new PTRRecord.Data(), Record.Data.newInstance(PTRRecord.Data.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstancePTRExtraDataElement() throws Exception {
        Record.Data.newInstance(PTRRecord.Data.class, "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstancePTRInvalidTargetProperty() throws Exception {
        Record.Data.newInstance(PTRRecord.Data.class, new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceSRV() throws Exception {
        SRVRecord.Data expectedRecordData = new SRVRecord.Data();
        expectedRecordData.setPort(new Integer(0));
        expectedRecordData.setPriority(new Integer(2));
        expectedRecordData.setTarget("target");
        expectedRecordData.setWeight(new Integer(1));
        assertEquals(expectedRecordData, Record.Data.newInstance(SRVRecord.Data.class, "target", new Integer(0), new Integer(1), new Integer(2)));
    }

    @Test
    public void testNewInstanceSRVNullDataElements() throws Exception {
        assertEquals(new SRVRecord.Data(), Record.Data.newInstance(SRVRecord.Data.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVExtraDataElement() throws Exception {
        Record.Data.newInstance(SRVRecord.Data.class, "target", new Integer(0), new Integer(1), new Integer(2), "extra");
    }
           
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidTargetProperty() throws Exception {
        Record.Data.newInstance(SRVRecord.Data.class, new Integer(3), new Integer(0), new Integer(1), new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidPortProperty() throws Exception {
        Record.Data.newInstance(SRVRecord.Data.class, "target", "port", new Integer(1), new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidWeightProperty() throws Exception {
        Record.Data.newInstance(SRVRecord.Data.class, "target", new Integer(0), "weight", new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidPriorityProperty() throws Exception {
        Record.Data.newInstance(SRVRecord.Data.class, "target", new Integer(0), new Integer(1), "priority");
    }

    
    @Test
    public void testNewInstanceTXT() throws Exception {
        TXTRecord.Data expectedRecordData = new TXTRecord.Data();
        expectedRecordData.setValues(Arrays.asList("value"));
        assertEquals(expectedRecordData, Record.Data.newInstance(TXTRecord.Data.class, Arrays.asList("value")));
    }

    @Test
    public void testNewInstanceTXTNullDataElements() throws Exception {
        assertEquals(new TXTRecord.Data(), Record.Data.newInstance(TXTRecord.Data.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceTXTExtraDataElement() throws Exception {
        Record.Data.newInstance(TXTRecord.Data.class, Arrays.asList("value"), "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceTXTInvalidValuesProperty() throws Exception {
        Record.Data.newInstance(TXTRecord.Data.class, new Integer(0));
    }
}
