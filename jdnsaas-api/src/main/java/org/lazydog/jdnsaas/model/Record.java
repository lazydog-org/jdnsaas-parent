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

/**
 * Record.
 * 
 * @author  Ron Rickard
 */
public abstract class Record<T extends RecordData> extends Model {
        
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
    public static <T extends Record<U>,U extends RecordData> T newInstance(final Class<T> recordClass, final String name, final Integer timeToLive, final U data) throws IllegalAccessException, InstantiationException {

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
     * @see  RecordData#newInstance(java.lang.Class, java.lang.Object[]) 
     */
    public static <T extends Record<U>,U extends RecordData> T newInstance(final Class<T> recordClass, final String name, final Integer timeToLive, final Object... dataElements) throws IllegalAccessException, InstantiationException {

        if (recordClass == null) {
            throw new NullPointerException("The record class cannot be null.");
        }

        // Instantiate the record.
        T record = recordClass.newInstance();

        if (dataElements != null) {
            
            // Get the record data class.
            Class<U> recordDataClass = record.getRecordDataClass();

            // Create a new instance of the record data class.
            U data = RecordData.newInstance(recordDataClass, dataElements);
            
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
}
