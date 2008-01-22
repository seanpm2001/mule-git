/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.impl.container;

import org.mule.api.context.ContainerException;
import org.mule.api.context.ObjectNotFoundException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.registry.RegistrationException;
import org.mule.util.TemplateParser;

import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides a facade for accessing System properties and properties on the
 * MuleContext. This container context serves 3 functions -
 * <ol>
 * <li>Allows System properties to be set in Mule Xml (by setting the
 * #systemProperties Map)
 * <li>Allows one to load System properties into the mule context so that MuleXml
 * templates referring to System properties can be used (i.e. ${os.name}).
 * <li>Provides a consistent way to set abitary properties on the Management
 * Context. Setting properties on this container context is equivilent to using the
 * <environment-properties> element in Mule Xml. The latter element may be removed in
 * the future.
 * </ol>
 */
public class PropertiesContainerContext extends AbstractContainerContext implements TemplateParser.TemplateCallback
{

    protected Map systemProperties;
    protected Map properties;
    protected boolean includeSystemProperties = true;
    protected boolean enableTemplates = false;

    protected TemplateParser templateParser = TemplateParser.createAntStyleParser();

    public PropertiesContainerContext()
    {
        super("properties");
    }

    //@Override
    public void initialise() throws InitialisationException
    {
        if (properties != null)
        {
            Map.Entry entry;
            String value;
            for (Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();)
            {
                entry = (Map.Entry)iterator.next();
                value = entry.getValue().toString();
                value = templateParser.parse(this, value);
                try
                {
                    muleContext.getRegistry().registerObject(entry.getKey().toString(), value, muleContext);
                }
                catch (RegistrationException e)
                {
                    throw new InitialisationException(e, this);
                }
            }
        }
        setSystemProperties(null);
    }

    public Object match(String token)
    {
        return muleContext.getRegistry().lookupObject(token);
    }

    public void configure(Reader configuration) throws ContainerException
    {
        throw new UnsupportedOperationException("configure");
    }

    /**
     * Queries a component from the underlying container. For this container it will
     * look up a property on the Mule Management Context.
     * 
     * @param key the key fo find the component with. It's up to the individual
     *            implementation to check the type of this key and look up objects
     *            accordingly
     * @return the component found in the container
     * @throws org.mule.api.context.ObjectNotFoundException if the component is not
     *             found
     */
    public Object getComponent(Object key) throws ObjectNotFoundException
    {
        if (key == null)
        {
            throw new ObjectNotFoundException("null");
        }
        Object value = muleContext.getRegistry().lookupObject(key.toString());
        if (value == null)
        {
            throw new ObjectNotFoundException(key.toString());
        }
        if (value instanceof String && enableTemplates)
        {
            value = templateParser.parse(properties, value.toString());
        }
        return value;
    }

    public Map getSystemProperties()
    {
        return systemProperties;
    }

    public void setSystemProperties(Map properties)
    {
        this.systemProperties = properties;
        String value;
        Map.Entry entry;
        if (systemProperties != null)
        {
            for (Iterator iterator = systemProperties.entrySet().iterator(); iterator.hasNext();)
            {
                entry = (Map.Entry) iterator.next();
                value = entry.getValue().toString();
                value = templateParser.parse(systemProperties, value);
                value = templateParser.parse(this, value);
                System.setProperty(entry.getKey().toString(), value);
            }
        }

        if (includeSystemProperties)
        {
            Map props = System.getProperties();

            for (Iterator iterator = props.entrySet().iterator(); iterator.hasNext();)
            {
                entry = (Map.Entry) iterator.next();
                value = entry.getValue().toString();
                value = templateParser.parse(this, value.toString());
                try
                {
                    muleContext.getRegistry().registerObject(entry.getKey().toString(), value, muleContext);
                }
                catch (RegistrationException e)
                {
                    logger.error(e);
                }
            }
        }
    }

    public Map getProperties()
    {
        return properties;
    }

    public void setProperties(Map properties)
    {
        this.properties = properties;
    }


    public boolean isIncludeSystemProperties()
    {
        return includeSystemProperties;
    }

    public void setIncludeSystemProperties(boolean includeSystemProperties)
    {
        this.includeSystemProperties = includeSystemProperties;
    }

    public boolean isEnableTemplates()
    {
        return enableTemplates;
    }

    public void setEnableTemplates(boolean enableTemplates)
    {
        this.enableTemplates = enableTemplates;
    }
}
