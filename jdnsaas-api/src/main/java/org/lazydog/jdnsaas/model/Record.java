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

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

/**
 * Record.
 * 
 * @author  Ron Rickard
 */
public abstract class Record<T extends Record.Data> extends Model {
        
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_TIME_TO_LIVE = 300;
    private T data;
    private String name;
    private RecordOperation operation;
    private Integer timeToLive = new Integer(DEFAULT_TIME_TO_LIVE);
    private transient RecordType type;
  
    /**
     * Get the data.
     * 
     * @return  the data.
     */
    public T getData() {
        return this.data;
    }
    
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
    public Integer getTimeToLive() {
        return this.timeToLive;
    }
    
    /**
     * Get the record data class.
     * 
     * @return  the record data class.
     */
    @SuppressWarnings("unchecked")
    public Class<T> getRecordDataClass() {
        return (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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
     * @param  recordClass  the class of the record.
     * @param  name         the record name.
     * @param  timeToLive   the record time to live.
     * @param  data         the record data.
     * 
     * @return  a new instance of the record class.
     * 
     * @throws  IllegalAccessException    if the record class is not accessible.
     * @throws  InstantiationException    if the record class cannot be instantiated.
     * @throws  NullPointerException      if the record class is null.
     */
    public static <T extends Record<U>,U extends Data> T newInstance(final Class<T> recordClass, final String name, final Integer timeToLive, final U data) throws IllegalAccessException, InstantiationException {

        if (recordClass == null) {
            throw new NullPointerException("The record class cannot be null.");
        }

        // Instantiate the record.
        T record = recordClass.newInstance();

        // Set the record.
        record.setData(data);
        record.setName(name);
        record.setTimeToLive(timeToLive);
        
        return record;
    }

    /**
     * Create a new instance of the record class.
     * 
     * This method will also create a new instance of the record data class associated with this record class if the data elements are not null.
     * 
     * @param  recordClass   the class of the record.
     * @param  name          the record name.
     * @param  timeToLive    the record time to live.
     * @param  dataElements  the record data elements.
     * 
     * @return  a new instance of the record class.
     * 
     * @throws  IllegalAccessException    if the record class or record data class are not accessible.
     * @throws  IllegalArgumentException  if the data elements are invalid.
     * @throws  InstantiationException    if the record class or record data class cannot be instantiated.
     * @throws  NullPointerException      if the record class or record data class are null.
     * 
     * @see  Data#newInstance(java.lang.Class, java.lang.Object[]) 
     */
    public static <T extends Record<U>,U extends Data> T newInstance(final Class<T> recordClass, final String name, final Integer timeToLive, final Object... dataElements) throws IllegalAccessException, InstantiationException {

        if (recordClass == null) {
            throw new NullPointerException("The record class cannot be null.");
        }

        // Instantiate the record.
        T record = recordClass.newInstance();

        if (dataElements != null) {
            
            // Get the record data class.
            Class<U> recordDataClass = record.getRecordDataClass();

            // Create a new instance of the record data class.
            U data = Data.newInstance(recordDataClass, dataElements);
            
            // Set the data on the record.
            record.setData(data);
        }

        // Set the record.
        record.setName(name);
        record.setTimeToLive(timeToLive);
        
        return record;
    }

    /**
     * Set the data.
     * 
     * @param  data  the data.
     */
    public void setData(final T data) {
        this.data = data;
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
    public void setTimeToLive(final Integer timeToLive) {
        this.timeToLive = replaceNull(timeToLive, new Integer(DEFAULT_TIME_TO_LIVE));
    }
    
    /**
     * Set the type.
     * 
     * @param  type  the type.
     */
    protected void setType(final RecordType type) {
        this.type = type;
    }
    
    /**
     * Record data.
     */
    public static abstract class Data extends Model {
   
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
         * @param  dataClass     the class of the record data.
         * @param  dataElements  the record data elements.
         * 
         * @return  a new instance of the record data class.
         * 
         * @throws  IllegalAccessException    if the record data class is not accessible.
         * @throws  IllegalArgumentException  if the data elements are invalid for the record data class.
         * @throws  InstantiationException    if the record data class cannot be instantiated.
         * @throws  NullPointerException      if the record data class is null.
         */
        @SuppressWarnings("unchecked")
        public static <T extends Data> T newInstance(final Class<T> dataClass, final Object... dataElements) throws IllegalAccessException, InstantiationException {

            if (dataClass == null) {
                throw new NullPointerException("The record data class cannot be null.");
            }

            // Instantiate the record data.
            T data = dataClass.newInstance();

            // Check if there are data elements.
            if (dataElements != null) {

                IllegalArgumentException illegalArgumentException = new IllegalArgumentException("The data elements, " + Arrays.asList(dataElements) + ", are invalid for the record data class " + dataClass.getSimpleName() + ".");

                // Check if the record data class is the AAAA record data class.    
                if (AAAARecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                        throw illegalArgumentException;
                    }
                    ((AAAARecord.Data)data).setIpv6Address((String)dataElements[0]);

                // Check if the record data class is the A record data class.
                } else if (ARecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                        throw illegalArgumentException;
                    }
                    ((ARecord.Data)data).setIpAddress((String)dataElements[0]);

                // Check if the record data class is the CNAME record data class.
                } else if (CNAMERecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                        throw illegalArgumentException;
                    }
                    ((CNAMERecord.Data)data).setTarget((String)dataElements[0]);

                // Check if the record data class is the MX record data class.
                } else if (MXRecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 2 || !(dataElements[0] instanceof String) || !(dataElements[1] instanceof Integer)) {
                        throw illegalArgumentException;
                    }
                    ((MXRecord.Data)data).setPriority((Integer)dataElements[1]);
                    ((MXRecord.Data)data).setTarget((String)dataElements[0]);

                // Check if the record data class is the NS record data class.
                } else if (NSRecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                        throw illegalArgumentException;
                    }
                    ((NSRecord.Data)data).setTarget((String)dataElements[0]);

                // Check if the record data class is the PTR record data class.
                } else if (PTRRecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 1 || !(dataElements[0] instanceof String)) {
                        throw illegalArgumentException;
                    }
                    ((PTRRecord.Data)data).setTarget((String)dataElements[0]);

                // Check if the record data class is the SOA record data class.
                } else if (SOARecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 7 || !(dataElements[0] instanceof Integer) || !(dataElements[1] instanceof Integer) || !(dataElements[2] instanceof Integer) || !(dataElements[3] instanceof Integer) || !(dataElements[4] instanceof Long) || !(dataElements[5] instanceof String) || !(dataElements[6] instanceof String)) {
                        throw illegalArgumentException;
                    }
                    ((SOARecord.Data)data).setEmailAddress((String)dataElements[5]);
                    ((SOARecord.Data)data).setExpireInterval((Integer)dataElements[1]);
                    ((SOARecord.Data)data).setMasterNameServer((String)dataElements[6]);
                    ((SOARecord.Data)data).setMinimumTimeToLive((Integer)dataElements[0]);
                    ((SOARecord.Data)data).setRefreshInterval((Integer)dataElements[3]);
                    ((SOARecord.Data)data).setRetryInterval((Integer)dataElements[2]);
                    ((SOARecord.Data)data).setSerialNumber((Long)dataElements[4]);
                    
                // Check if the record data class is the SRV record data class.
                } else if (SRVRecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 4 || !(dataElements[0] instanceof String) || !(dataElements[1] instanceof Integer) || !(dataElements[2] instanceof Integer) || !(dataElements[3] instanceof Integer)) {
                        throw illegalArgumentException;
                    }
                    ((SRVRecord.Data)data).setPort((Integer)dataElements[1]);
                    ((SRVRecord.Data)data).setPriority((Integer)dataElements[3]);
                    ((SRVRecord.Data)data).setTarget((String)dataElements[0]);
                    ((SRVRecord.Data)data).setWeight((Integer)dataElements[2]);

                // Check if the record data class is the TXT record data class.
                } else if (TXTRecord.Data.class.equals(dataClass)) {
                    if (dataElements.length != 1 || !(dataElements[0] instanceof List)) {
                        throw illegalArgumentException;
                    }
                    ((TXTRecord.Data)data).setValues((List<String>)dataElements[0]);
                }
            }

            return data;
        }
    }
}
