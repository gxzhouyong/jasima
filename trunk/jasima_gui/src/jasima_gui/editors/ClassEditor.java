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
package jasima_gui.editors;

import jasima_gui.JavaLinkHandler;
import jasima_gui.editor.EditorWidget;
import jasima_gui.editor.PropertyException;
import jasima_gui.util.TypeUtil;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class ClassEditor extends EditorWidget implements IHyperlinkListener {

	private Hyperlink hyperlink;

	public ClassEditor(Composite parent) {
		super(parent);
	}

	public void createControls() {
		hyperlink = toolkit.createHyperlink(this, "", SWT.NONE);
		hyperlink.addHyperlinkListener(this);
	}

	@Override
	public void loadValue() {
		Class<?> val = null;
		try {
			val = (Class<?>) property.getValue();
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
		}
		if (val == null) {
			hyperlink.setEnabled(false);
			hyperlink.setText("(null)");
		} else {
			hyperlink.setEnabled(true);
			String unAbbr = TypeUtil.toString(val);
			String abbr = TypeUtil.toString(val, true);
			if (!unAbbr.equals(abbr)) {
				hyperlink.setToolTipText(unAbbr);
			}
			hyperlink.setText(abbr);
		}
	}

	@Override
	public void storeValue() {
		// TODO write support...
	}

	@Override
	public void linkEntered(HyperlinkEvent e) {
		// ignore
	}

	@Override
	public void linkExited(HyperlinkEvent e) {
		// ignore
	}

	@Override
	public void linkActivated(HyperlinkEvent evt) {
		try {
			Class<?> val = (Class<?>) property.getValue();
			IJavaElement elem = topLevelEditor.getJavaProject().findType(val.getCanonicalName());
			JavaLinkHandler.openJavadoc(elem);
		} catch (Exception e) {
			// ignore
		}
	}
}
