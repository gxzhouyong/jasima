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

import jasima_gui.ClassLoaderState;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class OutdatedClassesInfo {

	protected FormText message;

	public OutdatedClassesInfo(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		message = toolkit.createFormText(parent, true);

		message.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				String href = e.getHref().toString();
				if (href.equals("jasima:save")) {
					// doSave(null);
				} else if (href.equals("jasima:reopen")) {
					// doSave(null);
					// TODO
				}
			}
		});
	}

	public void setState(ClassLoaderState state) {
		StringBuilder bldr = new StringBuilder();
		bldr.append("<form>");
		state.generateReport(bldr);
		bldr.append("<p>You can still <a href='jasima:save'>save</a> this file, " //
				+ "but you have to <a href='jasima:reopen'>re-open</a> this editor " //
				+ "to continue editing.</p>");
		bldr.append("</form>");
		message.setText(bldr.toString(), true, false);
	}
}
