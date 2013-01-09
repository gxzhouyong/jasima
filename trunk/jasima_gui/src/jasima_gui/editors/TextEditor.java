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
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = 0;
		setLayout(layout);

		text = toolkit.createText(this, "", property.isWritable() ? SWT.BORDER
				: 0);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
		text.setEditable(property.isWritable());
		text.addFocusListener(this);
		text.addModifyListener(this);

		if (property.canBeNull() && property.isWritable()) {
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
			if (Number.class.isAssignableFrom(type)) {
				try {
					val = type.getConstructor(String.class).newInstance(
							text.getText());
				} catch (InvocationTargetException ex) {
					return;
				}
			} else if (type == String.class) {
				val = text.getText();
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
