/* 

 * $Header$

 * $Revision$

 * $Date$

 * ------------------------------------------------------------------------------------------------------

 * 

 * Copyright (c) SymphonySoft Limited. All rights reserved.

 * http://www.symphonysoft.com

 * 

 * The software in this package is published under the terms of the BSD

 * style license a copy of which has been included with this distribution in

 * the LICENSE.txt file. 

 *

 */

package org.mule.providers.vm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.Messages;
import org.mule.providers.AbstractMessageDispatcher;
import org.mule.umo.UMOEvent;
import org.mule.umo.UMOException;
import org.mule.umo.UMOMessage;
import org.mule.umo.endpoint.UMOEndpointURI;
import org.mule.umo.provider.DispatchException;
import org.mule.umo.provider.NoReceiverForEndpointException;
import org.mule.umo.provider.UMOConnector;
import org.mule.util.queue.BoundedPersistentQueue;

/**
 * <code>VMMessageDispatcher</code> TODO
 *
 * @author <a href="mailto:ross.mason@symphonysoft.com">Ross Mason</a>
 * @version $Revision$
 */
public class VMMessageDispatcher extends AbstractMessageDispatcher
{
    /**
     * logger used by this class
     */
    private static transient Log logger = LogFactory.getLog(VMMessageDispatcher.class);

    private VMConnector connector;

    public VMMessageDispatcher(VMConnector connector)
    {
        super(connector);
        this.connector = connector;
    }

    /* (non-Javadoc)
     * @see org.mule.umo.provider.UMOMessageDispatcher#getDelegateSession()
     */
    public Object getDelegateSession() throws UMOException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.mule.umo.provider.UMOMessageDispatcher#receive(java.lang.String, org.mule.umo.UMOEvent)
     */
    public UMOMessage receive(UMOEndpointURI endpointUri, long timeout) throws Exception
    {
        if(!connector.isQueueEvents()) {
            throw new UnsupportedOperationException("Receive only supported on the VM Queue Connector");
        }
        BoundedPersistentQueue queue = connector.getQueue(endpointUri.getAddress());
        if(queue == null) {
            return null;
        } else {
            UMOEvent event = null;
            try
            {
                event = (UMOEvent)queue.poll(timeout);
            } catch (InterruptedException e)
            {
                logger.error("failed to receive event from queue: " + endpointUri);
            }
            if(event != null) {
                return event.getMessage();
            }
            return null;
        }
    }

    /* (non-Javadoc)
      * @see org.mule.umo.provider.UMOConnector#dispatch(org.mule.umo.UMOEvent)
      */
    public void doDispatch(UMOEvent event) throws Exception
    {
        UMOEndpointURI endpointUri = event.getEndpoint().getEndpointURI();

        if(endpointUri==null) {
            throw new DispatchException(new Message(Messages.X_IS_NULL, "Endpoint"), event.getMessage(), event.getEndpoint());
        }
        if(connector.isQueueEvents()) {
            BoundedPersistentQueue queue = connector.getQueue(endpointUri.getAddress());
            if(queue==null) {
                logger.info("Creating vm Queue: " + endpointUri.getAddress());
                queue = connector.createQueue(endpointUri.getAddress());
            }
            queue.put(event);
        } else {
            VMMessageReceiver receiver = connector.getReceiver(event.getEndpoint().getEndpointURI());
            if(receiver==null) {
                logger.warn("No receiver for endpointUri: " + event.getEndpoint().getEndpointURI());
                return;
            }
            receiver.onEvent(event);
        }
        logger.debug("dispatched Event on endpointUri: " + endpointUri);
    }

    /* (non-Javadoc)
     * @see org.mule.umo.provider.UMOConnector#send(org.mule.umo.UMOEvent)
     */
    public UMOMessage doSend(UMOEvent event) throws Exception
    {
        UMOMessage retMessage = null;
        UMOEndpointURI endpointUri = event.getEndpoint().getEndpointURI();
        VMMessageReceiver receiver = connector.getReceiver(endpointUri);
        if(receiver==null) {
            if(connector.isQueueEvents()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Writing to queue as there is no receiver on connector: " + connector.getName() + ", for endpointUri: " + event.getEndpoint().getEndpointURI());
                }
                doDispatch(event);
                return null;
            } else {
                throw new NoReceiverForEndpointException(new Message(Messages.NO_RECEIVER_X_FOR_ENDPOINT_X, connector.getName(), event.getEndpoint().getEndpointURI()));
            }
        }
        retMessage = (UMOMessage) receiver.onCall(event);
        logger.debug("sent event on endpointUri: " + event.getEndpoint().getEndpointURI());
        return retMessage;
    }

    /* (non-Javadoc)
     * @see org.mule.umo.provider.UMOMessageDispatcher#getConnector()
     */
    public UMOConnector getConnector()
    {
        return connector;
    }

    public void doDispose()
    {
    }
}