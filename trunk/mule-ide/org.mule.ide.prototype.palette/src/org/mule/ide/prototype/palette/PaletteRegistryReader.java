/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the MuleSource MPL
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.ide.prototype.palette;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * This class reads the plugin manifests and adds each specified gramamr
 * annotation file with the AnnotationProvider
 */
public class PaletteRegistryReader {
	protected static final String pluginId = "org.mule.ide.prototype.palette"; //$NON-NLS-1$
	protected static final String EXTENSION_POINT_ID = "paletteEntries"; //$NON-NLS-1$
	protected static final String FOLDER_TAG_NAME = "folder"; //$NON-NLS-1$
	protected static final String ENDPOINT_TAG_NAME = "endpoint"; //$NON-NLS-1$
	protected static final String COMPONENT_TAG_NAME = "component"; //$NON-NLS-1$
	protected static final String FILTER_TAG_NAME = "filter"; //$NON-NLS-1$
	protected static final String ATT_NAME = "name"; //$NON-NLS-1$
	protected static final String ATT_CLASS_NAME = "className"; //$NON-NLS-1$
	protected static final String ATT_SCHEME_PREFIX = "schemePrefix"; //$NON-NLS-1$
	protected static final String ATT_ICON = "icon"; //$NON-NLS-1$

	private FolderItem rootPaletteItem;

	public PaletteRegistryReader() {
		this.rootPaletteItem = new FolderItem("Mule");
	}

	/**
	 * read from plugin registry and parse it.
	 */
	public void readRegistry() {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint point = extensionRegistry.getExtensionPoint(pluginId, EXTENSION_POINT_ID);
		if (point != null) {
			visitConfigElements(point.getConfigurationElements(), point,  rootPaletteItem);
		}
	}

	private void visitConfigElements(IConfigurationElement[] elements, IExtensionPoint point, FolderItem parent) {
		for (int i = 0; i < elements.length; i++) {
			readElement(elements[i], point, parent);
		}
	}

	/**
	 * readElement() - parse and deal with extension
	 */
	protected void readElement(IConfigurationElement element, IExtensionPoint point, FolderItem parent) {
		PaletteItem palItem = null;
		
		if (element.getName().equals(FOLDER_TAG_NAME)) {
			FolderItem pi = null;
			String name = element.getAttribute(ATT_NAME);
			if (name != null) {
				palItem = pi = new FolderItem(name);
				parent.addChild(pi);
			}
			visitConfigElements(element.getChildren(), point, pi);
		} else if (element.getName().equals(FILTER_TAG_NAME)) {
			String className = element.getAttribute(ATT_CLASS_NAME);
			if (className != null) {
				parent.addChild(palItem = new FilterItem(className));
			}
		} else if (element.getName().equals(COMPONENT_TAG_NAME)) {
			String className = element.getAttribute(ATT_CLASS_NAME);
			if (className != null) {
				parent.addChild(palItem = new ComponentItem(className));
			}
		}
		
		// Only includes folder at this stage 
		String icon = element.getAttribute(ATT_ICON);
		if (icon != null) icon = icon.trim();
		
		if (icon != null && icon.length() > 0)
		{
			ImageDescriptor iDesc = Activator.imageDescriptorFromPlugin(element.getContributor().getName(), icon);
			if (iDesc != null) {
				palItem.setImageDescriptor(iDesc);
			}
		}
	}

	public FolderItem getRootPaletteItem() {
		return rootPaletteItem;
	}
}
