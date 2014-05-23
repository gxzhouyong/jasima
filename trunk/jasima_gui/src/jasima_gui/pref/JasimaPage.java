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
package jasima_gui.pref;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JasimaPage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public JasimaPage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Pref.prefStore());
	}

	@Override
	protected void createFieldEditors() {
		final Composite fep = getFieldEditorParent();
		final String[][] formats = { { "XLS (Excel)", "--xlsres" },
				{ "XML", "--xmlres" }, { "Console output", "--printres" } };

		addField(new StringFieldEditor(Pref.JASIMA_VERSION.key,
				"Jasima version for new projects:", fep));

		addField(new ComboFieldEditor(Pref.EXP_RES_FMT.key,
				"Default result format for XML experiments:", formats, fep));
		addField(new ComboFieldEditor(Pref.XLS_EXP_RES_FMT.key,
				"Default result format for Excel experiments:", formats, fep));
	}

}
