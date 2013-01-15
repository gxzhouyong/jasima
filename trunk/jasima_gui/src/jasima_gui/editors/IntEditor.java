/*******************************************************************************
 * Copyright (c) 2010-2013 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.0.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

public class IntEditor extends EditorWidget implements ModifyListener {

	private Spinner spinner;
	private boolean modifying = false;

	public IntEditor(Composite parent) {
		super(parent);
	}

	public void createControls() {
		spinner = new Spinner(this, SWT.BORDER);
		spinner.setValues(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, 50);
		toolkit.adapt(spinner, true, true);
		spinner.setEnabled(property.isWritable());
		spinner.addModifyListener(this);
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (modifying)
			return;
		storeValue();
	}

	@Override
	public void setEnabled(boolean enabled) {
		spinner.setEnabled(enabled && property.isWritable());
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
			val = type.getConstructor(String.class).newInstance(
					String.valueOf(spinner.getSelection())); // TODO
			if (val != null) {
				property.setValue(val);
			}
			hideError();
		} catch (Exception ex) {
			showError(ex.getLocalizedMessage());
		}
	}
}
