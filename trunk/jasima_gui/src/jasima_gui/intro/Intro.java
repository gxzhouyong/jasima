/*******************************************************************************
 * Copyright (c) 2010-2015 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.2.
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
 *******************************************************************************/
package jasima_gui.intro;

import jasima_gui.util.IOUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.part.IntroPart;

public class Intro extends IntroPart {
	protected Browser browser;

	public static final String HREF_OPEN_PERSPECTIVE = "jasima:open-perspective";

	@Override
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.V_SCROLL);

		try {
			InputStream str = Intro.class.getResourceAsStream("welcome.html");
			String s = IOUtil.readFully(new InputStreamReader(str, "utf-8"));
			browser.setText(s);
			browser.addLocationListener(new LocationAdapter() {
				@Override
				public void changing(LocationEvent event) {
					if (event.location.equals(HREF_OPEN_PERSPECTIVE)) {
						event.doit = false;
						IWorkbenchWindow wbw = getIntroSite().getWorkbenchWindow();
						IWorkbench wb = wbw.getWorkbench();
						wb.getIntroManager().closeIntro(Intro.this);
						try {
							wb.showPerspective("jasima_gui.perspective", wbw);
						} catch (WorkbenchException ex) {
							// ignore
						}
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			// can't happen
		}
	}

	@Override
	public void standbyStateChanged(boolean standby) {
		// ignore
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}
}
