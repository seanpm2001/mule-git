/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.test.usecases.routing.response;

import org.mule.api.UMOEvent;
import org.mule.api.UMOMessage;
import org.mule.extras.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.test.usecases.service.DummyResponseAggregator;

import java.util.Map;

public class ResponseAggregatorTestCase extends FunctionalTestCase
{

    protected String getConfigResources()
    {
        return "org/mule/test/usecases/routing/response/response-router.xml";
    }

    public void testSyncResponse() throws Exception
    {
        MuleClient client = new MuleClient();
        UMOMessage message = client.send("http://localhost:28081", "request", null);
        assertNotNull(message);
        assertEquals("Received: request", new String(message.getPayloadAsBytes()));
    }

    public void testResponseEventsCleanedUp() throws Exception
    {
        // relax access to get to the responseEvents
        RelaxedResponseAggregator aggregator = new RelaxedResponseAggregator();

        UMOEvent event = getTestEvent("message1");
        final UMOMessage message = event.getMessage();
        final String id = message.getUniqueId();
        message.setCorrelationId(id);
        message.setCorrelationGroupSize(1);
        aggregator.process(event);

        aggregator.getResponse(message);

        Map responseEvents = aggregator.getResponseEvents();
        assertTrue("Response events should be cleaned up.", responseEvents.isEmpty());
    }

    /**
     * This class opens up the access to responseEvents map for testing
     */
    private static final class RelaxedResponseAggregator extends DummyResponseAggregator
    {
        public Map getResponseEvents()
        {
            return this.responseMessages;
        }
    }
}
