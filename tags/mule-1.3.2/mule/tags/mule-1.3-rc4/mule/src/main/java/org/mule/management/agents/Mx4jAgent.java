/*
 * $Id$
 * ------------------------------------------------------------------------------------------------------
 *
 * Copyright (c) SymphonySoft Limited. All rights reserved.
 * http://www.symphonysoft.com
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.mule.management.agents;

import mx4j.log.CommonsLogger;
import mx4j.log.Log;
import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.adaptor.http.XSLTProcessor;
import mx4j.tools.adaptor.ssl.SSLAdaptorServerSocketFactory;

import org.mule.config.i18n.Message;
import org.mule.config.i18n.Messages;
import org.mule.umo.UMOException;
import org.mule.umo.lifecycle.InitialisationException;
import org.mule.umo.manager.UMOAgent;
import org.mule.util.BeanUtils;
import org.mule.util.StringUtils;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;

/**
 * <code>Mx4jAgent</code> configures an Mx4J Http Adaptor for Jmx management,
 * statistics and configuration viewing of a Mule instance..
 * 
 * @author Guillaume Nodet
 * @version $Revision$
 */
public class Mx4jAgent implements UMOAgent
{

    private String name = "MX4j  Agent";
    private String jmxAdaptorUrl = "http://localhost:9999";
    private Object adaptor;
    private MBeanServer mBeanServer;
    private ObjectName adaptorName;

    //Adaptor overrides

    private String login = null;

    private String password = null;

    private String authenticationMethod = "basic";

    private String xslFilePath = null;

    private String pathInJar = null;

    private boolean cacheXsl = true;

    // SSL/TLS socket factory config
    private Map socketFactoryProperties = new HashMap();


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    public void setAuthenticationMethod(String authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }

    public String getXslFilePath() {
        return xslFilePath;
    }

    public void setXslFilePath(String xslFilePath) {
        this.xslFilePath = xslFilePath;
    }

    public String getPathInJar() {
        return pathInJar;
    }

    public void setPathInJar(String pathInJar) {
        this.pathInJar = pathInJar;
    }

    public boolean isCacheXsl() {
        return cacheXsl;
    }

    public void setCacheXsl(boolean cacheXsl) {
        this.cacheXsl = cacheXsl;
    }

    protected Object createAdaptor() throws Exception
    {
        Object adaptor;
        Log.redirectTo(new CommonsLogger());
        URI uri = new URI(StringUtils.stripToEmpty(jmxAdaptorUrl));
        adaptor = new HttpAdaptor(uri.getPort(), uri.getHost());

        // Set the XSLT Processor with any local overrides

        XSLTProcessor processor = new XSLTProcessor();

        if (StringUtils.isNotBlank(xslFilePath)) {

            processor.setFile(xslFilePath.trim());

        }

        if (StringUtils.isNotBlank(pathInJar)) {

            processor.setPathInJar(pathInJar.trim());

        }

        processor.setUseCache(cacheXsl);

        final HttpAdaptor httpAdaptor = ((HttpAdaptor) adaptor);
        httpAdaptor.setProcessor(processor);

        //Set endpoint authentication if required

        if (login != null) {

            httpAdaptor.addAuthorization(login, password);

            httpAdaptor.setAuthenticationMethod(authenticationMethod);

        }

        if (socketFactoryProperties != null && !socketFactoryProperties.isEmpty()) {
            // TODO mx4j's socket factory does not handle the IBM's security provider
            // extend it to hadnle the case.
            // See http://jira.symphonysoft.com/browse/MULE-809 for analogous patch
            SSLAdaptorServerSocketFactory factory = new SSLAdaptorServerSocketFactory();
            BeanUtils.populateWithoutFail(factory, socketFactoryProperties, true);
            httpAdaptor.setSocketFactory(factory);
        }

        return adaptor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.manager.UMOAgent#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.manager.UMOAgent#setName(java.lang.String)
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.manager.UMOAgent#getDescription()
     */
    public String getDescription()
    {
        return "MX4J Http adaptor: " + jmxAdaptorUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.lifecycle.Startable#start()
     */
    public void start() throws UMOException
    {
        try {
            mBeanServer.invoke(adaptorName, "start", null, null);
        } catch (InstanceNotFoundException e) {
            throw new JmxManagementException(new Message(Messages.FAILED_TO_START_X, "Mx4j agent"), adaptorName, e);
        } catch (MBeanException e) {
            throw new JmxManagementException(new Message(Messages.FAILED_TO_START_X, "Mx4j agent"), adaptorName, e);
        } catch (ReflectionException e) {
            // ignore
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.lifecycle.Stoppable#stop()
     */
    public void stop() throws UMOException
    {
        try {
            mBeanServer.invoke(adaptorName, "stop", null, null);
        } catch (InstanceNotFoundException e) {
            throw new JmxManagementException(new Message(Messages.FAILED_TO_STOP_X, "Mx4j agent"), adaptorName, e);
        } catch (MBeanException e) {
            throw new JmxManagementException(new Message(Messages.FAILED_TO_STOP_X, "Mx4j agent"), adaptorName, e);
        } catch (ReflectionException e) {
            // ignore
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.lifecycle.Disposable#dispose()
     */
    public void dispose()
    {
        try {
            stop();
        } catch (Exception e) {
            // TODO: log an exception
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.manager.UMOAgent#registered()
     */
    public void registered()
    {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.manager.UMOAgent#unregistered()
     */
    public void unregistered()
    {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mule.umo.lifecycle.Initialisable#initialise()
     */
    public void initialise() throws InitialisationException
    {
        try {
            mBeanServer = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
            adaptor = createAdaptor();
            adaptorName = new ObjectName("Adaptor:class=" + adaptor.getClass().getName());
            mBeanServer.registerMBean(adaptor, adaptorName);
        } catch (Exception e) {
            throw new InitialisationException(new Message(Messages.FAILED_TO_START_X, "mx4j agent"), e);
        }
    }

    /**
     * @return Returns the jmxAdaptorUrl.
     */
    public String getJmxAdaptorUrl()
    {
        return jmxAdaptorUrl;
    }

    /**
     * @param jmxAdaptorUrl The jmxAdaptorUrl to set.
     */
    public void setJmxAdaptorUrl(String jmxAdaptorUrl)
    {
        this.jmxAdaptorUrl = jmxAdaptorUrl;
    }


    public Map getSocketFactoryProperties() {
        return socketFactoryProperties;
    }

    public void setSocketFactoryProperties(Map socketFactoryProperties) {
        this.socketFactoryProperties = socketFactoryProperties;
    }
}
