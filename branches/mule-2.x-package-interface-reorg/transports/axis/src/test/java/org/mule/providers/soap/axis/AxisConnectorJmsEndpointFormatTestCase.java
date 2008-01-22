/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.soap.axis;

import org.mule.api.UMOMessage;
import org.mule.api.transport.DispatchException;
import org.mule.extras.client.MuleClient;
import org.mule.impl.MuleMessage;
import org.mule.impl.transport.NullPayload;
import org.mule.tck.FunctionalTestCase;

public class AxisConnectorJmsEndpointFormatTestCase extends FunctionalTestCase
{

    public void testAxisOverJmsWithQueueNameSameAsComponentName() throws Exception
    {
        MuleClient client = new MuleClient();
        UMOMessage result = client.send("componentName", new MuleMessage("test1"));
        assertNotNull(result.getPayload());
        assertEquals("test1", result.getPayloadAsString());
    }
    
    public void testAxisOverJmsWithQueueNameDifferentFromComponentName() throws Exception
    {
        MuleClient client = new MuleClient();
        UMOMessage result = client.send("soapActionDefined", new MuleMessage("test2"));
        assertNotNull(result.getPayload());
        assertEquals("test2", result.getPayloadAsString());
    }
    
    public void testAxisOverJmsWithoutSettingMethodOnEndpoint() throws Exception
    {
        MuleClient client = new MuleClient();
        Exception exception = null;
        try
        {
            client.send("noMethodDefined", new MuleMessage("test3"));
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertTrue(exception instanceof DispatchException);
        assertTrue(exception.getMessage().startsWith("Cannot invoke WS call without an Operation."));
    }
    
    public void testAxisOverJmsWithoutSettingSoapAction() throws Exception
    {
        MuleClient client = new MuleClient();
        Exception exception = null;
        try
        {
            UMOMessage msg = client.send("noSoapActionDefined", new MuleMessage("test4"));
            assertEquals(NullPayload.getInstance(), msg.getPayload());
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertNotNull(exception);
        assertTrue(exception instanceof DispatchException);
        assertTrue(exception.getCause().getMessage().startsWith("The AXIS engine could not find a target service to invoke!"));
    }
    
    protected String getConfigResources()
    {
        return "axis-jms-endpoint-format-config.xml";
    }

}