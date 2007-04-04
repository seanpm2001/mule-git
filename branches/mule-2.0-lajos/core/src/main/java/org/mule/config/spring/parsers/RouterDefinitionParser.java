/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.config.spring.parsers;

import org.w3c.dom.Element;

/**
 * TODO
 */
public class RouterDefinitionParser extends SimpleChildDefinitionParser
{

    public RouterDefinitionParser(String setterMethod, Class clazz)
    {
        super(setterMethod, clazz);
        registerValueMapping("enableCorrelation", "IF_NOT_SET=0,ALWAYS=1,NEVER=2");
        registerAttributeMapping("transformers", "transformer");
    }


    public boolean isCollection(Element element)
    {
        return true;
    }
}
