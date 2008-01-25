/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tck.testmodels.mule;

import org.mule.api.MuleException;
import org.mule.api.lifecycle.LifecycleAdapter;
import org.mule.api.lifecycle.LifecycleAdapterFactory;
import org.mule.api.model.EntryPointResolverSet;
import org.mule.api.service.Service;


public class TestDefaultLifecycleAdapterFactory implements LifecycleAdapterFactory
{
    public TestDefaultLifecycleAdapterFactory()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.api.lifecycle.LifecycleAdapterFactory#create(java.lang.Object,
     *      org.mule.api.UMODescriptor, org.mule.api.model.EntryPointResolver)
     */
    public LifecycleAdapter create(Object pojoService,
                                      Service service,
                                      EntryPointResolverSet resolver) throws MuleException
    {
        return new TestDefaultLifecycleAdapter(pojoService, service, resolver);
    }

}
