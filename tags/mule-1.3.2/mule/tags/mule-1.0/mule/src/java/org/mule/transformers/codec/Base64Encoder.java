/*
 * $Header$
 * $Revision$
 * $Date$
 * ------------------------------------------------------------------------------------------------------
 *
 * Copyright (c) SymphonySoft Limited. All rights reserved.
 * http://www.symphonysoft.com
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.mule.transformers.codec;

import org.mule.transformers.DefaultTransformer;
import org.mule.umo.transformer.TransformerException;
import sun.misc.BASE64Encoder;

/**
 * <code>Base64Encoder</code> transforms strings or byte arrays into Base64 encoded
 * string
 *
 * @author <a href="mailto:ross.mason@symphonysoft.com">Ross Mason</a>
 * @version $Revision$
 */
public class Base64Encoder extends DefaultTransformer
{
    private BASE64Encoder encoder;

    public Base64Encoder()
    {
        registerSourceType(String.class);
        registerSourceType(byte[].class);
        setReturnClass(String.class);
        encoder = new BASE64Encoder();
    }

    /* (non-Javadoc)
     * @see org.mule.umo.transformer.UMOTransformer#transform(java.lang.Object)
     */
    public Object doTransform(Object src) throws TransformerException
    {
        byte[] buf;
        if(src instanceof String) {
            buf = src.toString().getBytes();
        } else {
            buf = (byte[])src;
        }
        String result = encoder.encode(buf);
        if(getReturnClass().equals(byte[].class)) {
            return result.getBytes();
        }
        return result;
    }
}
