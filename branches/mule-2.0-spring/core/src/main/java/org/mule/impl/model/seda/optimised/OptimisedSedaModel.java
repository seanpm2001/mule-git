/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.impl.model.seda.optimised;

import org.mule.impl.MuleDescriptor;
import org.mule.impl.model.seda.SedaModel;
import org.mule.umo.UMOComponent;
import org.mule.umo.UMODescriptor;

/**
 * A Mule component service model that uses Seda principles to achieve high
 * throughput by queueing events for components and processing them concurrently.
 */
public class OptimisedSedaModel extends SedaModel
{

    /**
     * Returns the model type name. This is a friendly identifier that is used to
     * look up the SPI class for the model
     * 
     * @return the model type
     */
    public String getType()
    {
        return "seda-optimised";
    }

    public UMOComponent createComponent(UMODescriptor descriptor)
    {
        return new OptimisedSedaComponent((MuleDescriptor)descriptor, this);
    }
}
