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

import org.lazydog.jdnsaas.utility.ZoneUtility;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.lazydog.jdnsaas.model.AAAARecord;
import org.lazydog.jdnsaas.model.ARecord;
import org.lazydog.jdnsaas.model.CNAMERecord;
import org.lazydog.jdnsaas.model.MXRecord;
import org.lazydog.jdnsaas.model.NSRecord;
import org.lazydog.jdnsaas.model.PTRRecord;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.SOARecord;
import org.lazydog.jdnsaas.model.SRVRecord;
import org.lazydog.jdnsaas.model.TXTRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * Record converter.
 * 
 * @author  Ron Rickard
 */
public final class RecordConverter {
    
    private static final Logger logger = LoggerFactory.getLogger(RecordConverter.class);
    private ZoneUtility zoneUtility;
    
    /**
     * Hide the constructor.
     * 
     * @param  zoneName  the zone name.
     */
    private RecordConverter(final String zoneName) {
        this.zoneUtility = ZoneUtility.newInstance(zoneName);
    }

    /**
     * Create the DNS AAAA record from the AAAA record.
     * 
     * @param  aaaaRecord  the AAAA record.
     * 
     * @return  the DNS AAAA record.
     * 
     * @throws  TextParseException   if the record name is invalid.
     * @throws  UknownHostException  if the IPv6 address is invalid.
     */
    private org.xbill.DNS.Record createDnsAAAARecord(final AAAARecord aaaaRecord) throws TextParseException, UnknownHostException {
        return new org.xbill.DNS.AAAARecord(Name.fromString(this.zoneUtility.absolutize(aaaaRecord.getName())), DClass.IN, aaaaRecord.getTimeToLive().longValue(), 
                InetAddress.getByName(aaaaRecord.getIpv6Address()));
    }
    
    /**
     * Create the DNS A record from the A record.
     * 
     * @param  aRecord  the A record.
     * 
     * @return  the DNS A record.
     * 
     * @throws  TextParseException   if the record name is invalid.
     * @throws  UknownHostException  if the IP address is invalid.
     */
    private org.xbill.DNS.Record createDnsARecord(final ARecord aRecord) throws TextParseException, UnknownHostException {
        return new org.xbill.DNS.ARecord(Name.fromString(this.zoneUtility.absolutize(aRecord.getName())), DClass.IN, aRecord.getTimeToLive().longValue(), 
                InetAddress.getByName(aRecord.getIpAddress()));
    }
          
    /**
     * Create the DNS CNAME record from the CNAME record.
     * 
     * @param  cnameRecord  the CNAME record.
     * 
     * @return  the DNS CNAME record.
     * 
     * @throws  TextParseException  if the record name or target is invalid.
     */
    private org.xbill.DNS.Record createDnsCNAMERecord(final CNAMERecord cnameRecord) throws TextParseException {
        return new org.xbill.DNS.CNAMERecord(Name.fromString(this.zoneUtility.absolutize(cnameRecord.getName())), DClass.IN, cnameRecord.getTimeToLive().longValue(), 
                Name.fromString(this.zoneUtility.absolutize(cnameRecord.getTarget())));
    }
                 
    /**
     * Create the DNS MX record from the MX record.
     * 
     * @param  mxRecord  the MX record.
     * 
     * @return  the DNS MX record.
     * 
     * @throws  TextParseException  if the record name or target is invalid.
     */
    private org.xbill.DNS.Record createDnsMXRecord(final MXRecord mxRecord) throws TextParseException {
        return new org.xbill.DNS.MXRecord(Name.fromString(this.zoneUtility.absolutize(mxRecord.getName())), DClass.IN, mxRecord.getTimeToLive().longValue(), 
                mxRecord.getPriority().intValue(), Name.fromString(this.zoneUtility.absolutize(mxRecord.getTarget())));
    }
                  
    /**
     * Create the DNS NS record from the NS record.
     * 
     * @param  nsRecord  the NS record.
     * 
     * @return  the DNS NS record.
     * 
     * @throws  TextParseException  if the record name or target is invalid.
     */
    private org.xbill.DNS.Record createDnsNSRecord(final NSRecord nsRecord) throws TextParseException {
        return new org.xbill.DNS.NSRecord(Name.fromString(this.zoneUtility.absolutize(nsRecord.getName())), DClass.IN, nsRecord.getTimeToLive().longValue(), 
                Name.fromString(this.zoneUtility.absolutize(nsRecord.getTarget())));
    }
                      
    /**
     * Create the DNS PTR record from the PTR record.
     * 
     * @param  ptrRecord  the PTR record.
     * 
     * @return  the DNS PTR record.
     * 
     * @throws  TextParseException  if the record name or target is invalid.
     */
    private org.xbill.DNS.Record createDnsPTRRecord(final PTRRecord ptrRecord) throws TextParseException {
        return new org.xbill.DNS.PTRRecord(Name.fromString(this.zoneUtility.absolutize(this.zoneUtility.getReverseTets(ptrRecord.getName()))), DClass.IN, ptrRecord.getTimeToLive().longValue(), 
                Name.fromString(this.zoneUtility.absolutize(ptrRecord.getTarget())));
    }
                   
    /**
     * Create the DNS SOA record from the SOA record.
     * 
     * @param  soaRecord  the SOA record.
     * 
     * @return  the DNS SOA record.
     * 
     * @throws  TextParseException  if the record name, master name server, or email address is invalid.
     */
    private org.xbill.DNS.Record createDnsSOARecord(final SOARecord soaRecord) throws TextParseException {
        return new org.xbill.DNS.SOARecord(Name.fromString(this.zoneUtility.absolutize(soaRecord.getName())), DClass.IN, soaRecord.getTimeToLive().longValue(),
                Name.fromString(this.zoneUtility.absolutize(soaRecord.getMasterNameServer())), Name.fromString(soaRecord.getEmailAddress()),
                soaRecord.getSerialNumber().longValue(), soaRecord.getRefreshInterval().longValue(), soaRecord.getRetryInterval().longValue(),
                soaRecord.getExpireInterval().longValue(), soaRecord.getMinimumTimeToLive().longValue());
    }
                   
    /**
     * Create the DNS SRV record from the SRV record.
     * 
     * @param  srvRecord  the SRV record.
     * 
     * @return  the DNS SRV record.
     * 
     * @throws  TextParseException  if the record name or target is invalid.
     */
    private org.xbill.DNS.Record createDnsSRVRecord(final SRVRecord srvRecord) throws TextParseException {
        return new org.xbill.DNS.SRVRecord(Name.fromString(this.zoneUtility.absolutize(srvRecord.getName())), DClass.IN, srvRecord.getTimeToLive().longValue(), 
                srvRecord.getPriority().intValue(), srvRecord.getWeight().intValue(), 
                srvRecord.getPort().intValue(), Name.fromString(this.zoneUtility.absolutize(srvRecord.getTarget())));
    }
                           
    /**
     * Create the DNS TXT record from the TXT record.
     * 
     * @param  txtRecord  the TXT record.
     * 
     * @return  the DNS TXT record.
     * 
     * @throws  TextParseException  if the record name is invalid.
     */
    private org.xbill.DNS.Record createDnsTXTRecord(final TXTRecord txtRecord) throws TextParseException {
        return new org.xbill.DNS.TXTRecord(Name.fromString(this.zoneUtility.absolutize(txtRecord.getName())), DClass.IN, txtRecord.getTimeToLive().longValue(), 
                txtRecord.getValues());
    }
    
    /**
     * Create the AAAA record from the DNS AAAA record.
     * 
     * @param  dnsAAAARecord  the DNS AAAA record.
     * 
     * @return  the AAAA record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createAAAARecord(final org.xbill.DNS.AAAARecord dnsAAAARecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(AAAARecord.class, dnsAAAARecord, dnsAAAARecord.getAddress().getHostAddress());
    }

    /**
     * Create the A record from the DNS A record.
     * 
     * @param  dnsARecord  the DNS A record.
     * 
     * @return  the A record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createARecord(final org.xbill.DNS.ARecord dnsARecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(ARecord.class, dnsARecord, dnsARecord.getAddress().getHostAddress());
    }
 
    /**
     * Create the CNAME record from the DNS CNAME record.
     * 
     * @param  dnsCNAMERecord  the DNS CNAME record.
     * 
     * @return  the CNAME record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createCNAMERecord(final org.xbill.DNS.CNAMERecord dnsCNAMERecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(CNAMERecord.class, dnsCNAMERecord, this.zoneUtility.relativize(dnsCNAMERecord.getTarget().toString()));
    }

    /**
     * Create the MX record from the DNS MX record.
     * 
     * @param  dnsMXRecord  the DNS MX record.
     * 
     * @return  the MX record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createMXRecord(final org.xbill.DNS.MXRecord dnsMXRecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(MXRecord.class, dnsMXRecord, this.zoneUtility.relativize(dnsMXRecord.getTarget().toString()), dnsMXRecord.getPriority());
    }
          
    /**
     * Create the NS record from the DNS NS record.
     * 
     * @param  dnsNSRecord  the DNS NS record.
     * 
     * @return  the NS record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createNSRecord(final org.xbill.DNS.NSRecord dnsNSRecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(NSRecord.class, dnsNSRecord, this.zoneUtility.relativize(dnsNSRecord.getTarget().toString()));
    }

    /**
     * Create the PTR record from the DNS PTR record.
     * 
     * @param  dnsPTRRecord  the DNS PTR record.
     * 
     * @return  the PTR record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createPTRRecord(final org.xbill.DNS.PTRRecord dnsPTRRecord) throws IllegalAccessException, InstantiationException {
        return Record.newInstance(PTRRecord.class, this.zoneUtility.getIpAddress(this.zoneUtility.relativize(dnsPTRRecord.getName().toString())), dnsPTRRecord.getTTL(), this.zoneUtility.relativize(dnsPTRRecord.getTarget().toString()));
    }
     
    /**
     * Create the record from the record class, DNS record, and record data.
     * 
     * @param  recordClass  the record class.
     * @param  dnsRecord    the DNS record.
     * @param  recordData   the record data.
     * 
     * @return  the record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private <T extends Record> Record createRecord(final Class<T> recordClass, final org.xbill.DNS.Record dnsRecord, final Object... dataElements) throws IllegalAccessException, InstantiationException {
        return Record.newInstance(recordClass, this.zoneUtility.relativize(dnsRecord.getName().toString()), dnsRecord.getTTL(), dataElements);
    }

    /**
     * Create the SOA record from the DNS SOA record.
     * 
     * @param  dnsSOARecord  the DNS SOA record.
     * 
     * @return  the SOA record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createSOARecord(final org.xbill.DNS.SOARecord dnsSOARecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(SOARecord.class, dnsSOARecord, dnsSOARecord.getMinimum(), dnsSOARecord.getExpire(), dnsSOARecord.getRetry(), dnsSOARecord.getRefresh(), dnsSOARecord.getSerial(), dnsSOARecord.getAdmin().toString(), this.zoneUtility.relativize(dnsSOARecord.getHost().toString()));
    }

    /**
     * Create the SRV record from the DNS SRV record.
     * 
     * @param  dnsSRVRecord  the DNS SRV record.
     * 
     * @return  the SRV record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    private Record createSRVRecord(final org.xbill.DNS.SRVRecord dnsSRVRecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(SRVRecord.class, dnsSRVRecord, this.zoneUtility.relativize(dnsSRVRecord.getTarget().toString()), dnsSRVRecord.getPort(), dnsSRVRecord.getWeight(), dnsSRVRecord.getPriority());
    }
            
    /**
     * Create the TXT record from the DNS TXT record.
     * 
     * @param  dnsTXTRecord  the DNS TXT record.
     * 
     * @return  the TXT record.
     * 
     * @throws  IllegalAccessException  if the record class or record data class are not accessible.
     * @throws  InstantiationException  if the record class or record data class cannot be instantiated.
     */
    @SuppressWarnings("unchecked")
    private Record createTXTRecord(final org.xbill.DNS.TXTRecord dnsTXTRecord) throws IllegalAccessException, InstantiationException {
        return this.createRecord(TXTRecord.class, dnsTXTRecord, dnsTXTRecord.getStrings());
    } 

    /**
     * Convert the DNS record to a record.
     * 
     * @param  dnsRecord              the DNS record.
     * @param  ignoreConversionError  true to ignore a conversion error, otherwise false.
     * 
     * @return  the record.
     * 
     * @throws  RecordConverterException  if the DNS record cannot be converted to a record and ignore conversion error is false.
     */
    public Record fromDnsRecord(final org.xbill.DNS.Record dnsRecord, final boolean ignoreConversionError) throws RecordConverterException {

        Record record = null;
        
        try {
            
            // Get the record based on the DNS record type.
            switch (dnsRecord.getType()) {

                case Type.AAAA:
                    record = this.createAAAARecord((org.xbill.DNS.AAAARecord)dnsRecord);
                    break;
                case Type.A:
                    record = this.createARecord((org.xbill.DNS.ARecord)dnsRecord);
                    break;
                case Type.CNAME:
                    record = this.createCNAMERecord((org.xbill.DNS.CNAMERecord)dnsRecord);
                    break;
                case Type.MX:
                    record = this.createMXRecord((org.xbill.DNS.MXRecord)dnsRecord);
                    break;
                case Type.NS:
                    record = this.createNSRecord((org.xbill.DNS.NSRecord)dnsRecord);
                    break;
                case Type.PTR:
                    record = this.createPTRRecord((org.xbill.DNS.PTRRecord)dnsRecord);
                    break;
                case Type.SOA:
                    record = this.createSOARecord((org.xbill.DNS.SOARecord)dnsRecord);
                    break;
                case Type.SRV:
                    record = this.createSRVRecord((org.xbill.DNS.SRVRecord)dnsRecord);
                    break;
                case Type.TXT:
                    record = this.createTXTRecord((org.xbill.DNS.TXTRecord)dnsRecord);
                    break;
            }
            logger.trace("Converted from {}\n to {}\n", dnsRecord, record);  
        } catch (Exception e) {
            if (ignoreConversionError) {
                logger.warn("The DNS record, {}, cannot be converted to a record.", dnsRecord, e);
            } else {
                throw new RecordConverterException("The DNS record, " + dnsRecord + ", cannot be converted to a record.", e);
            }
        }
    
        return record;       
    }
        
    /**
     * Convert the DNS records to records.
     * 
     * @param  dnsRecords             the DNS records.
     * @param  ignoreConversionError  true to ignore a conversion error, otherwise false.
     * 
     * @return  the records.
     * 
     * @throws  RecordConverterException  if any DNS record cannot be converted to a record and ignore conversion error is false.
     */
    public List<Record> fromDnsRecords(final List<org.xbill.DNS.Record> dnsRecords, final boolean ignoreConversionError) throws RecordConverterException {
        
        List<Record> records = new ArrayList<Record>();
        
        // Loop through the DNS records.
        for (org.xbill.DNS.Record dnsRecord : dnsRecords) {

            // Get the record from the DNS record.
            Record record = this.fromDnsRecord(dnsRecord, ignoreConversionError);

            // Check if the record exists.
            if (record != null) {

                // Add the record to the list.
                records.add(record);
            }
        }
        
        return records;
    }
    
    /**
     * Get the DNS record type.
     * 
     * @param  recordType  the record type.
     * 
     * @return  the DNS record type.
     */
    public static int getDnsRecordType(final RecordType recordType) {
        return Type.value(recordType.toString());
    }

    /**
     * Create a new instance of the record converter class.
     * 
     * @param  zoneName  the zone name.
     * 
     * @return  a new instance of the record converter class.
     */
    public static RecordConverter newInstance(final String zoneName) {
        return new RecordConverter(zoneName);
    }

    /**
     * Convert the record to a DNS record.
     * 
     * @param  record                 the record.
     * @param  ignoreConversionError  true to ignore a conversion error, otherwise false.
     * 
     * @return  the DNS record.
     * 
     * @throws  RecordConverterException  if the record cannot be converted to a DNS record and ignore conversion error is false.
     */
    public org.xbill.DNS.Record toDnsRecord(final Record record, final boolean ignoreConversionError) throws RecordConverterException {

        org.xbill.DNS.Record dnsRecord = null;
        
        try {
            
            // Get the DNS record based on the record type.
            switch (record.getType()) {

                case AAAA:
                    dnsRecord = this.createDnsAAAARecord((AAAARecord)record);
                    break;
                case A:
                    dnsRecord = this.createDnsARecord((ARecord)record);
                    break;
                case CNAME:
                    dnsRecord = this.createDnsCNAMERecord((CNAMERecord)record);
                    break;
                case MX:
                    dnsRecord = this.createDnsMXRecord((MXRecord)record);
                    break;
                case NS:
                    dnsRecord = this.createDnsNSRecord((NSRecord)record);
                    break;
                case PTR:
                    dnsRecord = this.createDnsPTRRecord((PTRRecord)record);
                    break;
                case SOA:
                    dnsRecord = this.createDnsSOARecord((SOARecord)record);
                    break;
                case SRV:
                    dnsRecord = this.createDnsSRVRecord((SRVRecord)record);
                    break;
                case TXT:
                    dnsRecord = this.createDnsTXTRecord((TXTRecord)record);
                    break;
            }
            logger.trace("Converted to {}\n from {}\n", dnsRecord, record);
        } catch (Exception e) {
            if (ignoreConversionError) {
                logger.warn("The record, {}, cannot be converted to a DNS record.", record, e);
            } else {
                throw new RecordConverterException("The record, " + record + ", cannot be converted to a DNS record.", e);
            }
        }
 
        return dnsRecord;
    }
    
            
    /**
     * Convert the records to DNS records.
     * 
     * @param  records                the records.
     * @param  ignoreConversionError  true to ignore a conversion error, otherwise false.
     * 
     * @return  the DNS records.
     * 
     * @throws  RecordConverterException  if any record cannot be converted to a DNS record and ignore conversion error is false.
     */
    public List<org.xbill.DNS.Record> toDnsRecords(final List<Record> records, final boolean ignoreConversionError) throws RecordConverterException {
        
        List<org.xbill.DNS.Record> dnsRecords = new ArrayList<org.xbill.DNS.Record>();
        
        // Loop through the records.
        for (Record record : records) {

            org.xbill.DNS.Record dnsRecord = this.toDnsRecord(record, ignoreConversionError);

            // Check if the record exists.
            if (dnsRecord != null) {

                // Add the DNS record to the list.
                dnsRecords.add(dnsRecord);
            }
        }
        
        return dnsRecords;
    }
}
