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
package org.lazydog.jdnsaas.spi.repository.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * DNS server entity.
 * 
 * @author  Ron Rickard
 */
@Entity
@Table(name="dns_server")
public class DNSServerEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="port", nullable=false)
    private Integer port;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="transaction_signature_id", nullable=false)
    private TransactionSignatureEntity transactionSignature;
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="dns_server_id")
    private List<ZoneEntity> zones = new ArrayList<ZoneEntity>();
    
    /**
     * Compare this object to the specified object.
     * 
     * @param  object  the object to compare this object against.
     * 
     * @return  true if the objects are equal; false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    /**
     * Get the ID.
     * 
     * @return  the ID.
     */
    public Integer getId() {
        return this.id;
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
     * Get the port.
     * 
     * @return  the port.
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * Get the transaction signature.
     * 
     * @return  the transaction signature.
     */
    public TransactionSignatureEntity getTransactionSignature() {
        return this.transactionSignature;
    }
    
    /**
     * Get the zones.
     * 
     * @return  the zones.
     */
    public List<ZoneEntity> getZones() {
        return this.zones;
    }
    
    /**
     * Returns a hash code for this object.
     * 
     * @return  a hash code for this object.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    /**
     * Set the ID.
     * 
     * @param  id  the ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(String name) {
        this.name = name;
    }
       
    /**
     * Set the port.
     * 
     * @param  port  the port.
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Set the transaction signature.
     * 
     * @param  transactionSignature  the transaction signature.
     */
    public void setTransactionSignature(TransactionSignatureEntity transactionSignature) {
        this.transactionSignature = transactionSignature;
    }
    
    /**
     * Set the zones.
     * 
     * @param  zones  the zones.
     */
    public void setZones(List<ZoneEntity> zones) {
        this.zones = zones;
    }
    
    /**
     * Get this object as a string.
     *
     * @return  this object as a string.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
