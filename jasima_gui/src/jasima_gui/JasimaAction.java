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

import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandImageService;
import org.eclipse.ui.commands.ICommandService;

public class JasimaAction extends Action {

	public JasimaAction(String id) {
		try {
			if (id.startsWith("...")) {
				id = "jasima_gui.commands." + id.substring(3);
			}

			IWorkbench wb = PlatformUI.getWorkbench();
			ICommandService commandService = (ICommandService) wb.getService(ICommandService.class);
			ICommandImageService commandImageService = (ICommandImageService) wb.getService(ICommandImageService.class);

			setText(commandService.getCommand(id).getName());
			setImageDescriptor(commandImageService.getImageDescriptor(id));
		} catch (NotDefinedException e) {
			throw new RuntimeException(e);
		}
	}

}
