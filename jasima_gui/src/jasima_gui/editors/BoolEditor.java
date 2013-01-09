package jasima_gui.editors;

import jasima_gui.editor.EditorWidget;
import jasima_gui.editor.PropertyException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class BoolEditor extends EditorWidget implements SelectionListener {

	private Boolean val;
	private Button chkFalse;
	private Button chkTrue;
	private Button chkNull = null;

	public BoolEditor(Composite parent) {
		super(parent);
	}

	public void createControls() {
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = layout.marginWidth = 0;
		setLayout(layout);

		chkTrue = toolkit.createButton(this, "true", SWT.RADIO);
		chkTrue.setEnabled(property.isWritable());
		chkTrue.addSelectionListener(this);

		chkFalse = toolkit.createButton(this, "false", SWT.RADIO);
		chkFalse.setEnabled(property.isWritable());
		chkFalse.addSelectionListener(this);

		if (property.canBeNull() && property.isWritable()) {
			chkNull = toolkit.createButton(this, "null", SWT.RADIO);
			chkNull.addSelectionListener(this);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}

	@Override
	public void widgetSelected(SelectionEvent evt) {
		storeValue();
	}

	@Override
	public void setEnabled(boolean enabled) {
		chkFalse.setEnabled(enabled && property.isWritable());
	}

	@Override
	public void loadValue() {
		try {
			val = (Boolean) property.getValue();
			if (val == null) {
				chkNull.setSelection(true);
			} else if (val.booleanValue()) {
				chkTrue.setSelection(true);
			} else {
				chkFalse.setSelection(true);
			}
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
			chkFalse.setSelection(false);
			chkTrue.setSelection(false);
			chkNull.setSelection(false);
		}
	}

	@Override
	public void storeValue() {
		try {
			Boolean newVal = (chkNull != null && chkNull.getSelection()) ? null
					: chkTrue.getSelection();
			if (val == null) {
				if (newVal == null)
					return;
			} else if (val.equals(newVal)) {
				return;
			}
			property.setValue(newVal);
			val = newVal;
			hideError();
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
			loadValue();
		}
	}
}
