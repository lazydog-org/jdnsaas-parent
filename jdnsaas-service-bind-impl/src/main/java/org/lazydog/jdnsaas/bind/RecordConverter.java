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

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.lazydog.jdnsaas.model.AAAARecord;
import org.lazydog.jdnsaas.model.ARecord;
import org.lazydog.jdnsaas.model.CNAMERecord;
import org.lazydog.jdnsaas.model.MXRecord;
import org.lazydog.jdnsaas.model.NSRecord;
import org.lazydog.jdnsaas.model.PTRRecord;
import org.lazydog.jdnsaas.model.Record;
import org.lazydog.jdnsaas.model.RecordData;
import org.lazydog.jdnsaas.model.RecordType;
import org.lazydog.jdnsaas.model.SRVRecord;
import org.lazydog.jdnsaas.model.TXTRecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * Record converter.
 * 
 * @author  Ron Rickard
 */
final class RecordConverter {
    
    private ZoneResolver zoneResolver;
    
    /**
     * Hide the constructor.
     * 
     * @param  zoneName  the zone name.
     */
    private RecordConverter(final String zoneName) {
        this.zoneResolver = ZoneResolver.newInstance(zoneName);
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
    private org.xbill.DNS.Record createAAAARecord(final AAAARecord aaaaRecord) throws TextParseException, UnknownHostException {
        return new org.xbill.DNS.AAAARecord(new Name(this.zoneResolver.absolutize(aaaaRecord.getName())), DClass.IN, aaaaRecord.getTimeToLive().longValue(), 
                InetAddress.getByName(aaaaRecord.getData().getIpv6Address()));
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
        return createRecord(AAAARecord.class, dnsAAAARecord, dnsAAAARecord.getAddress().getHostAddress());
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
    private org.xbill.DNS.Record createARecord(final ARecord aRecord) throws TextParseException, UnknownHostException {
        return new org.xbill.DNS.ARecord(new Name(this.zoneResolver.absolutize(aRecord.getName())), DClass.IN, aRecord.getTimeToLive().longValue(), 
                InetAddress.getByName(aRecord.getData().getIpAddress()));
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
        return createRecord(ARecord.class, dnsARecord, dnsARecord.getAddress().getHostAddress());
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
    private org.xbill.DNS.Record createCNAMERecord(final CNAMERecord cnameRecord) throws TextParseException {
        return new org.xbill.DNS.CNAMERecord(new Name(this.zoneResolver.absolutize(cnameRecord.getName())), DClass.IN, cnameRecord.getTimeToLive().longValue(), 
                new Name(this.zoneResolver.absolutize(cnameRecord.getData().getTarget())));
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
        return createRecord(CNAMERecord.class, dnsCNAMERecord, this.zoneResolver.relativize(dnsCNAMERecord.getTarget().toString()));
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
    private org.xbill.DNS.Record createMXRecord(final MXRecord mxRecord) throws TextParseException {
        return new org.xbill.DNS.MXRecord(new Name(this.zoneResolver.absolutize(mxRecord.getName())), DClass.IN, mxRecord.getTimeToLive().longValue(), 
                mxRecord.getData().getPriority().intValue(), new Name(this.zoneResolver.absolutize(mxRecord.getData().getTarget())));
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
        return createRecord(MXRecord.class, dnsMXRecord, this.zoneResolver.relativize(dnsMXRecord.getTarget().toString()), dnsMXRecord.getPriority());
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
    private org.xbill.DNS.Record createNSRecord(final NSRecord nsRecord) throws TextParseException {
        return new org.xbill.DNS.NSRecord(new Name(this.zoneResolver.absolutize(nsRecord.getName())), DClass.IN, nsRecord.getTimeToLive().longValue(), 
                new Name(this.zoneResolver.absolutize(nsRecord.getData().getTarget())));
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
        return createRecord(NSRecord.class, dnsNSRecord, this.zoneResolver.relativize(dnsNSRecord.getTarget().toString()));
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
    private org.xbill.DNS.Record createPTRRecord(final PTRRecord ptrRecord) throws TextParseException {
        return new org.xbill.DNS.PTRRecord(new Name(this.zoneResolver.absolutize(this.zoneResolver.getReverseTets(ptrRecord.getName()))), DClass.IN, ptrRecord.getTimeToLive().longValue(), 
                new Name(this.zoneResolver.absolutize(ptrRecord.getData().getTarget())));
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
        return Record.newInstance(PTRRecord.class, this.zoneResolver.getIpAddress(this.zoneResolver.relativize(dnsPTRRecord.getName().toString())), (int)dnsPTRRecord.getTTL(), this.zoneResolver.relativize(dnsPTRRecord.getTarget().toString()));
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
    private <T extends Record<U>, U extends RecordData> Record createRecord(final Class<T> recordClass, final org.xbill.DNS.Record dnsRecord, final Object... dataElements) throws IllegalAccessException, InstantiationException {
        return Record.newInstance(recordClass, this.zoneResolver.relativize(dnsRecord.getName().toString()), (int)dnsRecord.getTTL(), dataElements);
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
    private org.xbill.DNS.Record createSRVRecord(final SRVRecord srvRecord) throws TextParseException {
        return new org.xbill.DNS.SRVRecord(new Name(this.zoneResolver.absolutize(srvRecord.getName())), DClass.IN, srvRecord.getTimeToLive().longValue(), 
                srvRecord.getData().getPriority().intValue(), srvRecord.getData().getWeight().intValue(), 
                srvRecord.getData().getPort().intValue(), new Name(this.zoneResolver.absolutize(srvRecord.getData().getTarget())));
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
        return createRecord(SRVRecord.class, dnsSRVRecord, this.zoneResolver.relativize(dnsSRVRecord.getTarget().toString()), dnsSRVRecord.getPort(), dnsSRVRecord.getWeight(), dnsSRVRecord.getPriority());
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
    private org.xbill.DNS.Record createTXTRecord(final TXTRecord txtRecord) throws TextParseException {
        return new org.xbill.DNS.TXTRecord(new Name(this.zoneResolver.absolutize(txtRecord.getName())), DClass.IN, txtRecord.getTimeToLive().longValue(), 
                txtRecord.getData().getValues());
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
        return createRecord(TXTRecord.class, dnsTXTRecord, dnsTXTRecord.getStrings());
    } 
    
    /**
     * Convert the DNS record to a record.
     * 
     * @param  dnsRecord  the DNS record.
     * 
     * @return  the record.
     * 
     * @throws  RecordConverterException  if the DNS record cannot be converted to a record.
     */
    public Record fromDnsRecord(final org.xbill.DNS.Record dnsRecord) throws RecordConverterException {
        
        // Initialize the record.
        Record record = null;
        
        try {
            
            // Get the record based on the DNS record type.
            switch (dnsRecord.getType()) {

                case Type.AAAA:
                    record = createAAAARecord((org.xbill.DNS.AAAARecord)dnsRecord);
                    break;
                case Type.A:
                    record = createARecord((org.xbill.DNS.ARecord)dnsRecord);
                    break;
                case Type.CNAME:
                    record = createCNAMERecord((org.xbill.DNS.CNAMERecord)dnsRecord);
                    break;
                case Type.MX:
                    record = createMXRecord((org.xbill.DNS.MXRecord)dnsRecord);
                    break;
                case Type.NS:
                    record = createNSRecord((org.xbill.DNS.NSRecord)dnsRecord);
                    break;
                case Type.PTR:
                    record = createPTRRecord((org.xbill.DNS.PTRRecord)dnsRecord);
                    break;
                case Type.SRV:
                    record = createSRVRecord((org.xbill.DNS.SRVRecord)dnsRecord);
                    break;
                case Type.TXT:
                    record = createTXTRecord((org.xbill.DNS.TXTRecord)dnsRecord);
                    break;
            }
        } catch (Exception e) {
            throw new RecordConverterException("The DNS record, " + dnsRecord + ", cannot be converted to a record.", e);
        }
                
        return record;       
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
     * @param  record  the record.
     * 
     * @return  the DNS record.
     * 
     * @throws  RecordConverterException  if the record cannot be converted to a DNS record.
     */
    public org.xbill.DNS.Record toDnsRecord(final Record record) throws RecordConverterException {
        
        // Initialize the record.
        org.xbill.DNS.Record dnsRecord = null;
        
        try {
            
            // Get the DNS record based on the record type.
            switch (record.getType()) {

                case AAAA:
                    dnsRecord = createAAAARecord((AAAARecord)record);
                    break;
                case A:
                    dnsRecord = createARecord((ARecord)record);
                    break;
                case CNAME:
                    dnsRecord = createCNAMERecord((CNAMERecord)record);
                    break;
                case MX:
                    dnsRecord = createMXRecord((MXRecord)record);
                    break;
                case NS:
                    dnsRecord = createNSRecord((NSRecord)record);
                    break;
                case PTR:
                    dnsRecord = createPTRRecord((PTRRecord)record);
                    break;
                case SRV:
                    dnsRecord = createSRVRecord((SRVRecord)record);
                    break;
                case TXT:
                    dnsRecord = createTXTRecord((TXTRecord)record);
                    break;
            }
        } catch (Exception e) {
            throw new RecordConverterException("The record, " + record + ", cannot be converted to a DNS record.", e);
        }
                
        return dnsRecord;
    }
}
