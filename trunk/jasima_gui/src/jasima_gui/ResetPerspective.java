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
package jasima_gui;

import jasima_gui.pref.Pref;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/**
 * The activator class controls the plug-in life cycle
 */
public class ResetPerspective implements IStartup {

	@Override
	public void earlyStartup() {
		if (Pref.FIRST_RUN.val()) {
			Pref.FIRST_RUN.set(false);
			final IWorkbench wb = PlatformUI.getWorkbench();
			wb.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbenchWindow wbw = wb.getActiveWorkbenchWindow();
					if (!MessageDialog.openConfirm(wbw.getShell(), "Reset perspective",
							"To complete installation of the jasima plugin, the Java perspective will be reset.")) {
						return;
					}
					try {
						wb.showPerspective("org.eclipse.jdt.ui.JavaPerspective", wbw);
						wbw.getActivePage().resetPerspective();
					} catch (WorkbenchException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

}
