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
package org.mule.management.mbeans;

import javax.management.ObjectName;

/**
 * <code>ComponentStatsMBean</code> TODO
 *
 * @author Guillaume Nodet
 * @version $Revision$
 */
public interface ComponentStatsMBean {

	public void clear();
	
	public ObjectName getRouterInbound();
	public ObjectName getRouterOutbound();
	
    public long getAverageExecutionTime();

    public long getAverageQueueSize();

    public long getMaxQueueSize();

    public long getMaxExecutionTime();

    public long getFatalErrors();

    public long getMinExecutionTime();

    public long getTotalExecutionTime();

    public long getQueuedEvents();

    public long getAsyncEventsReceived();

    public long getSyncEventsReceived();

    public long getReplyToEventsSent();

    public long getSyncEventsSent();

    public long getAsyncEventsSent();

    public long getTotalEventsSent();

    public long getTotalEventsReceived();

    public long getExecutedEvents();

    public long getExecutionErrors();
}
