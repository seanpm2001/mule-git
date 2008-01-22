/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.util.file;

import org.mule.util.xa.AbstractTransactionContext;
import org.mule.util.xa.AbstractXAResourceManager;
import org.mule.util.xa.ResourceManagerException;

import javax.transaction.xa.XAResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileManager extends AbstractXAResourceManager
{

    private static Log logger = LogFactory.getLog(FileManager.class);

    public synchronized FileSession createSession()
    {
        return new TransactedFileSession(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transaction.xa.AbstractResourceManager#getLogger()
     */
    protected Log getLogger()
    {
        return logger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transaction.xa.AbstractResourceManager#createTransactionContext(java.lang.Object)
     */
    protected AbstractTransactionContext createTransactionContext(Object session)
    {
        return new FileTransactionContext();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transaction.xa.AbstractResourceManager#doBegin(org.mule.impl.transaction.xa.AbstractTransactionContext)
     */
    protected void doBegin(AbstractTransactionContext context)
    {
        // template method
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transaction.xa.AbstractResourceManager#doPrepare(org.mule.impl.transaction.xa.AbstractTransactionContext)
     */
    protected int doPrepare(AbstractTransactionContext context)
    {
        return XAResource.XA_OK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transaction.xa.AbstractResourceManager#doCommit(org.mule.impl.transaction.xa.AbstractTransactionContext)
     */
    protected void doCommit(AbstractTransactionContext context) throws ResourceManagerException
    {
        // template method
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.impl.transaction.xa.AbstractResourceManager#doRollback(org.mule.impl.transaction.xa.AbstractTransactionContext)
     */
    protected void doRollback(AbstractTransactionContext context) throws ResourceManagerException
    {
        // template method
    }

    protected class FileTransactionContext extends AbstractTransactionContext
    {
        // nothing here yet
    }

}
