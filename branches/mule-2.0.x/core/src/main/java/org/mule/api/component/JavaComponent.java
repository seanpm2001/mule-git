/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.api.component;

import org.mule.api.lifecycle.LifecycleAdapterFactory;
import org.mule.api.model.EntryPointResolverSet;
import org.mule.api.object.ObjectFactory;
import org.mule.api.routing.NestedRouterCollection;

/**
 * <code>JavaComponent</code> is a Java {@link Component} implementation used to
 * invoke Java component implementations. A <code>JavaComponent</code> uses an
 * {@link ObjectFactory} to specify the object instance's source and allows for
 * singleton and prototype implementations to be used along with other custom
 * {@link ObjectFactory} that allow component instances to be obtained from
 * containers such as Spring. A <code>JavaComponent</code> uses a customizable
 * {@link EntryPointResolverSet} in order to resolve which method should be used for
 * invocation and allows java bindings to be configure. Java Component bindings, if
 * implemented by the JavaComponent implementation, uses a component instance proxy
 * to implement interface methods using calls to outbound endpoints.
 */
public interface JavaComponent extends Component
{

    /**
     * A {@link JavaComponent} can have a custom entry-point resolver for its own
     * object. By default this is null. When set this resolver will override the
     * resolver on the model
     * 
     * @return Null is a resolver set has not been set otherwise the resolver to use
     *         on this service
     */
    EntryPointResolverSet getEntryPointResolverSet();

    /**
     * A {@link JavaComponent} can have a custom entry-point resolver for its own
     * object. By default this is null. When set this resolver will override the
     * resolver on the model
     * 
     * @return Null is a resolver set has not been set otherwise the resolver to use
     *         on this service
     */
    void setEntryPointResolverSet(EntryPointResolverSet entryPointResolverSet);

    // TODO This should be renamed to something like "Bindings", moved up to
    // Component. NestedRouter should also be renamed to "Binding" and made more
    // generic so as to support other types of bindings e.g. wsdl port -> ws-endpont,
    // or script context variable -> outbound endpoint etc. See MULE-3114
    NestedRouterCollection getNestedRouter();

    void setNestedRouter(NestedRouterCollection nestedRouter);

    /**
     * The object factory used to obtain the component object instance. Mule core
     * provides two implementations: {@link SingletonObjectFactory} and
     * {@link PrototypeObjectFactory}.<br/> The spring-config module provides an
     * {@link ObjectFactory} implementation that delegates to spring. There is no
     * PooledObjectFactory, the {@link PooledJavaComponent} should be used for
     * pooling.
     * 
     * @param objectFactory
     */
    void setObjectFactory(ObjectFactory objectFactory);

    /**
     * @return
     */
    ObjectFactory getObjectFactory();

    /**
     * @return
     */
    Class getObjectType();

    /**
     * Returns the factory used create life-cycle adaptors that are used to wrap
     * component instance.
     * 
     * @return
     */
    public LifecycleAdapterFactory getLifecycleAdaptorFactory();

    /**
     * Sets the factory used create life-cycle adaptors that are used to wrap
     * component instance.
     * 
     * @param lifecycleAdaptor
     */
    public void setLifecycleAdaptorFactory(LifecycleAdapterFactory lifecycleAdaptor);
}
