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

import org.mule.api.MuleException;
import org.mule.api.routing.RoutingException;
import org.mule.api.security.UnauthorisedException;
import org.mule.impl.config.ExceptionHelper;
import org.mule.imple.config.i18n.MessageFactory;
import org.mule.tck.AbstractMuleTestCase;

public class StatusCodeMappingsTestCase extends AbstractMuleTestCase
{
    public void testErrorMappings()
    {
        String code = ExceptionHelper.getErrorMapping("http", RoutingException.class);
        assertEquals("500", code);

        code = ExceptionHelper.getErrorMapping("HTTP", org.mule.api.security.SecurityException.class);
        assertEquals("403", code);

        code = ExceptionHelper.getErrorMapping("http", UnauthorisedException.class);
        assertEquals("401", code);

        code = ExceptionHelper.getErrorMapping("blah", MuleException.class);
        assertEquals(
            String.valueOf(new MuleException(MessageFactory.createStaticMessage("test")).getExceptionCode()), code);

    }

    public void testHttpsErrorMappings()
    {
        String code = ExceptionHelper.getErrorMapping("httpS", RoutingException.class);
        assertEquals("500", code);

        code = ExceptionHelper.getErrorMapping("HTTPS", org.mule.api.security.SecurityException.class);
        assertEquals("403", code);

        code = ExceptionHelper.getErrorMapping("https", UnauthorisedException.class);
        assertEquals("401", code);
    }
}
