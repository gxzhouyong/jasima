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

import jasima_gui.editor.EditorWidget;
import jasima_gui.editor.PropertyException;
import jasima_gui.util.TypeUtil;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TextEditor extends EditorWidget implements FocusListener,
		ModifyListener, SelectionListener {

	private Text text;
	private Button btnNull = null;
	private boolean dirty = false;

	public TextEditor(Composite parent) {
		super(parent);
	}

	public void createControls() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 0;
		setLayout(layout);

		text = toolkit.createText(this, "", property.isWritable() ? SWT.BORDER
				: 0);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
		text.setEditable(property.isWritable());
		text.addFocusListener(this);
		text.addModifyListener(this);

		if (property.canBeNull() && property.isWritable()) {
			layout.numColumns = 2;
			btnNull = toolkit.createButton(this, "null", SWT.CHECK);
			btnNull.addSelectionListener(this);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// ignore
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (!property.isWritable())
			return;
		if (!dirty)
			return;
		try {
			storeValue();
		} finally {
			loadValue(); // reformat numbers, for example
		}
	}

	@Override
	public void modifyText(ModifyEvent e) {
		dirty = true;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		assert e.widget == btnNull;
		boolean nullSelected = btnNull.getSelection();
		text.setEnabled(!nullSelected);
		text.setText(nullSelected ? String.valueOf((Object) null) : "");
		if (!nullSelected) {
			dirty = true;
			text.setFocus();
		} else {
			storeValue();
		}
	}

	@Override
	public void loadValue() {
		try {
			Object val = property.getValue();
			// val might be null if IProperty.canBeNull returned false...
			text.setText(String.valueOf(val));
			if (btnNull != null) {
				text.setEnabled(val != null);
				btnNull.setSelection(val == null);
			}
			dirty = false;
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
			text.setText("?");
		}
	}

	@Override
	public void storeValue() {
		try {
			if (btnNull != null && btnNull.getSelection()) {
				property.setValue(null);
				dirty = false;
				hideError();
				return;
			}
			Object val = null;
			Class<?> type = TypeUtil.toClass(property.getType());
			if (type.isPrimitive()) {
				type = TypeUtil.getPrimitiveWrapper(type);
			}
			if (type == Number.class) {
				val = Double.valueOf(text.getText());
			} else if (Number.class.isAssignableFrom(type)) {
				try {
					val = type.getConstructor(String.class).newInstance(
							text.getText());
				} catch (InvocationTargetException ex) {
					return;
				}
			} else if (type == String.class) {
				val = text.getText();
			} else if (type == Character.class) {
				if (text.getText().length() > 0) {
					val = text.getText().charAt(0);
				}
			}
			if (val != null) {
				property.setValue(val);
			}
			dirty = false;
			hideError();
		} catch (PropertyException ex) {
			showError(ex.getLocalizedMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
