/*
 * $Id: Persistable.java 3649 2006-10-24 10:09:08Z holger $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.registry.persistence;

import org.mule.umo.UMOException;
import org.mule.umo.manager.UMOServerNotificationListener;

/**
 * <code>Persistable</code> is an interface for a Mule object that wants
 * to be persisted somewhere. Right now, this interface is envisioned for
 * the registry itself, but it can be extended to other components.
 * 
 * @author 
 * @version $Revision: 3649 $
 */
public interface Persistable
{
    /**
     * Registers a listener from the Persistence Manager. This is
     * used by the component to alert the Manager to queue up the
     * persistence request.
     *
     * @param listener The listener that the component will talk to
     * @throws UMOException if the registration fails
     */
    void registerPersistenceRequestListener(UMOServerNotificationListener listener) throws UMOException;

    /**
     * Return the data for persistence. It is the responsibility
     * of the component to handle any locks, copies, deep copies,
     * or synchronizations necessary in order to preserve a consistent
     * backup.
     *
     * @returns the data to be persisted
     * @throws UMOException if the registration fails
     */

    Object getPersistableObject() throws UMOException;
}

