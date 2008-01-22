/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transformers.xml;

import org.mule.api.transformer.UMOTransformer;
import org.mule.tck.testmodels.fruit.Apple;

public class XmlObjectTransformersTestCase extends AbstractXmlTransformerTestCase
{
    private Apple testObject = null;

    public XmlObjectTransformersTestCase()
    {
        testObject = new Apple();
        testObject.wash();
    }

    public UMOTransformer getTransformer() throws Exception
    {
        return new ObjectToXml();
    }

    public UMOTransformer getRoundTripTransformer() throws Exception
    {
        return new XmlToObject();
    }

    public Object getTestData()
    {
        return testObject;
    }

    public Object getResultData()
    {
        return "<org.mule.tck.testmodels.fruit.Apple>\n" + "  <bitten>false</bitten>\n"
               + "  <washed>true</washed>\n" + "</org.mule.tck.testmodels.fruit.Apple>";
    }
}
