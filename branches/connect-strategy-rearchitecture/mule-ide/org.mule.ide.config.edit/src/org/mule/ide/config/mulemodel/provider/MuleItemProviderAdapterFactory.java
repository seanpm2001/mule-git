/**
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * MULE_LICENSE.txt file.
 */
package org.mule.ide.config.mulemodel.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import org.mule.ide.config.mulemodel.util.MuleAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MuleItemProviderAdapterFactory extends MuleAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com"; //$NON-NLS-1$

	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection supportedTypes = new ArrayList();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MuleItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);		
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.InboundRouter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InboundRouterItemProvider inboundRouterItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.InboundRouter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createInboundRouterAdapter() {
		if (inboundRouterItemProvider == null) {
			inboundRouterItemProvider = new InboundRouterItemProvider(this);
		}

		return inboundRouterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.MuleConfig} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MuleConfigItemProvider muleConfigItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.MuleConfig}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createMuleConfigAdapter() {
		if (muleConfigItemProvider == null) {
			muleConfigItemProvider = new MuleConfigItemProvider(this);
		}

		return muleConfigItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.OutboundRouter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OutboundRouterItemProvider outboundRouterItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.OutboundRouter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createOutboundRouterAdapter() {
		if (outboundRouterItemProvider == null) {
			outboundRouterItemProvider = new OutboundRouterItemProvider(this);
		}

		return outboundRouterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.Interceptor} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InterceptorItemProvider interceptorItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.Interceptor}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createInterceptorAdapter() {
		if (interceptorItemProvider == null) {
			interceptorItemProvider = new InterceptorItemProvider(this);
		}

		return interceptorItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.Connector} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectorItemProvider connectorItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.Connector}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createConnectorAdapter() {
		if (connectorItemProvider == null) {
			connectorItemProvider = new ConnectorItemProvider(this);
		}

		return connectorItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.Properties} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertiesItemProvider propertiesItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.Properties}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createPropertiesAdapter() {
		if (propertiesItemProvider == null) {
			propertiesItemProvider = new PropertiesItemProvider(this);
		}

		return propertiesItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.TextProperty} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TextPropertyItemProvider textPropertyItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.TextProperty}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createTextPropertyAdapter() {
		if (textPropertyItemProvider == null) {
			textPropertyItemProvider = new TextPropertyItemProvider(this);
		}

		return textPropertyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.ListProperty} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ListPropertyItemProvider listPropertyItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.ListProperty}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createListPropertyAdapter() {
		if (listPropertyItemProvider == null) {
			listPropertyItemProvider = new ListPropertyItemProvider(this);
		}

		return listPropertyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.MapProperty} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MapPropertyItemProvider mapPropertyItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.MapProperty}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createMapPropertyAdapter() {
		if (mapPropertyItemProvider == null) {
			mapPropertyItemProvider = new MapPropertyItemProvider(this);
		}

		return mapPropertyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.InterceptorStack} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InterceptorStackItemProvider interceptorStackItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.InterceptorStack}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createInterceptorStackAdapter() {
		if (interceptorStackItemProvider == null) {
			interceptorStackItemProvider = new InterceptorStackItemProvider(this);
		}

		return interceptorStackItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.BridgeComponent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BridgeComponentItemProvider bridgeComponentItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.BridgeComponent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createBridgeComponentAdapter() {
		if (bridgeComponentItemProvider == null) {
			bridgeComponentItemProvider = new BridgeComponentItemProvider(this);
		}

		return bridgeComponentItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.LocalEndpoint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalEndpointItemProvider localEndpointItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.LocalEndpoint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createLocalEndpointAdapter() {
		if (localEndpointItemProvider == null) {
			localEndpointItemProvider = new LocalEndpointItemProvider(this);
		}

		return localEndpointItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.GlobalEndpoint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GlobalEndpointItemProvider globalEndpointItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.GlobalEndpoint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createGlobalEndpointAdapter() {
		if (globalEndpointItemProvider == null) {
			globalEndpointItemProvider = new GlobalEndpointItemProvider(this);
		}

		return globalEndpointItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.Router} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouterItemProvider routerItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.Router}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createRouterAdapter() {
		if (routerItemProvider == null) {
			routerItemProvider = new RouterItemProvider(this);
		}

		return routerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.Transformer} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransformerItemProvider transformerItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.Transformer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createTransformerAdapter() {
		if (transformerItemProvider == null) {
			transformerItemProvider = new TransformerItemProvider(this);
		}

		return transformerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.GenericComponent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GenericComponentItemProvider genericComponentItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.GenericComponent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createGenericComponentAdapter() {
		if (genericComponentItemProvider == null) {
			genericComponentItemProvider = new GenericComponentItemProvider(this);
		}

		return genericComponentItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.GenericFilter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GenericFilterItemProvider genericFilterItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.GenericFilter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createGenericFilterAdapter() {
		if (genericFilterItemProvider == null) {
			genericFilterItemProvider = new GenericFilterItemProvider(this);
		}

		return genericFilterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.BinaryFilter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BinaryFilterItemProvider binaryFilterItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.BinaryFilter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createBinaryFilterAdapter() {
		if (binaryFilterItemProvider == null) {
			binaryFilterItemProvider = new BinaryFilterItemProvider(this);
		}

		return binaryFilterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.mule.ide.config.mulemodel.XsltFilter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XsltFilterItemProvider xsltFilterItemProvider;

	/**
	 * This creates an adapter for a {@link org.mule.ide.config.mulemodel.XsltFilter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createXsltFilterAdapter() {
		if (xsltFilterItemProvider == null) {
			xsltFilterItemProvider = new XsltFilterItemProvider(this);
		}

		return xsltFilterItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class) || (((Class)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (inboundRouterItemProvider != null) inboundRouterItemProvider.dispose();
		if (muleConfigItemProvider != null) muleConfigItemProvider.dispose();
		if (outboundRouterItemProvider != null) outboundRouterItemProvider.dispose();
		if (interceptorItemProvider != null) interceptorItemProvider.dispose();
		if (connectorItemProvider != null) connectorItemProvider.dispose();
		if (propertiesItemProvider != null) propertiesItemProvider.dispose();
		if (textPropertyItemProvider != null) textPropertyItemProvider.dispose();
		if (listPropertyItemProvider != null) listPropertyItemProvider.dispose();
		if (mapPropertyItemProvider != null) mapPropertyItemProvider.dispose();
		if (interceptorStackItemProvider != null) interceptorStackItemProvider.dispose();
		if (genericComponentItemProvider != null) genericComponentItemProvider.dispose();
		if (bridgeComponentItemProvider != null) bridgeComponentItemProvider.dispose();
		if (localEndpointItemProvider != null) localEndpointItemProvider.dispose();
		if (globalEndpointItemProvider != null) globalEndpointItemProvider.dispose();
		if (routerItemProvider != null) routerItemProvider.dispose();
		if (transformerItemProvider != null) transformerItemProvider.dispose();
		if (genericFilterItemProvider != null) genericFilterItemProvider.dispose();
		if (binaryFilterItemProvider != null) binaryFilterItemProvider.dispose();
		if (xsltFilterItemProvider != null) xsltFilterItemProvider.dispose();
	}

}
