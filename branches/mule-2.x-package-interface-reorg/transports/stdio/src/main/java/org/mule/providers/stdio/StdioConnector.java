/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.stdio;

import org.mule.api.UMOComponent;
import org.mule.api.endpoint.UMOImmutableEndpoint;
import org.mule.api.transport.UMOMessageReceiver;
import org.mule.impl.transport.AbstractConnector;
import org.mule.impl.transport.AbstractPollingMessageReceiver;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * <code>StdioConnector</code> can send and receive Mule events over IO streams.
 */

public abstract class StdioConnector extends AbstractConnector
{

    public static final String STDIO = "stdio";
    public static final String STREAM_SYSTEM_IN = "system.in";
    public static final String STREAM_SYSTEM_OUT = "system.out";
    public static final String STREAM_SYSTEM_ERR = "system.err";

    protected OutputStream outputStream;
    protected InputStream inputStream;

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.api.transport.UMOConnector#registerListener(org.mule.api.UMOSession,
     *      org.mule.api.endpoint.UMOEndpoint)
     */
    public UMOMessageReceiver createReceiver(UMOComponent component, UMOImmutableEndpoint endpoint) throws Exception
    {
        return serviceDescriptor.createMessageReceiver(this, component, endpoint,
            new Object[]{new Long(AbstractPollingMessageReceiver.DEFAULT_POLL_FREQUENCY)});
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transport.AbstractConnector#doStop()
     */
    public void doStop()
    {
        // template method
    }

    protected void doDispose()
    {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transport.AbstractConnector#doStart()
     */
    public void doStart()
    {
        // template method
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.api.transport.UMOConnector#getProtocol()
     */

    public String getProtocol()
    {
        return STDIO;
    }

    public InputStream getInputStream()
    {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream()
    {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
    }

}
