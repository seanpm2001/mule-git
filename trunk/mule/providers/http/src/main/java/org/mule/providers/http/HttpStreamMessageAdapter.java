/*
 * $Id: $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the BSD style
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.providers.http;

import org.apache.commons.httpclient.HttpMethod;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.Messages;
import org.mule.providers.streaming.StreamMessageAdapter;
import org.mule.umo.provider.OutputHandler;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A HttpStream adapter that can be used with the HttpClinetMessageDispatcher as knows when to release the Http Connection.
 */
public class HttpStreamMessageAdapter extends StreamMessageAdapter

{


    public HttpStreamMessageAdapter(InputStream in)
    {
        super(in);
    }

    public HttpStreamMessageAdapter(InputStream in, OutputStream out)
    {
        super(in, out);
    }

    public HttpStreamMessageAdapter(OutputHandler handler)
    {
        super(handler);
    }

    public HttpStreamMessageAdapter(OutputStream out, OutputHandler handler)
    {
        super(out, handler);
    }

    public HttpStreamMessageAdapter(InputStream in, OutputStream out, OutputHandler handler)
    {
        super(in, out, handler);
    }

    
    protected HttpMethod httpMethod;


    public HttpMethod getHttpMethod()
    {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod)
    {
        this.httpMethod = httpMethod;
    }

    public void release()
    {
        if(httpMethod==null)
        {
            throw new IllegalStateException(new Message(Messages.X_IS_NULL, "httpMethod object").toString());
        }
        else
        {
            httpMethod.releaseConnection();
        }
    }
}
