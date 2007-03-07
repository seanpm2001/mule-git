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
package org.mule.umo.routing;

import java.util.List;

import org.mule.management.stats.RouterStatistics;

/**
 * <code>UMORouterCollection</code> TODO
 * 
 * @author <a href="mailto:ross.mason@symphonysoft.com">Ross Mason</a>
 * @version $Revision$
 */

public interface UMORouterCollection
{
    void setRouters(List routers);

    List getRouters();

    void addRouter(UMORouter router);

    UMORouter removeRouter(UMORouter router);

    UMORouterCatchAllStrategy getCatchAllStrategy();

    void setCatchAllStrategy(UMORouterCatchAllStrategy catchAllStrategy);

    boolean isMatchAll();

    RouterStatistics getStatistics();

    void setStatistics(RouterStatistics stat);

    void setMatchAll(boolean matchAll);
}
