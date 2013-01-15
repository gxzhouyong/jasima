/*******************************************************************************
 * Copyright (c) 2010-2013 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.0.
 *
 * jasima is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jasima is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jasima.  If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id$
 *******************************************************************************/
package jasima_gui;

import jasima_gui.editor.EditorWidget;
import jasima_gui.editor.IProperty;
import jasima_gui.editor.TopLevelEditor;
import jasima_gui.util.TypeUtil;

import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class PropertyToolTip extends ToolTip {

	protected static String javadocStylesheet = null;

	protected static String getJavadocStylesheet() {
		if (javadocStylesheet != null)
			return javadocStylesheet;
		try {
			InputStreamReader rdr = new InputStreamReader(Platform
					.getBundle(JavaPlugin.getPluginId())
					.getEntry("/JavadocHoverStyleSheet.css").openStream());
			StringBuilder stylesheet = new StringBuilder();
			char[] buf = new char[1024];
			int r;
			while ((r = rdr.read(buf)) != -1) {
				stylesheet.append(buf, 0, r);
			}
			return javadocStylesheet = stylesheet.toString();
		} catch (Exception e) {
			javadocStylesheet = ""; // don't try again
			throw new RuntimeException(e);
		}
	}

	protected IProperty prop;
	protected TopLevelEditor editor;

	public PropertyToolTip(IProperty prop, TopLevelEditor editor,
			Control control) {
		super(control);
		this.prop = prop;
		this.editor = editor;
		setShift(new Point(-5, -5));
		setHideOnMouseDown(false);
	}

	private static String escapeHTML(String input) {
		return input.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	private static String color2HTML(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(),
				color.getBlue());
	}

	@Override
	protected boolean shouldCreateToolTip(Event event) {
		return true;
	}

	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		comp.setLayout(layout);

		Browser browser = new Browser(comp, SWT.NONE);
		GridDataFactory.fillDefaults().hint(400, 200).applyTo(browser);

		ColorRegistry colors = PlatformUI.getWorkbench().getThemeManager()
				.getCurrentTheme().getColorRegistry();
		Color bgColor = colors
				.get("org.eclipse.jdt.ui.JavadocView.backgroundColor");

		Class<?> tmp = TypeUtil.toClass(prop.getType());
		while (tmp.isArray())
			tmp = tmp.getComponentType();
		final Class<?> typeAsClass = tmp;

		StringBuilder htmlDoc = new StringBuilder();
		htmlDoc.append("<!DOCTYPE html><html><head>"
				+ "<title>Tooltip</title><style type='text/css'>");
		htmlDoc.append(getJavadocStylesheet());
		htmlDoc.append("html {background-color:");
		htmlDoc.append(color2HTML(bgColor));
		htmlDoc.append(";padding:10px}");
		// TODO add font (see HTMLPrinter)

		htmlDoc.append("</style></head><body><div>");
		String htmlDesc = prop.getHTMLDescription();
		if (htmlDesc != null) {
			htmlDoc.append(htmlDesc);
		}

		boolean hideType = EditorWidget.class.isAssignableFrom(typeAsClass);
		if (!hideType) {
			IType type = null;
			if (!typeAsClass.isPrimitive()) {
				try {
					type = editor.getJavaProject().findType(
							typeAsClass.getCanonicalName());
				} catch (JavaModelException e) {
					// ignore
				}
			}

			htmlDoc.append("<dl><dt>Property type:</dt><dd><a");
			if (type != null) {
				try {
					htmlDoc.append(" href=\""
							+ JavaElementLinks.createURI(
									JavaElementLinks.JAVADOC_SCHEME, type)
							+ "\"");
				} catch (URISyntaxException e) {
					// ignore
				}
			}
			htmlDoc.append("><code>");
			htmlDoc.append(escapeHTML(TypeUtil.toString(prop.getType(), true)));
			htmlDoc.append("</code></a></dd></dl>");
		}

		htmlDoc.append("</div></body></html>");
		browser.setText(htmlDoc.toString(), true);

		browser.addLocationListener(JavaElementLinks
				.createLocationListener(new JavaElementLinks.ILinkHandler() {

					public void handleTextSet() {
						// ignore
					}

					public void handleJavadocViewLink(IJavaElement target) {
						hide();
						try {
							JavaUI.openInEditor(target);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					public void handleInlineJavadocLink(IJavaElement target) {
						handleJavadocViewLink(target);
					}

					public boolean handleExternalLink(URL url, Display display) {
						hide();
						try {
							PlatformUI.getWorkbench().getBrowserSupport()
									.createBrowser(null).openURL(url);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return true;
					}

					public void handleDeclarationLink(IJavaElement target) {
						handleJavadocViewLink(target);
					}
				}));
		return comp;
	}
}
