/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extras.jaas.filters;

import org.mule.api.UMOEvent;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.security.CredentialsNotSetException;
import org.mule.api.security.CryptoFailureException;
import org.mule.api.security.EncryptionStrategyNotFoundException;
import org.mule.api.security.SecurityException;
import org.mule.api.security.SecurityProviderNotFoundException;
import org.mule.api.security.UMOAuthentication;
import org.mule.api.security.UMOCredentials;
import org.mule.api.security.UMOSecurityContext;
import org.mule.api.security.UnauthorisedException;
import org.mule.api.security.UnknownAuthenticationTypeException;
import org.mule.extras.jaas.JaasAuthentication;
import org.mule.impl.security.AbstractEndpointSecurityFilter;
import org.mule.impl.security.MuleCredentials;
import org.mule.impl.security.MuleHeaderCredentialsAccessor;
import org.mule.imple.config.i18n.CoreMessages;

public class JaasSecurityFilter extends AbstractEndpointSecurityFilter
{

    public JaasSecurityFilter()
    {
        setCredentialsAccessor(new MuleHeaderCredentialsAccessor());
    }

    protected final void authenticateInbound(UMOEvent event)
        throws SecurityException, CryptoFailureException, EncryptionStrategyNotFoundException,
        UnknownAuthenticationTypeException
    {
        String userHeader = (String) getCredentialsAccessor().getCredentials(event);
        if (userHeader == null)
        {
            throw new CredentialsNotSetException(event.getMessage(), event.getSession().getSecurityContext(),
                event.getEndpoint(), this);
        }

        UMOCredentials user = new MuleCredentials(userHeader, getSecurityManager());
        UMOAuthentication authResult;
        UMOAuthentication umoAuthentication = new JaasAuthentication(user);
        try
        {
            authResult = getSecurityManager().authenticate(umoAuthentication);
        }
        catch (SecurityException se)
        {
            // Security Exception occurred
            if (logger.isDebugEnabled())
            {
                logger.debug("Security Exception raised. Authentication request for user: " + user.getUsername() 
                    + " failed: " + se.toString());
            }
            throw se;
        }
        catch (Exception e)
        {
            // Authentication failed
            if (logger.isDebugEnabled())
            {
                logger.debug("Authentication request for user: " + user.getUsername() 
                    + " failed: " + e.toString());
            }
            throw new UnauthorisedException(CoreMessages.authFailedForUser(user.getUsername()),
                event.getMessage(), e);
        }

        // Authentication success
        if (logger.isDebugEnabled())
        {
            logger.debug("Authentication success: " + authResult.toString());
        }

        UMOSecurityContext context = getSecurityManager().createSecurityContext(authResult);
        context.setAuthentication(authResult);
        event.getSession().setSecurityContext(context);
    }

    protected void authenticateOutbound(UMOEvent event)
        throws SecurityException, SecurityProviderNotFoundException, CryptoFailureException
    {
        if (event.getSession().getSecurityContext() == null)
        {
            if (isAuthenticate())
            {
                throw new UnauthorisedException(event.getMessage(), event.getSession().getSecurityContext(),
                    event.getEndpoint(), this);
            }
            else
            {
                return;
            }
        }
        UMOAuthentication auth = event.getSession().getSecurityContext().getAuthentication();
        if (isAuthenticate())
        {
            auth = getSecurityManager().authenticate(auth);
            if (logger.isDebugEnabled())
            {
                logger.debug("Authentication success: " + auth.toString());
            }
        }

        String token = auth.getCredentials().toString();
        getCredentialsAccessor().setCredentials(event, token);

    }

    protected void doInitialise() throws InitialisationException
    {
        // empty constructor
    }
}

