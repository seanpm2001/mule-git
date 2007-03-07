/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.test.transformers;

import org.mule.tck.AbstractTransformerTestCase;

import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;

/**
 * Use this superclass if you intend to compare Xml contents.
 */
public abstract class AbstractXmlTransformerTestCase extends AbstractTransformerTestCase
{

    protected AbstractXmlTransformerTestCase()
    {
        super();
        XMLUnit.setIgnoreWhitespace(true);
    }

    // @Override
    public boolean compareResults(Object expected, Object result)
    {
        if (expected instanceof Document && result instanceof Document)
        {
            return XMLUnit.compareXML((Document)expected, (Document)result).similar();
        }
        else if (expected instanceof String && result instanceof String)
        {
            try
            {
                String expectedString = this.normalizeString((String)expected);
                String resultString = this.normalizeString((String)result);
                return XMLUnit.compareXML(expectedString, resultString).similar();
            }
            catch (Exception ex)
            {
                return false;
            }
        }

        // all other comparisons are passed up
        return super.compareResults(expected, result);
    }

}
