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
package jasima_gui.util;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public class EnablingSelectionListener implements SelectionListener {

	protected final Control control;

	public EnablingSelectionListener(Control control) {
		this.control = control;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Widget w = e.widget;
		if (w instanceof Button) {
			control.setEnabled(((Button) w).getSelection());
		} else {
			assert false;
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}

}
