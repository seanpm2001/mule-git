/*
 * $Header$
 * $Revision$
 * $Date$
 * ------------------------------------------------------------------------------------------------------
 *
 * Copyright (c) Cubis Limited. All rights reserved.
 * http://www.cubis.co.uk
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.mule.umo.routing;

import org.mule.umo.UMOEvent;

/**
 * <code>CouldNotRouteInboundEventException</code> thrown if the current component cannot
 * accept the inbound event
 *
 * @author <a href="mailto:ross.mason@cubis.co.uk">Ross Mason</a>
 * @version $Revision$
 */

public class CouldNotRouteInboundEventException extends RoutingException
{
    private UMOEvent event;

    public CouldNotRouteInboundEventException(String message, UMOEvent event)
    {
        super(message);
        this.event = event;
    }

    public CouldNotRouteInboundEventException(String message, Throwable cause, UMOEvent event)
    {
        super(message, cause);
        this.event = event;
    }

    public UMOEvent getEvent()
    {
        return event;
    }


}
