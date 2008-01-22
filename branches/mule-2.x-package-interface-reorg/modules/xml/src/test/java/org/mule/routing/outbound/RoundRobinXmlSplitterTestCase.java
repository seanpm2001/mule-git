/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.routing.outbound;

import org.mule.api.UMOMessage;
import org.mule.api.UMOSession;
import org.mule.api.endpoint.UMOEndpoint;
import org.mule.impl.MuleMessage;
import org.mule.impl.endpoint.MuleEndpointURI;
import org.mule.tck.AbstractMuleTestCase;
import org.mule.tck.MuleTestUtils;
import org.mule.util.IOUtils;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class RoundRobinXmlSplitterTestCase extends AbstractMuleTestCase
{
    private UMOEndpoint endpoint1;
    private UMOEndpoint endpoint2;
    private UMOEndpoint endpoint3;
    private RoundRobinXmlSplitter xmlSplitter;

    // @Override
    protected void doSetUp() throws Exception
    {
        // setup endpoints
        endpoint1 = getTestEndpoint("Test1Endpoint", UMOEndpoint.ENDPOINT_TYPE_SENDER);
        endpoint1.setEndpointURI(new MuleEndpointURI("test://endpointUri.1"));
        endpoint2 = getTestEndpoint("Test2Endpoint", UMOEndpoint.ENDPOINT_TYPE_SENDER);
        endpoint2.setEndpointURI(new MuleEndpointURI("test://endpointUri.2"));
        endpoint3 = getTestEndpoint("Test3Endpoint", UMOEndpoint.ENDPOINT_TYPE_SENDER);
        endpoint3.setEndpointURI(new MuleEndpointURI("test://endpointUri.3"));

        // setup splitter
        xmlSplitter = new RoundRobinXmlSplitter();
        xmlSplitter.setValidateSchema(true);
        xmlSplitter.setExternalSchemaLocation("purchase-order.xsd");

        // The xml document declares a default namespace, thus
        // we need to workaround it by specifying it both in
        // the namespaces and in the splitExpression
        Map namespaces = new HashMap();
        namespaces.put("e", "http://www.example.com");
        xmlSplitter.setSplitExpression("/e:purchaseOrder/e:items/e:item");
        xmlSplitter.setNamespaces(namespaces);
        xmlSplitter.addEndpoint(endpoint1);
        xmlSplitter.addEndpoint(endpoint2);
        xmlSplitter.addEndpoint(endpoint3);
    }

    public void testStringPayloadXmlMessageSplitter() throws Exception
    {
        String payload = IOUtils.getResourceAsString("purchase-order2.xml", getClass());
        internalTestSuccessfulXmlSplitter(payload);
    }

    public void testDom4JDocumentPayloadXmlMessageSplitter() throws Exception
    {
        String payload = IOUtils.getResourceAsString("purchase-order2.xml", getClass());
        Document doc = DocumentHelper.parseText(payload);
        internalTestSuccessfulXmlSplitter(doc);
    }

    public void testByteArrayPayloadXmlMessageSplitter() throws Exception
    {
        String payload = IOUtils.getResourceAsString("purchase-order2.xml", getClass());
        internalTestSuccessfulXmlSplitter(payload.getBytes());
    }

    private void internalTestSuccessfulXmlSplitter(Object payload) throws Exception
    {
        Mock session = MuleTestUtils.getMockSession();

        UMOMessage message = new MuleMessage(payload);

        assertTrue(xmlSplitter.isMatch(message));
        final RoundRobinXmlSplitterTestCase.ItemNodeConstraint itemNodeConstraint = new RoundRobinXmlSplitterTestCase.ItemNodeConstraint();
        session.expect("dispatchEvent", C.args(itemNodeConstraint, C.eq(endpoint1)));
        session.expect("dispatchEvent", C.args(itemNodeConstraint, C.eq(endpoint2)));
        session.expect("dispatchEvent", C.args(itemNodeConstraint, C.eq(endpoint3)));
        session.expect("dispatchEvent", C.args(itemNodeConstraint, C.eq(endpoint1)));
        xmlSplitter.route(message, (UMOSession)session.proxy(), false);
        session.verify();

        message = new MuleMessage(payload);

        session.expectAndReturn("sendEvent", C.args(itemNodeConstraint, C.eq(endpoint1)), message);
        session.expectAndReturn("sendEvent", C.args(itemNodeConstraint, C.eq(endpoint2)), message);
        session.expectAndReturn("sendEvent", C.args(itemNodeConstraint, C.eq(endpoint3)), message);
        session.expectAndReturn("sendEvent", C.args(itemNodeConstraint, C.eq(endpoint1)), message);
        UMOMessage result = xmlSplitter.route(message, (UMOSession)session.proxy(), true);
        assertNotNull(result);
        assertEquals(message, result);
        session.verify();
    }

    public void testXsdNotFoundThrowsException() throws Exception
    {
        final String invalidSchemaLocation = "non-existent.xsd";
        Mock session = MuleTestUtils.getMockSession();

        UMOEndpoint endpoint1 = getTestEndpoint("Test1Endpoint", UMOEndpoint.ENDPOINT_TYPE_SENDER);
        endpoint1.setEndpointURI(new MuleEndpointURI("test://endpointUri.1"));

        RoundRobinXmlSplitter splitter = new RoundRobinXmlSplitter();
        splitter.setValidateSchema(true);
        splitter.setExternalSchemaLocation(invalidSchemaLocation);

        String payload = IOUtils.getResourceAsString("purchase-order.xml", getClass());

        UMOMessage message = new MuleMessage(payload);

        assertTrue(splitter.isMatch(message));
        try
        {
            splitter.route(message, (UMOSession)session.proxy(), false);
            fail("Should have thrown an exception, because XSD is not found.");
        }
        catch (IllegalArgumentException iaex)
        {
            assertTrue("Wrong exception?", iaex.getMessage().indexOf(
                "Couldn't find schema at " + invalidSchemaLocation) != -1);
        }
        session.verify();
    }

    public void testUnsupportedTypePayloadIsIgnored() throws Exception
    {
        Exception unsupportedPayload = new Exception();

        Mock session = MuleTestUtils.getMockSession();

        UMOMessage message = new MuleMessage(unsupportedPayload);

        assertTrue(xmlSplitter.isMatch(message));
        xmlSplitter.route(message, (UMOSession)session.proxy(), false);
        session.verify();

        message = new MuleMessage(unsupportedPayload);

        UMOMessage result = xmlSplitter.route(message, (UMOSession)session.proxy(), true);
        assertNull(result);
        session.verify();
    }

    public void testInvalidXmlPayloadThrowsException() throws Exception
    {
        Mock session = MuleTestUtils.getMockSession();

        RoundRobinXmlSplitter splitter = new RoundRobinXmlSplitter();

        UMOMessage message = new MuleMessage("This is not XML.");

        try
        {
            splitter.route(message, (UMOSession)session.proxy(), false);
            fail("No exception thrown.");
        }
        catch (IllegalArgumentException iaex)
        {
            assertTrue("Wrong exception message.", iaex.getMessage().startsWith(
                "Failed to initialise the payload: "));
        }

    }

    private class ItemNodeConstraint implements Constraint
    {
        public boolean eval(Object o)
        {
            final UMOMessage message = (UMOMessage)o;
            final Object payload = message.getPayload();
            assertTrue("Wrong class type for node.", payload instanceof Document);

            Document node = (Document)payload;

            final String partNumber = node.getRootElement().attributeValue("partNum");

            return "872-AA".equals(partNumber) || "926-AA".equals(partNumber) || "126-AA".equals(partNumber)
                   || "226-AA".equals(partNumber);
        }
    }
}
