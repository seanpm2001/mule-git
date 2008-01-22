/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.management;

import org.mule.api.context.UMOServerNotification;
import org.mule.api.context.UMOServerNotificationListener;

public class DummyListener implements UMOServerNotificationListener
{

    public void onNotification(UMOServerNotification notification)
    {
        // empty
    }

}
