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
 */
package org.mule.test.mule;

import org.mule.MuleManager;
import org.mule.config.builders.MuleXmlConfigurationBuilder;
import org.mule.tck.AbstractUMOManagerTestCase;
import org.mule.umo.UMOManager;

/**
 * @author <a href="mailto:ross.mason@cubis.co.uk">Ross Mason</a>
 * @version $Revision$
 */

public class MuleManagerTestCase extends AbstractUMOManagerTestCase
{
    public UMOManager getUMOManager() throws Exception
    {
        if(MuleManager.isInstanciated()) MuleManager.getInstance().dispose();
        MuleXmlConfigurationBuilder builder = new MuleXmlConfigurationBuilder();
        UMOManager manager = builder.configure("test-config-for-manager.xml");
        return manager;
    }
}
