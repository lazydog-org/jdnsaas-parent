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
package org.lazydog.jdnsaas.bind.cache;

import java.io.Serializable;

/**
 * Invalid request message exception.
 * 
 * @author  Ron Rickard
 */
public class InvalidRequestMessageException extends Exception implements Serializable {
    
    public static final long serialVersionUID = 1L;
    private byte[] requestMessage;
    
    /**
     * Constructs a new exception with no message.
     */
    public InvalidRequestMessageException(final byte[] requestMessage) {
        super();
        this.requestMessage = requestMessage;
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message  the message.
     */
    public InvalidRequestMessageException(final String message, final byte[] requestMessage) {
        super(message);
        this.requestMessage = requestMessage;
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message  the message.
     * @param  cause    the cause.
     */
    public InvalidRequestMessageException(final String message, final Throwable cause, final byte[] requestMessage) {
        super(message, cause);
        this.requestMessage = requestMessage;
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause  the cause.
     */
    public InvalidRequestMessageException(final Throwable cause, final byte[] requestMessage) {
        super(cause);
        this.requestMessage = requestMessage;
    }
    
    /**
     * Get the request message.
     * 
     * @return  the request message.
     */
    public byte[] getRequestMessage() {
        return this.requestMessage;
    }
}
