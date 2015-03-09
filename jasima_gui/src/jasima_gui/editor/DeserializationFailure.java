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
package jasima_gui.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DeserializationFailure {
	protected Label label;

	public DeserializationFailure(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		Composite comp = toolkit.createComposite(parent);
		GridLayout grid = new GridLayout(2, false);
		grid.marginTop = 10;
		comp.setLayout(grid);
		Label icon = toolkit.createLabel(comp, null);
		icon.setImage(icon.getDisplay().getSystemImage(SWT.ERROR));
		label = toolkit.createLabel(comp, null, SWT.WRAP);
	}

	public void setException(Throwable exception) {
		String type = exception.getClass().getSimpleName();
		String details = String.valueOf(exception.getLocalizedMessage()).replaceFirst("^ *: *", "");
		String message = String.format("Error reading input: %s: %s", type, details);
		label.setText(message);
	}

}
