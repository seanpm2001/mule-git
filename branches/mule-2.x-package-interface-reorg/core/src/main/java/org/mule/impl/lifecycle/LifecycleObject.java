/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.impl.lifecycle;

import org.mule.api.MuleContext;
import org.mule.api.context.UMOServerNotification;

/**
 * TODO
 */
public class LifecycleObject
{
    private Class type;
    private UMOServerNotification preNotification;
    private UMOServerNotification postNotification;

    public LifecycleObject(Class type)
    {
        this.type = type;
    }

    public UMOServerNotification getPostNotification()
    {
        return postNotification;
    }

    public void setPostNotification(UMOServerNotification postNotification)
    {
        this.postNotification = postNotification;
    }

    public UMOServerNotification getPreNotification()
    {
        return preNotification;
    }

    public void setPreNotification(UMOServerNotification preNotification)
    {
        this.preNotification = preNotification;
    }

    public Class getType()
    {
        return type;
    }

    public void setType(Class type)
    {
        this.type = type;
    }

    public void firePreNotification(MuleContext context)
    {
        if(preNotification!=null)
        {
            context.fireNotification(preNotification);
        }
    }

    public void firePostNotification(MuleContext context)
    {
        if(postNotification!=null)
        {
            context.fireNotification(postNotification);
        }
    }
}