/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.xmpp;

import org.mule.api.UMOComponent;
import org.mule.api.UMOException;
import org.mule.api.UMOMessage;
import org.mule.api.endpoint.UMOEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.UMOMessageAdapter;
import org.mule.impl.MuleMessage;
import org.mule.impl.transport.AbstractConnector;
import org.mule.impl.transport.AbstractMessageReceiver;
import org.mule.impl.transport.ConnectException;
import org.mule.imple.config.i18n.CoreMessages;

import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/** <code>XmppMessageReceiver</code> is responsible for receiving Mule events over XMPP. */
public class XmppMessageReceiver extends AbstractMessageReceiver implements PacketListener
{
    private XMPPConnection xmppConnection = null;

    public XmppMessageReceiver(AbstractConnector connector, UMOComponent component, UMOEndpoint endpoint)
            throws CreateException
    {

        super(connector, component, endpoint);
    }

    protected void doConnect() throws Exception
    {
        try
        {
            XmppConnector cnn = (XmppConnector) connector;
            xmppConnection = cnn.createXmppConnection(endpoint.getEndpointURI());
            if (endpoint.getFilter() instanceof PacketFilter)
            {
                xmppConnection.addPacketListener(this, (PacketFilter) endpoint.getFilter());
            }
            else
            {
                PacketFilter filter = new PacketTypeFilter(Message.class);
                xmppConnection.addPacketListener(this, filter);
            }
        }
        catch (XMPPException e)
        {
            throw new ConnectException(CoreMessages.failedToCreate("XMPP Connection"), e, this);
        }
    }

    protected void doDisconnect() throws Exception
    {
        if (xmppConnection != null)
        {
            xmppConnection.removePacketListener(this);
            xmppConnection.close();
        }
    }

    protected void doStart() throws UMOException
    {
        // nothing to do
    }

    protected void doStop() throws UMOException
    {
        // nothing to do
    }

    protected void doDispose()
    {
        // nothing to do
    }

    protected Work createWork(Packet message)
    {
        return new XMPPWorker(message);
    }

    /** @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet) */
    public void processPacket(Packet packet)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("processing packet: " + packet.toXML());
        }

        Work work = createWork(packet);
        try
        {
            getWorkManager().scheduleWork(work, WorkManager.INDEFINITE, null, connector);
        }
        catch (WorkException e)
        {
            logger.error("Xmpp Server receiver work failed: " + e.getMessage(), e);
        }
    }

    private class XMPPWorker implements Work
    {
        Packet packet = null;

        public XMPPWorker(Packet message)
        {
            this.packet = message;
        }

        /** Accept requests from a given TCP port */
        public void run()
        {
            try
            {
                UMOMessageAdapter adapter = connector.getMessageAdapter(packet);

                if (logger.isDebugEnabled())
                {
                    logger.debug("Processing XMPP packet from: " + packet.getFrom());
                    logger.debug("UMOMessageAdapter is a: " + adapter.getClass().getName());
                }

                UMOMessage returnMessage = routeMessage(new MuleMessage(adapter), endpoint.isSynchronous());

                if (returnMessage != null && packet instanceof Message)
                {
                    returnMessage.applyTransformers(connector.getDefaultResponseTransformers());
                    Packet result = (Packet) returnMessage.getPayload();
                    xmppConnection.sendPacket(result);
                }
            }
            catch (Exception e)
            {
                handleException(e);
            }
        }

        public void release()
        {
            // template method
        }
    }
}
