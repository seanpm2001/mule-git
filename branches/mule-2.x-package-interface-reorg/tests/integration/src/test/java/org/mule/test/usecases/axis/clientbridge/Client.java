/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.test.usecases.axis.clientbridge;

import org.mule.api.MuleContext;
import org.mule.api.MuleContextFactory;
import org.mule.api.UMOException;
import org.mule.api.UMOMessage;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.extras.client.MuleClient;
import org.mule.impl.DefaultMuleContextFactory;

public class Client
{
    private static final String LOCAL_ENDPOINT = "vm://complexRequest";
    private static final String AXIS_ENDPOINT = "axis:http://localhost:8002/axisService/doSomeWork";

    public static void main(String[] args) throws Exception
    {
        MuleContext muleContext = null;

        try
        {
            SpringXmlConfigurationBuilder builder = new SpringXmlConfigurationBuilder("clientbridge/conf/client-mule-config.xml");
            MuleContextFactory muleContextFactory = new DefaultMuleContextFactory();
            muleContext = muleContextFactory.createMuleContext(builder);

            Client c = new Client();
            c.execute();
        }
        finally
        {
            if (muleContext != null)
            {
               muleContext.dispose();
            }
        }
    }

    private MuleClient client;

    private void execute() throws UMOException
    {
        client = new MuleClient();

        try
        {
            executeComplexity();
            complexRequest();
        }
        finally
        {
            if (client != null)
            {
                client.dispose();
            }
        }
    }

    private void executeComplexity() throws UMOException
    {
        System.err.println("\nexecuteComplexity");
        Object result = client.send(AXIS_ENDPOINT + "?method=executeComplexity", new ComplexData("Foo",
            new Integer(42)), null);
        System.err.println(result);
        UMOMessage message = (UMOMessage)result;
        ComplexData data = (ComplexData)message.getPayload();
        System.err.println(data);
    }

    private void complexRequest() throws UMOException
    {
        System.err.println("\ncomplexRequest");
        Object result = client.send(LOCAL_ENDPOINT, new ComplexData("Foo", new Integer(84)), null);
        System.err.println(result);
        UMOMessage message = (UMOMessage)result;
        ComplexData data = (ComplexData)message.getPayload();
        System.err.println(data);
    }
}
