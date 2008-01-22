/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.http;

import org.mule.api.UMOComponent;
import org.mule.api.UMOMessage;
import org.mule.api.endpoint.UMOEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transport.UMOConnector;

import java.io.IOException;
import java.net.Socket;
import java.security.cert.Certificate;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.resource.spi.work.Work;

public class HttpsMessageReceiver extends HttpMessageReceiver
{

    public HttpsMessageReceiver(UMOConnector connector, UMOComponent component, UMOEndpoint endpoint)
            throws CreateException
    {
        super(connector, component, endpoint);
    }

    // @Override
    protected Work createWork(Socket socket) throws IOException
    {
        return new HttpsWorker(socket);
    }


    private class HttpsWorker extends HttpWorker implements HandshakeCompletedListener
    {
        private Certificate[] peerCertificateChain;
        private Certificate[] localCertificateChain;

        public HttpsWorker(Socket socket) throws IOException
        {
            super(socket);
            ((SSLSocket) socket).addHandshakeCompletedListener(this);
        }

        protected void preRouteMessage(UMOMessage message)
        {
            super.preRouteMessage(message);
            
            if (peerCertificateChain != null)
            {
                message.setProperty(HttpsConnector.PEER_CERTIFICATES, peerCertificateChain);
            }
            if (localCertificateChain != null)
            {
                message.setProperty(HttpsConnector.LOCAL_CERTIFICATES, localCertificateChain);
            }
        }

        public void handshakeCompleted(HandshakeCompletedEvent event)
        {
            localCertificateChain = event.getLocalCertificates();
            try
            {
                peerCertificateChain = event.getPeerCertificates();
            }
            catch (SSLPeerUnverifiedException e)
            {
                logger.debug("Cannot get peer certificate chain: "+ e.getMessage());
            }
        }

    }


}
