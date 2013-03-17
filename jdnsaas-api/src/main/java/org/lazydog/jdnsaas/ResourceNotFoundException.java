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
package org.lazydog.jdnsaas;

import java.io.Serializable;

/**
 * Resource not found exception.
 * 
 * @author  Ron Rickard
 */
public class ResourceNotFoundException extends Exception implements Serializable {
    
    public static final long serialVersionUID = 1L;
    private String resourceName;
    
    /**
     * Constructs a new exception with no message.
     * 
     * @param  resourceName  the resource name.
     */
    public ResourceNotFoundException(final String resourceName) {
        super();
        this.resourceName = resourceName;
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message       the message.
     * @param  resourceName  the resource name.
     */
    public ResourceNotFoundException(final String message, final String resourceName) {
        super(message);
        this.resourceName = resourceName;
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message       the message.
     * @param  cause         the cause.
     * @param  resourceName  the resource name.
     */
    public ResourceNotFoundException(final String message, final Throwable cause, final String resourceName) {
        super(message, cause);
        this.resourceName = resourceName;
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause         the cause.
     * @param  resourceName  the resource name.
     */
    public ResourceNotFoundException(final Throwable cause, final String resourceName) {
        super(cause);
        this.resourceName = resourceName;
    }

    /**
     * Get the resource name.
     * 
     * @return  the resource name.
     */
    public String getResourceName() {
        return this.resourceName;
    }
}
