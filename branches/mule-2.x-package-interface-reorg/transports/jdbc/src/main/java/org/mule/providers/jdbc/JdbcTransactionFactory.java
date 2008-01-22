/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.jdbc;

import org.mule.api.MuleContext;
import org.mule.api.TransactionException;
import org.mule.api.UMOTransaction;
import org.mule.api.UMOTransactionFactory;

/**
 * TODO
 */
public class JdbcTransactionFactory implements UMOTransactionFactory
{

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.api.UMOTransactionFactory#beginTransaction()
     */
    public UMOTransaction beginTransaction(MuleContext muleContext) throws TransactionException
    {
        JdbcTransaction tx = new JdbcTransaction();
        tx.begin();
        return tx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.api.UMOTransactionFactory#isTransacted()
     */
    public boolean isTransacted()
    {
        return true;
    }

}
