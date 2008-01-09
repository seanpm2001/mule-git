/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.providers.ejb.config;

import org.mule.config.spring.factories.InboundEndpointFactoryBean;
import org.mule.config.spring.factories.OutboundEndpointFactoryBean;
import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.config.spring.parsers.generic.MuleOrphanDefinitionParser;
import org.mule.config.spring.parsers.specific.endpoint.TransportEndpointDefinitionParser;
import org.mule.config.spring.parsers.specific.endpoint.TransportGlobalEndpointDefinitionParser;
import org.mule.impl.endpoint.URIBuilder;
import org.mule.providers.ejb.EjbConnector;
import org.mule.providers.rmi.config.RmiNamespaceHandler;

public class EjbNamespaceHandler extends AbstractMuleNamespaceHandler 
{

    public void init()
    {
        registerMuleBeanDefinitionParser("endpoint", new TransportGlobalEndpointDefinitionParser(EjbConnector.EJB, TransportGlobalEndpointDefinitionParser.PROTOCOL, true, RmiNamespaceHandler.ADDRESS, RmiNamespaceHandler.PROPERTIES)).addAlias(RmiNamespaceHandler.OBJECT, URIBuilder.PATH);
        registerMuleBeanDefinitionParser("inbound-endpoint", new TransportEndpointDefinitionParser(EjbConnector.EJB, TransportGlobalEndpointDefinitionParser.PROTOCOL, true, InboundEndpointFactoryBean.class, RmiNamespaceHandler.ADDRESS, RmiNamespaceHandler.PROPERTIES)).addAlias(RmiNamespaceHandler.OBJECT, URIBuilder.PATH);
        registerMuleBeanDefinitionParser("outbound-endpoint", new TransportEndpointDefinitionParser(EjbConnector.EJB, TransportGlobalEndpointDefinitionParser.PROTOCOL, true, OutboundEndpointFactoryBean.class, RmiNamespaceHandler.ADDRESS, RmiNamespaceHandler.PROPERTIES)).addAlias(RmiNamespaceHandler.OBJECT, URIBuilder.PATH);
        registerBeanDefinitionParser("connector", new MuleOrphanDefinitionParser(EjbConnector.class, true));
    }

}