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
package org.mule.management;

import org.mule.umo.UMOException;

/**
 * <code>ManagementException</code> is a general exception thrown by management
 * extensions
 *
 * @author <a href="mailto:ross.mason@cubis.co.uk">Ross Mason</a>
 * @version $Revision$
 */
public abstract class ManagementException extends UMOException
{
    /**
     * @param message the exception message
     */
    public ManagementException(String message)
    {
        super(message);
    }

    /**
     * @param message the exception message
     * @param cause   the exception that cause this exception to be thrown
     */
    public ManagementException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
