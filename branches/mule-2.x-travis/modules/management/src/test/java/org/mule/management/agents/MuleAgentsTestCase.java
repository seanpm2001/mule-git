/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.management.agents;

import org.mule.tck.AbstractMuleTestCase;

import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

public class MuleAgentsTestCase extends AbstractMuleTestCase
{
    public MuleAgentsTestCase()
    {
        setStartContext(true);
    }

    public void testRemoveNonExistentAgent() throws Exception
    {
        managementContext.getRegistry().unregisterAgent("DOES_NOT_EXIST");
        // should not throw NPE
    }

    public void testAgentsRegistrationOrder() throws Exception
    {
        managementContext.getRegistry().getConfiguration().setId("MuleAgentsTestCase.agentsRegistrationOrder");
        JmxAgent agentFirst = new JmxAgent();
        // If you specified "JmxAgent", it was the first one in the map,
        // but for "jmxAgent" the order was not preserved.
        // MX4JAgent depends on JmxAgent having finished initilisation
        // before proceeding, otherwise it is not able to find any
        // MBeanServer.
        agentFirst.setName("jmxAgent");
        managementContext.getRegistry().registerAgent(agentFirst);

        Mx4jAgent agentSecond = new Mx4jAgent();
        agentSecond.setName("mx4jAgent");
        managementContext.getRegistry().registerAgent(agentSecond);

        // should not throw an exception
    }

    /**
     * Should not bark when the MBeanServer is injected and
     * {@code locateServer} and {@code createServer} both
     * set to false.
     */
    public void testJmxAgentInjectedMBeanServer() throws Exception
    {
        managementContext.getRegistry().getConfiguration().setId("MuleAgentsTestCase.jmxAgentInjectedMBeanServer");
        JmxAgent jmxAgent = new JmxAgent();
        List servers = MBeanServerFactory.findMBeanServer(null);
        MBeanServer server;
        server = servers == null || servers.isEmpty()
                ? MBeanServerFactory.createMBeanServer()
                : (MBeanServer) servers.get(0);
        jmxAgent.setCreateServer(false);
        jmxAgent.setLocateServer(false);
        jmxAgent.setMBeanServer(server);
        managementContext.getRegistry().registerAgent(jmxAgent);
    }
}
