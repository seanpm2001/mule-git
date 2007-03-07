/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.email;

import org.mule.providers.AbstractMessageDispatcherFactory;
import org.mule.umo.UMOException;
import org.mule.umo.endpoint.UMOImmutableEndpoint;
import org.mule.umo.provider.UMOMessageDispatcher;

/**
 * <code>Pop3MessageDispatcherFactory</code> creates a Pop3 Message dispatcher. For
 * Pop3 connections the dispatcher can only be used to receive message (as apposed to
 * listening for them). Trying to send or dispatch will throw an
 * {@link UnsupportedOperationException}.
 */

public class Pop3MessageDispatcherFactory extends AbstractMessageDispatcherFactory
{
    /**
     * By default client connections to POP3 are closed after the request.
     */
    // @Override
    public boolean isCreateDispatcherPerRequest()
    {
        return true;
    }

    public UMOMessageDispatcher create(UMOImmutableEndpoint endpoint) throws UMOException
    {
        return new Pop3MessageDispatcher(endpoint);
    }

}
