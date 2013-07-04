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

/**
 * SOA record.
 * 
 * @author  Ron Rickard
 */
public class SOARecord extends Record {
    
    private static final long serialVersionUID = 1L;
    private String emailAddress;
    private Long expireInterval = new Long(0);
    private String masterNameServer;
    private Long minimumTimeToLive = new Long(0);
    private Long refreshInterval = new Long(0);
    private Long retryInterval = new Long(0);
    private Long serialNumber = new Long(0);
    
    /**
     * Initialize the record with the record type.
     */
    public SOARecord() {
        this.setType(RecordType.SOA);
    }

    /**
     * Get the email address.
     * 
     * @return  the email address.
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * Get the expire interval.
     * 
     * @return  the expire interval.
     */
    public Long getExpireInterval() {
        return this.expireInterval;
    }

    /**
     * Get the master name server.
     * 
     * @return  the master name server.
     */
    public String getMasterNameServer() {
        return this.masterNameServer;
    }

    /**
     * Get the minimum time to live.
     * 
     * @return  the minimum time to live.
     */
    public Long getMinimumTimeToLive() {
        return this.minimumTimeToLive;
    }

    /**
     * Get the refresh interval.
     * 
     * @return  the refresh interval.
     */
    public Long getRefreshInterval() {
        return this.refreshInterval;
    }

    /**
     * Get the retry interval.
     * 
     * @return  the retry interval.
     */
    public Long getRetryInterval() {
        return this.retryInterval;
    }

    /**
     * Get the serial number.
     * 
     * @return  the serial number.
     */
    public Long getSerialNumber() {
        return this.serialNumber;
    }

    /**
     * Set the email address.
     * 
     * @param  emailAddress  the email address.
     */
    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Set the expire interval.
     * 
     * @param  expireInterval  the expire interval.
     */
    public void setExpireInterval(final Long expireInterval) {
        this.expireInterval = replaceNull(expireInterval, new Long(0));
    }

    /**
     * Set the master name server.
     * 
     * @param  masterNameServer  the master name server.
     */
    public void setMasterNameServer(final String masterNameServer) {
        this.masterNameServer = masterNameServer;
    }

    /**
     * Set the minimum time to live.
     * 
     * @param  minimumTimeToLive  the minimum time to live.
     */
    public void setMinimumTimeToLive(final Long minimumTimeToLive) {
        this.minimumTimeToLive = replaceNull(minimumTimeToLive, new Long(0));
    }

    /**
     * Set the refresh interval.
     * 
     * @param  refreshInterval  the refresh interval.
     */
    public void setRefreshInterval(final Long refreshInterval) {
        this.refreshInterval = replaceNull(refreshInterval, new Long(0));
    }

    /**
     * Set the retry interval.
     * 
     * @param  retryInterval  the retry interval.
     */
    public void setRetryInterval(final Long retryInterval) {
        this.retryInterval = replaceNull(retryInterval, new Long(0));
    }

    /**
     * Set the serial number.
     * 
     * @param  serialNumber  the serial number.
     */
    public void setSerialNumber(final Long serialNumber) {
        this.serialNumber = replaceNull(serialNumber, new Long(0));
    }
}
