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

import java.io.IOException;
import org.lazydog.jdnsaas.bind.ZoneUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Message;
import org.xbill.DNS.Opcode;
import org.xbill.DNS.Section;

/**
 * Notify request message.
 * 
 * @author  Ron Rickard
 */
final class NotifyRequestMessage {
    
    private static final Logger logger = LoggerFactory.getLogger(NotifyRequestMessage.class);
    private Message requestMessage;

    /**
     * Hide the constructor.
     * 
     * @param  requestMessage  the DNS request message (in DNS wire format.)
     * 
     * @throws  InvalidRequestMessageException  if unable to create the notify request message.
     */
    private NotifyRequestMessage(final byte[] requestMessage) throws InvalidRequestMessageException {
        
        try {
            this.requestMessage = new Message(requestMessage);

            if (this.isNotifyRequestMessage()) {
                logger.debug("Notify request message: {}", this.requestMessage);
            }
            else {
                logger.warn("The request message is not a notify request message: {}", this.requestMessage);
                throw new InvalidRequestMessageException("The request message is not a notify request message.", requestMessage);
            }
        } catch (IOException e) {
            logger.warn("The request message is not in DNS wire format: {}", requestMessage, e);
            throw new InvalidRequestMessageException("The request message is not in DNS wire format.", e, requestMessage);
        }
    }
    
    /**
     * Create the notify response message.
     * 
     * @return  the notify response message (in DNS wire format.)
     */
    public byte[] createNotifyResponseMessage() {

        // The notify response message is the notify request message ...
        Message notifyResponseMessage = (Message)this.requestMessage.clone();

        // ... with the query response flag set and no answer section.
        notifyResponseMessage.getHeader().setFlag(Flags.QR);
        notifyResponseMessage.removeAllRecords(Section.ANSWER);
        logger.debug("Notify response message: {}", notifyResponseMessage);

        return notifyResponseMessage.toWire();
    }

    /**
     * Get the zone name.
     * 
     * @return  the zone name.
     */
    public String getZoneName() {
        return ZoneUtility.newInstance(this.requestMessage.getQuestion().getName().toString()).getRelativeZoneName();
    }
    
    /**
     * Is this a notify request message?
     * 
     * @return  true if this is a notify request message, otherwise false.
     */
    private boolean isNotifyRequestMessage() {
        return (this.requestMessage.getHeader().getOpcode() == Opcode.NOTIFY);
    }
    
    /**
     * Create the notify request message.
     * 
     * @param  requestMessage  the request message (in DNS wire format.)
     * 
     * @return  the DNS request message.
     * 
     * @throws  InvalidRequestMessageException  if unable to create the notify request message.
     */
    public static NotifyRequestMessage newInstance(final byte[] requestMessage) throws InvalidRequestMessageException {
        return new NotifyRequestMessage(requestMessage);
    }
}
