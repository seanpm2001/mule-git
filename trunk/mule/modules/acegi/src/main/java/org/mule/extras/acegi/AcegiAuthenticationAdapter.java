/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extras.acegi;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.mule.umo.security.UMOAuthentication;

/**
 * <code>AcegiAuthenticationAdapter</code> TODO
 * 
 * @author <a href="mailto:ross.mason@symphonysoft.com">Ross Mason</a>
 * @version $Revision$
 */
public class AcegiAuthenticationAdapter implements UMOAuthentication
{
    private Authentication delegate;

    public AcegiAuthenticationAdapter(Authentication authentication)
    {
        this.delegate = authentication;
    }

    public void setAuthenticated(boolean b)
    {
        delegate.setAuthenticated(b);
    }

    public boolean isAuthenticated()
    {
        return delegate.isAuthenticated();
    }

    public GrantedAuthority[] getAuthorities()
    {
        return delegate.getAuthorities();
    }

    public Object getCredentials()
    {
        return delegate.getCredentials();
    }

    public Object getDetails()
    {
        return delegate.getDetails();
    }

    public Object getPrincipal()
    {
        return delegate.getPrincipal();
    }

    public int hashCode()
    {
        return delegate.hashCode();
    }

    public boolean equals(Object another)
    {
        return AcegiAuthenticationAdapter.this.equals(another);
    }

    public String getName()
    {
        return delegate.getName();
    }

    public Authentication getDelegate()
    {
        return delegate;
    }
}
