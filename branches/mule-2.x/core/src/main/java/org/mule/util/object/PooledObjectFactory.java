/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.util.object;

import org.mule.config.PoolingProfile;
import org.mule.umo.lifecycle.Disposable;

import java.util.Map;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * ObjectFactory backed by a Commons ObjectPool.  The characteristics of the ObjectPool can be 
 * customized by setting a PoolingProfile.
 */
public class PooledObjectFactory extends AbstractPooledObjectFactory implements PoolableObjectFactory
{
    /** 
     * Active instances of the object which have been created.  
     */
    private GenericObjectPool pool = null;
    
    /** For Spring only */
    public PooledObjectFactory() { super(); }
    
    public PooledObjectFactory(Class objectClass) { super(objectClass); }

    public PooledObjectFactory(Class objectClass, Map properties) { super(objectClass, properties); }
    
    public PooledObjectFactory(Class objectClass, PoolingProfile poolingProfile) { super(objectClass, poolingProfile); }

    public PooledObjectFactory(Class objectClass, Map properties, PoolingProfile poolingProfile) { super(objectClass, properties, poolingProfile); }
    
    protected void initialisePool() 
    {
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        if (poolingProfile != null)
        {
            config.maxIdle = poolingProfile.getMaxIdle();
            config.maxActive = poolingProfile.getMaxActive();
            config.maxWait = poolingProfile.getMaxWait();
            config.whenExhaustedAction = (byte) poolingProfile.getExhaustedAction();
        }
        pool = new GenericObjectPool(this, config);
    }

    public void dispose()
    {
        if (pool != null)
        {
            try 
            {
                // TODO Ideally we should call Disposable.dispose() on each object in the pool before destroying it.
                pool.close();
            }
            catch (Exception e)
            {
                logger.warn(e);
            }
            finally
            {
                pool = null;
            }
        }
    }

    /**
     * Creates a new instance of the object on each call.
     */
    public Object getOrCreate() throws Exception
    {
        return pool.borrowObject();
    }

    /** {@inheritDoc} */
    public Object lookup(String id) throws Exception
    {
        throw new UnsupportedOperationException("This operation is only supported by the PooledIdentifiableObjectFactory");
    }

    /** 
     * Returns the object instance to the pool.
     */
    public void release(Object object) throws Exception
    {
        pool.returnObject(object);
    }
    
    public int getPoolSize()
    {
        if (pool != null)
        {
            return pool.getNumActive();
        }
        else
        {
            return 0;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PoolableObjectFactory Interface
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Object makeObject() throws Exception
    {
        return super.getOrCreate();
    }

    public void destroyObject(Object obj) throws Exception
    {
        if (obj instanceof Disposable)
        {
            ((Disposable) obj).dispose();
        }
    }

    public void activateObject(Object obj) throws Exception
    {
        // nothing to do
    }

    public void passivateObject(Object obj) throws Exception
    {
        // nothing to do
    }

    public boolean validateObject(Object obj)
    {
        return true;
    }
}
