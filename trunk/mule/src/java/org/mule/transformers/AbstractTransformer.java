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
 *
 */
package org.mule.transformers;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.InitialisationException;
import org.mule.umo.UMOMessage;
import org.mule.umo.endpoint.UMOImmutableEndpoint;
import org.mule.umo.transformer.TransformerException;
import org.mule.umo.transformer.UMOTransformer;
import org.mule.util.ClassHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <code>AbstractTransformer</code> Is a base class for all transformers.
 * Transformations transform one object into another.
 *
 * @author Ross Mason
 * @version $Revision$
 */

public abstract class AbstractTransformer implements UMOTransformer
{
    /**
     * The fully qualified class name of the fallback <code>Transformer</code>
     * implementation class to use, if no other can be found.
     */
    public static final String TRANSFORMER_DEFAULT = "org.mule.transformers.DefaultTransformer";

    /**
     * logger used by this class
     */
    protected transient Log logger = LogFactory.getLog(getClass());

    protected Class returnClass = null;

    protected String name = null;

    protected UMOImmutableEndpoint endpoint = null;

    private List sourceTypes = new ArrayList();

    protected UMOTransformer transformer;

    /**
     * default constructor required for discovery
     */
    public AbstractTransformer()
    {
        name = generateTransformerName();
    }

    protected Object checkReturnClass(Object object) throws TransformerException
    {
        if (returnClass != null)
        {
            if (!returnClass.isInstance(object))
            {
                throw new TransformerException("The object transformed is of type: "
                        + object.getClass().getName()
                        + ", but the expected return type is: "
                        + returnClass.getName());
            }
        }
        logger.debug("The transformed object is of expected type. Type is: " + object.getClass().getName());
        return object;
    }

    protected void registerSourceType(Class aClass)
    {
        if (aClass.equals(Object.class))
        {
            logger.warn("java.lang.Object has been added as an exceptable sourcetype to this transformer, there will no source type checking on this transformer");
        }
        sourceTypes.add(aClass);
    }

    protected void unregisterSourceType(Class aClass)
    {
        sourceTypes.remove(aClass);
    }

    protected Iterator getSourceTypeClassesIterator()
    {
        return sourceTypes.iterator();
    }

    /**
     * @return
     */
    public String getName()
    {
        if (name == null)
        {
            name = getClass().getName();
            name = name.substring(name.lastIndexOf(".") + 1);
        }
        logger.debug("Setting transformer name to: " + name);
        return name;
    }

    /**
     * @param string
     */
    public void setName(String string)
    {
        name = string;
    }

    /* (non-Javadoc)
     * @see org.mule.transformers.Transformer#getReturnClass()
     */
    public Class getReturnClass()
    {
        return returnClass;
    }

    /* (non-Javadoc)
     * @see org.mule.transformers.Transformer#setReturnClass(java.lang.String)
     */
    public void setReturnClass(Class newClass)
    {
        returnClass = newClass;
    }

    public boolean isSourceTypeSupported(Class aClass)
    {
        if (sourceTypes.isEmpty())
        {
            return true;
        }
        Class anotherClass = null;
        for (Iterator i = getSourceTypeClassesIterator(); i.hasNext();)
        {
            anotherClass = (Class) i.next();
            if (anotherClass.isAssignableFrom(aClass))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Transforms the object.
     *
     * @param src The source object to transform.
     * @return The transformed object
     */
    public final Object transform(Object src) throws TransformerException
    {
        Object result = null;
        if(src instanceof UMOMessage) {
            src = ((UMOMessage)src).getPayload();
        }
        
        if (!isSourceTypeSupported(src.getClass()))
        {
            throw new TransformerException("This transformer: " + getName() + " does not support this source type: " + src.getClass().getName() + ". Endpoint is: " + endpoint.getEndpointURI());
        } else {
            result = doTransform(src);
            result = checkReturnClass(result);
            if (transformer != null)
            {
                result = transformer.transform(result);
            }
        }
        return result;
    }

    public UMOImmutableEndpoint getEndpoint()
    {
        return endpoint;
    }

    /* (non-Javadoc)
     * @see org.mule.umo.transformer.UMOTransformer#setConnector(org.mule.umo.provider.UMOConnector)
     */
    public void setEndpoint(UMOImmutableEndpoint endpoint)
    {
        this.endpoint = endpoint;
        UMOTransformer trans = transformer;
        while (trans != null && endpoint != null)
        {
            trans.setEndpoint(endpoint);
            trans = trans.getTransformer();
        }

    }

    public abstract Object doTransform(Object src) throws TransformerException;

    /* (non-Javadoc)
     * @see org.mule.umo.transformer.UMOTransformer#getTransformer()
     */
    public UMOTransformer getTransformer()
    {
        return transformer;
    }

    /* (non-Javadoc)
     * @see org.mule.umo.transformer.UMOTransformer#setTransformer(org.mule.umo.transformer.UMOTransformer)
     */
    public void setTransformer(UMOTransformer transformer)
    {
        this.transformer = transformer;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException
    {
        try
        {
            Object c = BeanUtils.cloneBean(this);
            return (UMOTransformer) c;
        }
        catch (Exception e)
        {
            throw new CloneNotSupportedException("Failed to clone transformer: " + e.getMessage());
        }
    }

    /**
     * Will return the return type for the last transformer in the chain
     *
     * @return the last transformers return type
     */
    public Class getFinalReturnClass()
    {
        UMOTransformer tempTrans = this;
        UMOTransformer returnTrans = this;
        while (tempTrans != null)
        {
            returnTrans = tempTrans;
            tempTrans = tempTrans.getTransformer();
        }
        return returnTrans.getReturnClass();
    }

    /**
     * Template method were deriving classes can do any initialisation
     * after the properties have been set on this transformer
     * @throws InitialisationException
     */
    public void initialise() throws InitialisationException
    {

    }

    protected String generateTransformerName() {
        String name = getClass().getName();
        int i = name.lastIndexOf(".");
        if(i > -1) {
            name = name.substring(i + 1);
        }
        return name;
        //String temp = name.substring(0,3).toLowerCase();
        //return temp + name.substring(3);
    }

    /**
     * Convenience method to register source types using a bean property setter
     * @param type the fully qualified class name
     * @throws ClassNotFoundException is thrown if the class is not on theclasspath
     */
    public void setSourceType(String type) throws ClassNotFoundException
    {
        Class clazz = ClassHelper.loadClass(type, getClass());
        registerSourceType(clazz);
    }

}