/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.jms;

import org.mule.config.i18n.CoreMessages;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.Messages;
import org.mule.transaction.AbstractSingleResourceTransaction;
import org.mule.transaction.IllegalTransactionStateException;
import org.mule.umo.TransactionException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * <code>JmsTransaction</code> is a wrapper for a JMS local transaction. This
 * object holds the JMS session and controls when the transaction is committed or
 * rolled back.
 */
public class JmsTransaction extends AbstractSingleResourceTransaction
{
    public void bindResource(Object key, Object resource) throws TransactionException
    {
        if (!(key instanceof Connection) || !(resource instanceof Session))
        {
            throw new IllegalTransactionStateException(new Message(
                Messages.TX_CAN_ONLY_BIND_TO_X_TYPE_RESOURCES, "javax.jms.Connection/javax.jms.Session"));
        }

        Session session = (Session)resource;
        try
        {
            if (!session.getTransacted())
            {
                throw new IllegalTransactionStateException(new Message("jms", 4));
            }
        }
        catch (JMSException e)
        {
            throw new IllegalTransactionStateException(CoreMessages.transactionCannotReadState(), e);
        }

        super.bindResource(key, resource);
    }

    protected void doBegin() throws TransactionException
    {
        // do nothing
    }

    protected void doCommit() throws TransactionException
    {
        try
        {
            ((Session)resource).commit();
        }
        catch (JMSException e)
        {
            throw new TransactionException(CoreMessages.transactionCommitFailed(), e);
        }
    }

    protected void doRollback() throws TransactionException
    {
        try
        {
            ((Session)resource).rollback();
        }
        catch (JMSException e)
        {
            throw new TransactionException(CoreMessages.transactionRollbackFailed(), e);
        }
    }
}
