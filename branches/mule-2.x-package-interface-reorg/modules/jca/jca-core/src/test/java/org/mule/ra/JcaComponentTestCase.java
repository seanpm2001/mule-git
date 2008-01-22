/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.ra;

import org.mule.api.UMOComponent;
import org.mule.api.UMOEvent;
import org.mule.api.UMOException;
import org.mule.api.endpoint.UMOEndpoint;
import org.mule.api.endpoint.UMOImmutableEndpoint;
import org.mule.impl.component.simple.EchoComponent;
import org.mule.impl.model.ModelFactory;
import org.mule.tck.AbstractMuleTestCase;
import org.mule.util.object.SingletonObjectFactory;

public class JcaComponentTestCase extends AbstractMuleTestCase // AbstractComponentTestCase
{

    // Cannot extend AbstractComponentTestCase because of inconsistent behaviour. See
    // MULE-2843

    private UMOComponent component;

    private TestJCAWorkManager workManager;

    protected void doSetUp() throws Exception
    {
        // Create and register JcaModel
        workManager = new TestJCAWorkManager();
        JcaModel jcaModel = (JcaModel) ModelFactory.createModel(JcaModel.JCA_MODEL_TYPE);
        muleContext.getRegistry().registerModel(jcaModel);

        // Create, register, initialise and start JcaComponent
        String name = "JcaComponent#";
        component = new JcaComponent(new DelegateWorkManager(workManager));
        component.setName(name);
        component.setModel(jcaModel);
        component.setServiceFactory(new SingletonObjectFactory(new EchoComponent()));
        muleContext.getRegistry().registerComponent(component);

        assertNotNull(component);
    }

    protected void doTearDown() throws Exception
    {
        workManager = null;
        component = null;
    }

    public void testSendEvent() throws Exception
    {
        component.start();
        UMOEndpoint endpoint = getTestEndpoint("jcaInFlowEndpoint", UMOImmutableEndpoint.ENDPOINT_TYPE_RECEIVER);
        UMOEvent event = getTestEvent("Message", endpoint);

        try
        {
            component.sendEvent(event);
            fail("Exception expected, JcaComponent does not support sendEvent()");
        }
        catch (Exception e)
        {
        }
    }

    public void testDispatchEvent() throws Exception
    {
        component.start();
        UMOEndpoint endpoint = getTestEndpoint("jcaInFlowEndpoint", UMOImmutableEndpoint.ENDPOINT_TYPE_RECEIVER);
        UMOEvent event = getTestEvent("Message", endpoint);

        component.dispatchEvent(event);
        assertEquals(1, workManager.getScheduledWorkList().size());
        assertEquals(0, workManager.getStartWorkList().size());
        assertEquals(0, workManager.getDoWorkList().size());
    }

    public void testPause()
    {
        try
        {
            component.pause();
            fail("Exception expected, JcaComponent does not support pause()");
        }
        catch (UMOException e)
        {

        }
    }

    public void testResume()
    {
        try
        {
            component.resume();
            fail("Exception expected, JcaComponent does not support resume()");
        }
        catch (UMOException e)
        {
        }
    }

}
