/*******************************************************************************
 * Copyright (c) 2010, 2014 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.1.
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

import java.net.URL;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class JavaLinkHandler implements JavaElementLinks.ILinkHandler {

	public static void openURL(URL url) {
		try {
			PlatformUI.getWorkbench().getBrowserSupport().createBrowser(null).openURL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openJavadoc(IJavaElement target) {
		try {
			URL location = JavaUI.getJavadocLocation(target, true);
			if (location != null) {
				openURL(location);
				return;
			}
		} catch (Exception e) {
			// ignore
		}

		try {
			JavaUI.openInEditor(target);
		} catch (Exception e) {
			// ignore
		}
	}

	public void linkOpened() {

	}

	public void handleTextSet() {
		// ignore
	}

	public void handleJavadocViewLink(IJavaElement target) {
		linkOpened();
		openJavadoc(target);
	}

	public void handleInlineJavadocLink(IJavaElement target) {
		handleJavadocViewLink(target);
	}

	public boolean handleExternalLink(URL url, Display display) {
		linkOpened();
		openURL(url);
		return true;
	}

	public void handleDeclarationLink(IJavaElement target) {
		handleJavadocViewLink(target);
	}
}
