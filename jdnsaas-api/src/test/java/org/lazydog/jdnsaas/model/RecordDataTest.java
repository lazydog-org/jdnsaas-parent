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
        RecordData.newInstance(null, "dataelement");
    }

    
    @Test
    public void testNewInstanceA() throws Exception {
        ARecordData expectedRecordData = new ARecordData();
        expectedRecordData.setIpAddress("0.0.0.0");
        assertEquals(expectedRecordData, RecordData.newInstance(ARecordData.class, "0.0.0.0"));
    }

    @Test
    public void testNewInstanceANullDataElements() throws Exception {
        assertEquals(new ARecordData(), RecordData.newInstance(ARecordData.class, (Object[])null));
    }
            
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAExtraDataElement() throws Exception {
        RecordData.newInstance(ARecordData.class, "0.0.0.0", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAInvalidIpAddressProperty() throws Exception {
        RecordData.newInstance(ARecordData.class, new Integer(0));
    }

    
    @Test
    public void testNewInstanceAAAA() throws Exception {
        ARecordData expectedRecordData = new ARecordData();
        expectedRecordData.setIpAddress("0000:0000:0000:0000:0000:0000:0000:0000");
        assertEquals(expectedRecordData, RecordData.newInstance(ARecordData.class, "0000:0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void testNewInstanceAAAANullDataElements() throws Exception {
        assertEquals(new AAAARecordData(), RecordData.newInstance(AAAARecordData.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAAAAExtraDataElement() throws Exception {
        RecordData.newInstance(AAAARecordData.class, "0000:0000:0000:0000:0000:0000:0000:0000", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceAAAAInvalidIpv6AddressProperty() throws Exception {
        RecordData.newInstance(AAAARecordData.class, new Integer(0));
    }
      
    
    @Test
    public void testNewInstanceCNAME() throws Exception {
        CNAMERecordData expectedRecordData = new CNAMERecordData();
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, RecordData.newInstance(CNAMERecordData.class, "target"));
    }

    @Test
    public void testNewInstanceCNAMENullDataElements() throws Exception {
        assertEquals(new CNAMERecordData(), RecordData.newInstance(CNAMERecordData.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceCNAMEExtraDataElement() throws Exception {
        RecordData.newInstance(CNAMERecordData.class, "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceCNAMEInvalidTargetProperty() throws Exception {
        RecordData.newInstance(CNAMERecordData.class, new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceMX() throws Exception {
        MXRecordData expectedRecordData = new MXRecordData();
        expectedRecordData.setPriority(new Integer(0));
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, RecordData.newInstance(MXRecordData.class, "target", new Integer(0)));
    }

    @Test
    public void testNewInstanceMXNullDataElements() throws Exception {
        assertEquals(new MXRecordData(), RecordData.newInstance(MXRecordData.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXExtraDataElement() throws Exception {
        RecordData.newInstance(MXRecordData.class, "target", new Integer(0), "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXInvalidTargetProperty() throws Exception {
        RecordData.newInstance(MXRecordData.class, new Integer(0), new Integer(0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceMXInvalidPriorityProperty() throws Exception {
        RecordData.newInstance(MXRecordData.class, "target", "priority");
    }
    
    
    @Test
    public void testNewInstanceNS() throws Exception {
        NSRecordData expectedRecordData = new NSRecordData();
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, RecordData.newInstance(NSRecordData.class, "target"));
    }

    @Test
    public void testNewInstanceNSNullDataElements() throws Exception {
        assertEquals(new NSRecordData(), RecordData.newInstance(NSRecordData.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceNSExtraDataElement() throws Exception {
        RecordData.newInstance(NSRecordData.class, "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceNSInvalidTargetProperty() throws Exception {
        RecordData.newInstance(NSRecordData.class, new Integer(0));
    }
    
    
    @Test
    public void testNewInstancePTR() throws Exception {
        PTRRecordData expectedRecordData = new PTRRecordData();
        expectedRecordData.setTarget("target");
        assertEquals(expectedRecordData, RecordData.newInstance(PTRRecordData.class, "target"));
    }

    @Test
    public void testNewInstancePTRNullDataElements() throws Exception {
        assertEquals(new PTRRecordData(), RecordData.newInstance(PTRRecordData.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstancePTRExtraDataElement() throws Exception {
        RecordData.newInstance(PTRRecordData.class, "target", "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstancePTRInvalidTargetProperty() throws Exception {
        RecordData.newInstance(PTRRecordData.class, new Integer(0));
    }
    
    
    @Test
    public void testNewInstanceSRV() throws Exception {
        SRVRecordData expectedRecordData = new SRVRecordData();
        expectedRecordData.setPort(new Integer(0));
        expectedRecordData.setPriority(new Integer(2));
        expectedRecordData.setTarget("target");
        expectedRecordData.setWeight(new Integer(1));
        assertEquals(expectedRecordData, RecordData.newInstance(SRVRecordData.class, "target", new Integer(0), new Integer(1), new Integer(2)));
    }

    @Test
    public void testNewInstanceSRVNullDataElements() throws Exception {
        assertEquals(new SRVRecordData(), RecordData.newInstance(SRVRecordData.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVExtraDataElement() throws Exception {
        RecordData.newInstance(SRVRecordData.class, "target", new Integer(0), new Integer(1), new Integer(2), "extra");
    }
           
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidTargetProperty() throws Exception {
        RecordData.newInstance(SRVRecordData.class, new Integer(3), new Integer(0), new Integer(1), new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidPortProperty() throws Exception {
        RecordData.newInstance(SRVRecordData.class, "target", "port", new Integer(1), new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidWeightProperty() throws Exception {
        RecordData.newInstance(SRVRecordData.class, "target", new Integer(0), "weight", new Integer(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceSRVInvalidPriorityProperty() throws Exception {
        RecordData.newInstance(SRVRecordData.class, "target", new Integer(0), new Integer(1), "priority");
    }

    
    @Test
    public void testNewInstanceTXT() throws Exception {
        TXTRecordData expectedRecordData = new TXTRecordData();
        expectedRecordData.setValues(Arrays.asList("value"));
        assertEquals(expectedRecordData, RecordData.newInstance(TXTRecordData.class, Arrays.asList("value")));
    }

    @Test
    public void testNewInstanceTXTNullDataElements() throws Exception {
        assertEquals(new TXTRecordData(), RecordData.newInstance(TXTRecordData.class, (Object[])null));
    }
             
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceTXTExtraDataElement() throws Exception {
        RecordData.newInstance(TXTRecordData.class, Arrays.asList("value"), "extra");
    }
                
    @Test(expected=IllegalArgumentException.class)
    public void testNewInstanceTXTInvalidValuesProperty() throws Exception {
        RecordData.newInstance(TXTRecordData.class, new Integer(0));
    }
}
