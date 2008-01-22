/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tck.functional;

import org.mule.api.TransactionException;
import org.mule.api.UMOEventContext;
import org.mule.api.UMOTransaction;
import org.mule.imple.config.i18n.MessageFactory;

/**
 * This component is useful for unit tests involving transactionality because it
 * will roll back the current transaction upon message arrival.  
 */
public class TransactionalFunctionalTestComponent extends FunctionalTestComponent
{
    private boolean expectTransaction = true;
    private boolean rollback = true;

    /** {@inheritDoc} */
    public Object onCall(UMOEventContext context) throws Exception
    {
        Object replyMessage = super.onCall(context);

        if (expectTransaction)
        {
            // Verify transaction has begun.
            UMOTransaction currentTx = context.getCurrentTransaction();
            if (currentTx == null || currentTx.isBegun() == false)
            {    
                context.setStopFurtherProcessing(true);
                throw new TransactionException(MessageFactory.createStaticMessage("Trying to roll back transaction but no transaction is underway."));
            }            

            if (rollback)
            {
                // Mark the transaction for rollback.
                logger.info("@@@@ Rolling back transaction @@@@");
                currentTx.setRollbackOnly();
            }        
        }
        
        return replyMessage;
    }

    public boolean isRollback()
    {
        return rollback;
    }

    public void setRollback(boolean rollback)
    {
        this.rollback = rollback;
    }

    public boolean isExpectTransaction()
    {
        return expectTransaction;
    }

    public void setExpectTransaction(boolean expectTransaction)
    {
        this.expectTransaction = expectTransaction;
    }
}

