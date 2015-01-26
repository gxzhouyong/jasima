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
package jasima_gui.editors;

import jasima_gui.editor.EditorWidget;
import jasima_gui.editor.PropertyException;
import jasima_gui.util.TypeUtil;

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
import org.eclipse.swt.widgets.Spinner;

public class IntEditor extends EditorWidget implements FocusListener, ModifyListener, SelectionListener {

	private Button btnNull = null;
	private Spinner spinner;
	private boolean modifying = false;
	private boolean dirty = false;

	public IntEditor(Composite parent) {
		super(parent);
	}

	public void createControls() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = 0;
		setLayout(layout);

		spinner = new Spinner(this, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(spinner);
		spinner.setValues(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, 50);
		toolkit.adapt(spinner, true, true);
		spinner.addFocusListener(this);
		spinner.setEnabled(property.isWritable());
		spinner.addModifyListener(this);
		if (property.canBeNull() && property.isWritable()) {
			layout.numColumns = 2;
			btnNull = toolkit.createButton(this, "null", SWT.CHECK);
			btnNull.addSelectionListener(this);
		}
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (modifying)
			return;
		storeValue();
	}

	@Override
	public void setEnabled(boolean enabled) {
		enabled &= property.isWritable();
		if (btnNull != null) {
			btnNull.setEnabled(enabled);
			enabled &= !btnNull.getSelection();
		}
		spinner.setEnabled(enabled);
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
			loadValue();
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		assert e.widget == btnNull;
		boolean nullSelected = btnNull.getSelection();
		spinner.setEnabled(!nullSelected);
		spinner.setSelection(0);
		if (!nullSelected) {
			dirty = true;
			spinner.setFocus();
		} else {
			storeValue();
		}
	}

	@Override
	public void loadValue() {
		modifying = true;
		Number num = null;
		try {
			num = (Number) property.getValue();
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
		}
		if (num == null) {
			spinner.setSelection(0);
		} else {
			spinner.setSelection(num.intValue());
		}
		dirty = false;
		modifying = false;
	}

	@Override
	public void storeValue() {
		try {
			Object val = null;
			Class<?> type = TypeUtil.toClass(property.getType());
			if (type.isPrimitive()) {
				type = TypeUtil.getPrimitiveWrapper(type);
			}
			val = type.getConstructor(String.class).newInstance(String.valueOf(spinner.getSelection()));
			if (val != null) {
				property.setValue(val);
			}
			dirty = false;
			hideError();
		} catch (Exception ex) {
			showError(ex.getLocalizedMessage());
		}
	}
}
