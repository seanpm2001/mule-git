/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.util.properties;

import org.mule.api.UMOMessage;

import java.util.Map;

/**
 * If the message payload is a map this extractor will look up the property value in
 * the map
 */
public class MapPayloadPropertyExtractor implements PropertyExtractor
{
    public static final String NAME = "map";
    
    public Object getProperty(String name, Object message)
    {
        Object payload = message;
        if (message instanceof UMOMessage)
        {
            payload = ((UMOMessage) message).getPayload();
        }
        if (payload instanceof Map)
        {
            return ((Map) payload).get(name);
        }
        return null;
    }

    /** {@inheritDoc} */
    public String getName()
    {
        return NAME;
    }

    /** {@inheritDoc} */
    public void setName(String name)
    {
        throw new UnsupportedOperationException("setName");
    }

}
