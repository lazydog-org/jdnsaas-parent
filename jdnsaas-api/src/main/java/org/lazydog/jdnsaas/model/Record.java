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
 * Record.
 * 
 * @author  Ron Rickard
 */
public abstract class Record extends Model {
        
    private static final long serialVersionUID = 1L;
    public static final long DEFAULT_TIME_TO_LIVE = 300L;
    private String name;
    private RecordOperation operation;
    private Long timeToLive = new Long(DEFAULT_TIME_TO_LIVE);
    private transient RecordType type;

    /**
     * Get the name.
     * 
     * @return  the name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Get the operation.
     * 
     * @return  the operation.
     */
    public RecordOperation getOperation() {
        return this.operation;
    }
    
    /**
     * Get the time to live.
     * 
     * @return  the time to live.
     */
    public Long getTimeToLive() {
        return this.timeToLive;
    }

    /**
     * Get the type.
     * 
     * @return  the type.
     */
    public RecordType getType() {
        return this.type;
    }

    /**
     * Create a new instance of the record class.
     *       
     * The data elements vary according to the record class:
     * 
     * <table>
     *   <thead>
     *     <tr><th>Class</th><th>Element</th><th>Type</th><th>Property</th></tr>
     *   </thead>
     *   <tbody>
     *     <tr><td>AAAARecord</td><td>0</td><td>String</td><td>ipv6Address</td></tr>
     *     <tr><td>ARecord</td><td>0</td><td>String</td><td>ipAddress</td></tr>
     *     <tr><td>CNAMERecord</td><td>0</td><td>String</td><td>target</td></tr>
     *     <tr><td>MXRecord</td><td>0</td><td>String</td><td>target</td></tr>
     *     <tr><td></td><td>1</td><td>Integer</td><td>priority</td></tr>
     *     <tr><td>NSRecord</td><td>0</td><td>String</td><td>target</td></tr>
     *     <tr><td>PTRRecord</td><td>0</td><td>String</td><td>target</td></tr>
     *     <tr><td>SOARecord</td><td>0</td><td>Long</td><td>minimumTimeToLive</td></tr>
     *     <tr><td></td><td>1</td><td>Long</td><td>expireInterval</td></tr>
     *     <tr><td></td><td>2</td><td>Long</td><td>retryInterval</td></tr>
     *     <tr><td></td><td>3</td><td>Long</td><td>refreshInterval</td></tr>
     *     <tr><td></td><td>4</td><td>Long</td><td>serialNumber</td></tr>
     *     <tr><td></td><td>5</td><td>String</td><td>emailAddress</td></tr>
     *     <tr><td></td><td>6</td><td>String</td><td>masterNameServer</td></tr>
     *     <tr><td>SRVRecord</td><td>0</td><td>String</td><td>target</td></tr>
     *     <tr><td></td><td>1</td><td>Integer</td><td>port</td></tr>
     *     <tr><td></td><td>2</td><td>Integer</td><td>weight</td></tr>
     *     <tr><td></td><td>3</td><td>Integer</td><td>priority</td></tr>
     *     <tr><td>TXTRecord</td><td> 0</td><td>List<String></td><td>values</td></tr>
     *   </tbody>
     * </table>
     * 
     * @param  recordClass   the class of the record.
     * @param  name          the record name.
     * @param  timeToLive    the record time to live.
     * @param  dataElements  the record data elements.
     * 
     * @return  a new instance of the record class.
     * 
     * @throws  IllegalAccessException    if the record class is not accessible.
     * @throws  IllegalArgumentException  if the data elements are invalid.
     * @throws  InstantiationException    if the record class cannot be instantiated.
     * @throws  NullPointerException      if the record class is null.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Record> T newInstance(final Class<T> recordClass, final String name, final Long timeToLive, final Object... dataElements) throws IllegalAccessException, InstantiationException {

        if (recordClass == null) {
            throw new NullPointerException("The record class cannot be null.");
        }

        // Instantiate the record.
        T record = recordClass.newInstance();
        record.setName(name);
        record.setTimeToLive(timeToLive);
        
        // Check if there are data elements.
        if (dataElements != null) {

            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("The data elements, " + Arrays.asList(dataElements) + ", are invalid for the record class " + recordClass.getSimpleName() + ".");

            // Check if the record class is the AAAA record class.    
            if (AAAARecord.class.equals(recordClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((AAAARecord)record).setIpv6Address((String)dataElements[0]);

            // Check if the record class is the A record class.
            } else if (ARecord.class.equals(recordClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((ARecord)record).setIpAddress((String)dataElements[0]);

            // Check if the record class is the CNAME record class.
            } else if (CNAMERecord.class.equals(recordClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((CNAMERecord)record).setTarget((String)dataElements[0]);

            // Check if the record class is the MX record class.
            } else if (MXRecord.class.equals(recordClass)) {
                if (dataElements.length != 2 || !(dataElements[0] instanceof String) || !(dataElements[1] instanceof Integer)) {
                    throw illegalArgumentException;
                }
                ((MXRecord)record).setPriority((Integer)dataElements[1]);
                ((MXRecord)record).setTarget((String)dataElements[0]);

            // Check if the record class is the NS record class.
            } else if (NSRecord.class.equals(recordClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((NSRecord)record).setTarget((String)dataElements[0]);

            // Check if the record class is the PTR record class.
            } else if (PTRRecord.class.equals(recordClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((PTRRecord)record).setTarget((String)dataElements[0]);

            // Check if the record class is the SOA record class.
            } else if (SOARecord.class.equals(recordClass)) {
                if (dataElements.length != 7 || !(dataElements[0] instanceof Long) || !(dataElements[1] instanceof Long) || !(dataElements[2] instanceof Long) || !(dataElements[3] instanceof Long) || !(dataElements[4] instanceof Long) || !(dataElements[5] instanceof String) || !(dataElements[6] instanceof String)) {
                    throw illegalArgumentException;
                }
                ((SOARecord)record).setEmailAddress((String)dataElements[5]);
                ((SOARecord)record).setExpireInterval((Long)dataElements[1]);
                ((SOARecord)record).setMasterNameServer((String)dataElements[6]);
                ((SOARecord)record).setMinimumTimeToLive((Long)dataElements[0]);
                ((SOARecord)record).setRefreshInterval((Long)dataElements[3]);
                ((SOARecord)record).setRetryInterval((Long)dataElements[2]);
                ((SOARecord)record).setSerialNumber((Long)dataElements[4]);

            // Check if the record class is the SRV record class.
            } else if (SRVRecord.class.equals(recordClass)) {
                if (dataElements.length != 4 || !(dataElements[0] instanceof String) || !(dataElements[1] instanceof Integer) || !(dataElements[2] instanceof Integer) || !(dataElements[3] instanceof Integer)) {
                    throw illegalArgumentException;
                }
                ((SRVRecord)record).setPort((Integer)dataElements[1]);
                ((SRVRecord)record).setPriority((Integer)dataElements[3]);
                ((SRVRecord)record).setTarget((String)dataElements[0]);
                ((SRVRecord)record).setWeight((Integer)dataElements[2]);

            // Check if the record class is the TXT record class.
            } else if (TXTRecord.class.equals(recordClass)) {
                if (dataElements.length != 1 || !(dataElements[0] instanceof List)) {
                    throw illegalArgumentException;
                }
                ((TXTRecord)record).setValues((List<String>)dataElements[0]);
            }
        }

        return record;
    }
       
    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(final String name) {
        this.name = name;
    }
    
    /**
     * Set the operation.
     * 
     * @param  operation  the operation.
     */
    public void setOperation(final RecordOperation operation) {
        this.operation = operation;
    }
    
    /**
     * Set the time to live.
     * 
     * @param  timeToLive  the time to live.
     */
    public void setTimeToLive(final Long timeToLive) {
        this.timeToLive = replaceNull(timeToLive, new Long(DEFAULT_TIME_TO_LIVE));
    }
    
    /**
     * Set the type.
     * 
     * @param  type  the type.
     */
    protected void setType(final RecordType type) {
        this.type = type;
    }
}
