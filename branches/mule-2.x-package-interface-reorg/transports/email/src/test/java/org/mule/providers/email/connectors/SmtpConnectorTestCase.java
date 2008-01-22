/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.email.connectors;

import org.mule.api.UMOComponent;
import org.mule.api.UMOMessage;
import org.mule.api.UMOSession;
import org.mule.api.endpoint.UMOEndpointBuilder;
import org.mule.api.endpoint.UMOImmutableEndpoint;
import org.mule.api.transport.UMOConnector;
import org.mule.impl.MuleEvent;
import org.mule.impl.MuleMessage;
import org.mule.impl.ResponseOutputStream;
import org.mule.impl.endpoint.EndpointURIEndpointBuilder;
import org.mule.impl.routing.outbound.OutboundPassThroughRouter;
import org.mule.providers.email.MailProperties;
import org.mule.providers.email.SmtpConnector;
import org.mule.tck.functional.FunctionalTestComponent;
import org.mule.tck.testmodels.fruit.Apple;

import com.icegreen.greenmail.util.ServerSetup;

import javax.mail.internet.MimeMessage;

/**
 * Send a message via SMTP to a (greenmail) server.
 */
public class SmtpConnectorTestCase extends AbstractMailConnectorFunctionalTestCase
{    
    public static final long DELIVERY_DELAY_MS = 5000;

    public SmtpConnectorTestCase()
    {
        this(ServerSetup.PROTOCOL_SMTP, 50007);
    } 
    
    public SmtpConnectorTestCase(String protocol, int port)
    {
        super(NO_INITIAL_EMAIL, protocol, port);
    }

    public UMOConnector createConnector() throws Exception
    {
        SmtpConnector c = new SmtpConnector();
        c.setName("SmtpConnector");
        return c;
    }

    /**
     * The SmtpConnector does not accept listeners, so the test in the
     * superclass makes no sense here.  Instead, we simply check that
     * a listener is rejected.
     */
    // @Override
    public void testConnectorListenerSupport() throws Exception
    {
        UMOConnector connector = getConnector();
        assertNotNull(connector);

        UMOComponent component = getTestComponent("anApple", Apple.class);
        //muleContext.getRegistry().registerComponent(component);
        UMOEndpointBuilder builder = new EndpointURIEndpointBuilder(getTestEndpointURI(), muleContext);
        builder.setName("test");
        UMOImmutableEndpoint endpoint = muleContext.getRegistry().lookupEndpointFactory().getOutboundEndpoint(
            builder);
        try
        {
            connector.registerListener(component, endpoint);
            fail("SMTP connector does not accept listeners");
        }
        catch (Exception e)
        {
            assertNotNull("expected", e);
        }
    }

    public void testSend() throws Exception
    {
        //muleContext.getRegistry().registerConnector(createConnector(false));
        UMOImmutableEndpoint endpoint = muleContext.getRegistry().lookupEndpointFactory().getOutboundEndpoint(
            getTestEndpointURI());
        
        UMOComponent component = getTestComponent(uniqueName("testComponent"), FunctionalTestComponent.class);
        // TODO Simplify this API for adding an outbound endpoint.
        ((OutboundPassThroughRouter) component.getOutboundRouter().getRouters().get(0)).addEndpoint(endpoint);
        //muleContext.getRegistry().registerComponent(component);

        UMOMessage message = new MuleMessage(MESSAGE);
        message.setStringProperty(MailProperties.TO_ADDRESSES_PROPERTY, EMAIL);
        UMOSession session = getTestSession(getTestComponent("apple", Apple.class));
        MuleEvent event = new MuleEvent(message, endpoint, session, true, new ResponseOutputStream(System.out));
        endpoint.dispatch(event);

        getServers().waitForIncomingEmail(DELIVERY_DELAY_MS, 1);
        MimeMessage[] messages = getServers().getReceivedMessages();
        assertNotNull("did not receive any messages", messages);
        assertEquals("did not receive 1 mail", 1, messages.length);
        assertMessageOk(messages[0]);
    }
    
}
