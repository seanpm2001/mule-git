/*
 * $Id: XFireWsdlMessageDispatcherFactory.java 4350 2006-12-20 16:34:49Z holger $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.cxf.wsdl;

import org.mule.api.UMOException;
import org.mule.api.endpoint.UMOImmutableEndpoint;
import org.mule.api.transport.UMOMessageDispatcher;
import org.mule.impl.transport.AbstractMessageDispatcherFactory;

/**
 * Creates an XFire WSDL Message Dispatcher
 */
public class CxfWsdlMessageDispatcherFactory extends AbstractMessageDispatcherFactory
{

    public UMOMessageDispatcher create(UMOImmutableEndpoint endpoint) throws UMOException
    {
        return new CxfWsdlMessageDispatcher(endpoint);
    }
}
