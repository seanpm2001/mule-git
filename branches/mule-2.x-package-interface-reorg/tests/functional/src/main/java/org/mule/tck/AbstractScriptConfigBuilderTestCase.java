/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tck;

import org.mule.api.UMOComponent;
import org.mule.api.UMOException;
import org.mule.api.context.ObjectNotFoundException;
import org.mule.api.endpoint.UMOEndpoint;
import org.mule.api.endpoint.UMOImmutableEndpoint;
import org.mule.api.model.UMOModel;
import org.mule.api.routing.UMOInboundRouterCollection;
import org.mule.api.routing.UMONestedRouter;
import org.mule.api.routing.UMONestedRouterCollection;
import org.mule.api.routing.UMOOutboundRouter;
import org.mule.api.routing.UMOOutboundRouterCollection;
import org.mule.api.routing.UMOResponseRouter;
import org.mule.api.routing.UMOResponseRouterCollection;
import org.mule.api.transformer.UMOTransformer;
import org.mule.impl.AbstractExceptionListener;
import org.mule.impl.model.resolvers.LegacyEntryPointResolverSet;
import org.mule.impl.routing.ForwardingCatchAllStrategy;
import org.mule.impl.routing.outbound.OutboundPassThroughRouter;
import org.mule.impl.transformer.TransformerUtils;
import org.mule.routing.filters.xml.JXPathFilter;
import org.mule.tck.testmodels.fruit.FruitCleaner;
import org.mule.tck.testmodels.mule.TestCompressionTransformer;
import org.mule.tck.testmodels.mule.TestConnector;
import org.mule.tck.testmodels.mule.TestEntryPointResolverSet;
import org.mule.tck.testmodels.mule.TestExceptionStrategy;
import org.mule.tck.testmodels.mule.TestInboundTransformer;
import org.mule.tck.testmodels.mule.TestResponseAggregator;

import java.util.List;

public abstract class AbstractScriptConfigBuilderTestCase extends FunctionalTestCase
{

    // use legacy entry point resolver?
    private boolean legacy;

    protected AbstractScriptConfigBuilderTestCase()
    {
        this(false);
    }

    protected AbstractScriptConfigBuilderTestCase(boolean legacy)
    {
        this.legacy = legacy;
        setDisposeManagerPerSuite(true);
    }

    public void testManagerConfig() throws Exception
    {
        assertEquals("true", muleContext.getRegistry().lookupObject("doCompression"));
        assertNotNull(muleContext.getTransactionManager());
    }


    public void testConnectorConfig() throws Exception
    {
        TestConnector c = (TestConnector) muleContext.getRegistry().lookupConnector("dummyConnector");
        assertNotNull(c);
        assertNotNull(c.getExceptionListener());
        assertTrue(c.getExceptionListener() instanceof TestExceptionStrategy);
    }

    public void testGlobalEndpointConfig() throws UMOException
    {
        UMOImmutableEndpoint endpoint = muleContext.getRegistry().lookupEndpointFactory().getInboundEndpoint(
            "fruitBowlEndpoint");
        assertNotNull(endpoint);
        assertEquals(endpoint.getEndpointURI().getAddress(), "fruitBowlPublishQ");
        assertNotNull(endpoint.getFilter());
        JXPathFilter filter = (JXPathFilter) endpoint.getFilter();
        assertEquals("name", filter.getPattern());
        assertEquals("bar", filter.getExpectedValue());
        assertEquals("http://foo.com", filter.getNamespaces().get("foo"));

        //UMOImmutableEndpoint ep = muleContext.getRegistry().lookupEndpoint("testEPWithCS");
        //assertNotNull(ep);
        //assertNotNull(ep.getConnectionStrategy());
        //assertTrue(ep.getConnectionStrategy() instanceof SimpleRetryConnectionStrategy);
        //assertEquals(4, ((SimpleRetryConnectionStrategy) ep.getConnectionStrategy()).getRetryCount());
        //assertEquals(3000, ((SimpleRetryConnectionStrategy) ep.getConnectionStrategy()).getRetryFrequency());
    }

    public void testEndpointConfig() throws UMOException
    {
        // test that endpoints have been resolved on endpoints
        UMOImmutableEndpoint endpoint = muleContext.getRegistry().lookupEndpointFactory().getInboundEndpoint(
            "waterMelonEndpoint");
        assertNotNull(endpoint);
        // aliases no longer possible
        assertEquals("test.queue", endpoint.getEndpointURI().getAddress());

        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
        UMOImmutableEndpoint ep = component.getInboundRouter().getEndpoint("Orange");
        assertNotNull(ep);
        final List responseTransformers = ep.getResponseTransformers();
        assertNotNull(responseTransformers);
        assertFalse(responseTransformers.isEmpty());
        final Object responseTransformer = responseTransformers.get(0);
        assertTrue(responseTransformer instanceof TestCompressionTransformer);
    }

    public void testExceptionStrategy()
    {
        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
        assertNotNull(muleContext.getRegistry().lookupModel("main").getExceptionListener());
        assertNotNull(component.getExceptionListener());

        assertTrue(((AbstractExceptionListener) component.getExceptionListener()).getEndpoints().size() > 0);
        UMOEndpoint ep = (UMOEndpoint) ((AbstractExceptionListener) component.getExceptionListener()).getEndpoints()
                .get(0);

        assertEquals("test://orange.exceptions", ep.getEndpointURI().toString());
    }

    public void testTransformerConfig()
    {
        UMOTransformer t = muleContext.getRegistry().lookupTransformer("TestCompressionTransformer");
        assertNotNull(t);
        assertTrue(t instanceof TestCompressionTransformer);
        assertEquals(t.getReturnClass(), String.class);
        assertNotNull(((TestCompressionTransformer) t).getContainerProperty());
    }

    public void testModelConfig() throws Exception
    {
        UMOModel model = muleContext.getRegistry().lookupModel("main");
        assertNotNull(model);
        assertEquals("main", model.getName());
        if (legacy)
        {
            assertTrue(model.getEntryPointResolverSet() instanceof LegacyEntryPointResolverSet);
        }
        else
        {
            assertTrue(model.getEntryPointResolverSet() instanceof TestEntryPointResolverSet);
        }
        assertTrue(model.getExceptionListener() instanceof TestExceptionStrategy);

        assertTrue(((AbstractExceptionListener) model.getExceptionListener()).getEndpoints().size() > 0);
        UMOEndpoint ep = (UMOEndpoint) ((AbstractExceptionListener) model.getExceptionListener()).getEndpoints()
                .get(0);

        assertEquals("test://component.exceptions", ep.getEndpointURI().toString());

        // assertTrue(model.isComponentRegistered("orangeComponent"));
    }

    // TODO Fix this somehow after MULE-1933
//    public void testPropertiesConfig() throws Exception
//    {
//        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
//
//        Map props = descriptor.getProperties();
//        assertNotNull(props);
//        assertEquals("9", props.get("segments"));
//        assertEquals("4.21", props.get("radius"));
//        assertEquals("Juicy Baby!", props.get("brand"));
//
//        assertNotNull(props.get("listProperties"));
//        List list = (List) props.get("listProperties");
//        assertEquals(3, list.size());
//        assertEquals("prop1", list.get(0));
//        assertEquals("prop2", list.get(1));
//        assertEquals("prop3", list.get(2));
//
//        assertNotNull(props.get("arrayProperties"));
//        list = (List) props.get("arrayProperties");
//        assertEquals(3, list.size());
//        assertEquals("prop4", list.get(0));
//        assertEquals("prop5", list.get(1));
//        assertEquals("prop6", list.get(2));
//
//        assertNotNull(props.get("mapProperties"));
//        props = (Map) props.get("mapProperties");
//        assertEquals("prop1", props.get("prop1"));
//        assertEquals("prop2", props.get("prop2"));
//
//        assertEquals(6, descriptor.getProperties().size());
//    }

    public void testOutboundRouterConfig()
    {
        // test outbound message router
        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
        assertNotNull(component.getOutboundRouter());
        UMOOutboundRouterCollection router = component.getOutboundRouter();
        assertNull(router.getCatchAllStrategy());
        assertEquals(1, router.getRouters().size());
        // check first Router
        UMOOutboundRouter route1 = (UMOOutboundRouter) router.getRouters().get(0);
        assertTrue(route1 instanceof OutboundPassThroughRouter);
        assertEquals(1, route1.getEndpoints().size());
    }

    public void testNestedRouterConfig() throws ObjectNotFoundException
    {
        // test outbound message router
        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
        assertNotNull(component.getNestedRouter());
        UMONestedRouterCollection router = component.getNestedRouter();
        assertEquals(2, router.getRouters().size());
        // check first Router
        UMONestedRouter route1 = (UMONestedRouter) router.getRouters().get(0);
        assertEquals(FruitCleaner.class, route1.getInterface());
        assertEquals("wash", route1.getMethod());
        assertNotNull(route1.getEndpoint());
        // check second Router
        UMONestedRouter route2 = (UMONestedRouter) router.getRouters().get(1);
        assertEquals(FruitCleaner.class, route2.getInterface());
        assertEquals("polish", route2.getMethod());
        assertNotNull(route1.getEndpoint());
    }

    public void testDescriptorEndpoints()
    {
        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
        assertEquals(1, component.getOutboundRouter().getRouters().size());
        UMOOutboundRouter router = (UMOOutboundRouter)component.getOutboundRouter().getRouters().get(0);
        assertEquals(1, router.getEndpoints().size());
        UMOImmutableEndpoint endpoint = (UMOImmutableEndpoint) router.getEndpoints().get(0);
        assertNotNull(endpoint);
        assertEquals("appleInEndpoint", endpoint.getName());
        assertNotNull(endpoint.getTransformers());
        assertTrue(TransformerUtils.firstOrNull(endpoint.getTransformers()) instanceof TestCompressionTransformer);

        // check the global endpoint
        try
        {
            endpoint = muleContext.getRegistry().lookupEndpointFactory().getInboundEndpoint("appleInEndpoint");
        }
        catch (UMOException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull(endpoint);
        assertTrue(TransformerUtils.isDefined(endpoint.getTransformers()));
        assertTrue(endpoint.getTransformers().get(0) instanceof TestInboundTransformer);

        assertEquals(2, component.getInboundRouter().getEndpoints().size());
        assertNotNull(component.getInboundRouter().getCatchAllStrategy());
        assertTrue(component.getInboundRouter().getCatchAllStrategy() instanceof ForwardingCatchAllStrategy);
        assertNotNull(component.getInboundRouter().getCatchAllStrategy().getEndpoint());
        assertEquals("test://catch.all", component.getInboundRouter()
            .getCatchAllStrategy()
            .getEndpoint()
            .getEndpointURI()
            .toString());
        endpoint = component.getInboundRouter().getEndpoint("orangeEndpoint");
        assertNotNull(endpoint);
        assertEquals("orangeEndpoint", endpoint.getName());
        assertEquals("orangeQ", endpoint.getEndpointURI().getAddress());
        assertNotNull(endpoint.getTransformers());
        assertTrue(TransformerUtils.firstOrNull(endpoint.getTransformers()) instanceof TestCompressionTransformer);

        // check the global endpoint
        try
        {
            endpoint = muleContext.getRegistry().lookupEndpointFactory().getInboundEndpoint("orangeEndpoint");
        }
        catch (UMOException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull(endpoint);
        assertTrue(TransformerUtils.isDefined(endpoint.getTransformers()));
        assertTrue(endpoint.getTransformers().get(0) instanceof TestInboundTransformer);
    }

    public void testInboundRouterConfig()
    {
        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
        assertNotNull(component.getInboundRouter());
        UMOInboundRouterCollection messageRouter = component.getInboundRouter();
        assertNotNull(messageRouter.getCatchAllStrategy());
        assertEquals(0, messageRouter.getRouters().size());
        assertTrue(messageRouter.getCatchAllStrategy() instanceof ForwardingCatchAllStrategy);
        assertEquals(2, messageRouter.getEndpoints().size());
    }

    public void testResponseRouterConfig()
    {
        UMOComponent component = muleContext.getRegistry().lookupComponent("orangeComponent");
        assertNotNull(component.getResponseRouter());
        UMOResponseRouterCollection messageRouter = component.getResponseRouter();
        assertNull(messageRouter.getCatchAllStrategy());
        assertEquals(10001, messageRouter.getTimeout());
        assertEquals(1, messageRouter.getRouters().size());
        UMOResponseRouter router = (UMOResponseRouter) messageRouter.getRouters().get(0);
        assertTrue(router instanceof TestResponseAggregator);
        assertNotNull(messageRouter.getEndpoints());
        assertEquals(2, messageRouter.getEndpoints().size());
        UMOEndpoint ep = (UMOEndpoint) messageRouter.getEndpoints().get(0);
        assertEquals("response1", ep.getEndpointURI().getAddress());
        assertEquals(UMOEndpoint.ENDPOINT_TYPE_RECEIVER, ep.getType());
        ep = (UMOEndpoint) messageRouter.getEndpoints().get(1);
        assertEquals("AppleResponseQueue", ep.getEndpointURI().getAddress());
        assertEquals(UMOEndpoint.ENDPOINT_TYPE_RECEIVER, ep.getType());
    }

    /* excluded - dep on management

    public void _testAgentConfiguration() throws UMOException
    {
        JmxAgent agent = (JmxAgent)muleContext.getRegistry().lookupAgent("jmxAgent");
        assertNotNull(agent);
        //TODO RM* Add this back in. Currently failing because of a JMX issue where the AllStatistics MBean is registered twice
//        assertNotNull(agent.getConnectorServerUrl());
//        assertEquals("service:jmx:rmi:///jndi/rmi://localhost:1099/server", agent.getConnectorServerUrl());
//        assertNotNull(agent.getConnectorServerProperties());
//        assertEquals("true", agent.getConnectorServerProperties().get("jmx.remote.jndi.rebind"));
    }
    */
}
