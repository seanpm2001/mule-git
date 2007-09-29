/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.config.spring.parsers.specific.endpoint;

import org.mule.config.spring.parsers.specific.endpoint.support.AddressedEndpointDefinitionParser;
import org.mule.config.spring.parsers.specific.endpoint.support.OrphanEndpointDefinitionParser;
import org.mule.impl.endpoint.GlobalEndpoint;

public class TransportGlobalEndpointDefinitionParser extends AddressedEndpointDefinitionParser
{

    public TransportGlobalEndpointDefinitionParser(String protocol)
    {
        super(protocol, new OrphanEndpointDefinitionParser(GlobalEndpoint.class));
    }

}
