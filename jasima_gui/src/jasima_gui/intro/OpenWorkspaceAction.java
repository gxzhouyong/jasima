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

import java.util.Properties;

import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;

public class OpenWorkspaceAction implements IIntroAction {
	protected Browser browser;

	public static final String HREF_OPEN_PERSPECTIVE = "jasima:open-perspective";

	@Override
	public void run(IIntroSite site, Properties params) {
		IWorkbenchWindow wbw = site.getWorkbenchWindow();
		IWorkbench wb = wbw.getWorkbench();
		IIntroManager im = wb.getIntroManager();
		im.closeIntro(im.getIntro());
		try {
			wb.showPerspective("jasima_gui.jasimaPerspective", wbw);
		} catch (WorkbenchException ex) {
			throw new RuntimeException(ex);
		}
	}
}
