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
import java.util.List;

/**
 * Record data.
 * 
 * @author  Ron Rickard
 */
public abstract class RecordData extends Model {
   
    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance of the record data class.
     * 
     * The data elements vary according to the record data class:
     * 
     * <table>
     *   <thead>
     *     <tr><th>Class</th><th>Element</th><th>Type</th><th>Property</th>
     *   </thead>
     *   <tbody>
     *     <tr><td>AAAARecordData</td><td>0</td><td>String</td><td>ipv6Address</td>
     *     <tr><td>ARecordData</td><td>0</td><td>String</td><td>ipAddress</td>
     *     <tr><td>CNAMERecordData</td><td>0</td><td>String</td><td>target</td>
     *     <tr><td>MXRecordData</td><td>0</td><td>String</td><td>target</td>
     *     <tr><td></td><td>1</td><td>Integer</td><td>priority</td>
     *     <tr><td>NSRecordData</td><td>0</td><td>String</td><td>target</td>
     *     <tr><td>PTRRecordData</td><td>0</td><td>String</td><td>target</td>
     *     <tr><td>SRVRecordData</td><td>0</td><td>String</td><td>target</td>
     *     <tr><td></td><td>1</td><td>Integer</td><td>port</td>
     *     <tr><td></td><td>2</td><td>Integer</td><td>weight</td>
     *     <tr><td></td><td>3</td><td>Integer</td><td>priority</td>
     *     <tr><td>TXTRecordData</td><td> 0</td><td>List<String></td><td>values</td>
     *   </tbody>
     * </table>
     * 
     * The record data elements can be null in which case the record data object will not have any initialized properties.
     * 
     * @param  recordDataClass  the class of the record data.
     * @param  dataElements     the record data elements.
     * 
     * @return  a new instance of the record data class.
     * 
     * @throws  IllegalAccessException    if the record data class is not accessible.
     * @throws  IllegalArgumentException  if the data elements are invalid for the record data class.
     * @throws  InstantiationException    if the record data class cannot be instantiated.
     * @throws  NullPointerException      if the record data class is null.
     */
    @SuppressWarnings("unchecked")
    public static <T extends RecordData> T newInstance(final Class<T> recordDataClass, final Object... dataElements) throws IllegalAccessException, InstantiationException {

        if (recordDataClass == null) {
            throw new NullPointerException("The record data class cannot be null.");
        }

        // Instantiate the record data.
        T recordData = recordDataClass.newInstance();

        // Check if there are data elements.
        if (dataElements != null) {
        
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("The data elements, " + Arrays.asList(dataElements) + ", are invalid for the record data class " + recordDataClass.getSimpleName() + ".");

            // Check if the record data class is the AAAA record data class.    
            if (AAAARecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((AAAARecordData)recordData).setIpv6Address((String)dataElements[0]);
                
            // Check if the record data class is the A record data class.
            } else if (ARecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((ARecordData)recordData).setIpAddress((String)dataElements[0]);

            // Check if the record data class is the CNAME record data class.
            } else if (CNAMERecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((CNAMERecordData)recordData).setTarget((String)dataElements[0]);

            // Check if the record data class is the MX record data class.
            } else if (MXRecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 2 || !(dataElements[0] instanceof String) || !(dataElements[1] instanceof Integer)) {
                    throw illegalArgumentException;
                }
                ((MXRecordData)recordData).setPriority((Integer)dataElements[1]);
                ((MXRecordData)recordData).setTarget((String)dataElements[0]);

            // Check if the record data class is the NS record data class.
            } else if (NSRecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((NSRecordData)recordData).setTarget((String)dataElements[0]);

            // Check if the record data class is the PTR record data class.
            } else if (PTRRecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((PTRRecordData)recordData).setTarget((String)dataElements[0]);

            // Check if the record data class is the SRV record data class.
            } else if (SRVRecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 4 || !(dataElements[0] instanceof String) || !(dataElements[1] instanceof Integer) || !(dataElements[2] instanceof Integer) || !(dataElements[3] instanceof Integer)) {
                    throw illegalArgumentException;
                }
                ((SRVRecordData)recordData).setPort((Integer)dataElements[1]);
                ((SRVRecordData)recordData).setPriority((Integer)dataElements[3]);
                ((SRVRecordData)recordData).setTarget((String)dataElements[0]);
                ((SRVRecordData)recordData).setWeight((Integer)dataElements[2]);

            // Check if the record data class is the TXT record data class.
            } else if (TXTRecordData.class.equals(recordDataClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof List)) {
                    throw illegalArgumentException;
                }
                ((TXTRecordData)recordData).setValues((List<String>)dataElements[0]);
            }
        }
        
        return recordData;
    }
}
