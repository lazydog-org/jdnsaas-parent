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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Transaction signature (TSIG).
 * 
 * @author  Ron Rickard
 */
@Entity
@Table(name="transaction_signature")
public class TransactionSignatureEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="transaction_signature_algorithm_id", nullable=false)
    private TransactionSignatureAlgorithmEntity algorithm;
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="secret", nullable=false)
    private String secret;
         
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
     * Get the algorithm.
     * 
     * @return  the algorithm.
     */
    public TransactionSignatureAlgorithmEntity getAlgorithm() {
        return this.algorithm;
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
     * Get the secret.
     * 
     * @return  the secret.
     */
    public String getSecret() {
        return this.secret;
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
     * Set the algorithm.
     * 
     * @param  algorithm  the algorithm.
     */
    public void setAlgorithm(TransactionSignatureAlgorithmEntity algorithm) {
        this.algorithm = algorithm;
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
     * Set the secret.
     * 
     * @param  secret  the secret.
     */
    public void setSecret(String secret) {
        this.secret = secret;
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
