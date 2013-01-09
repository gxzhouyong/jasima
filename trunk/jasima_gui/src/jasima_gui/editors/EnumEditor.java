package jasima_gui.editors;

import jasima_gui.editor.EditorWidget;
import jasima_gui.editor.PropertyException;
import jasima_gui.util.TypeUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class EnumEditor extends EditorWidget implements SelectionListener {

	private static final String NULL_STRING = "null";
	private Combo combo;

	public EnumEditor(Composite parent) {
		super(parent);
	}

	public void createControls() {
		combo = new Combo(this, SWT.READ_ONLY);
		toolkit.adapt(combo, true, true);
		combo.add(NULL_STRING);
		for (Object o : TypeUtil.toClass(property.getType()).getEnumConstants()) {
			combo.add(((Enum<?>) o).name());
		}
		combo.addSelectionListener(this);
	}

	@Override
	public void loadValue() {
		Enum<?> val = null;
		try {
			val = (Enum<?>) property.getValue();
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
		}
		if (val == null) {
			combo.setText(NULL_STRING);
		} else {
			combo.setText(val.name());
		}
	}

	@Override
	public void storeValue() {
		String text = combo.getText();
		Object newVal;
		outer: if (text.equals(NULL_STRING)) {
			newVal = null;
		} else {
			for (Object o : TypeUtil.toClass(property.getType())
					.getEnumConstants()) {
				Enum<?> e = (Enum<?>) o;
				if (text.equals(e.name())) {
					newVal = e;
					break outer;
				}
			}
			throw new AssertionError(
					"EnumEditor's Combo has an impossible text");
		}
		// if (newVal != property.getValue()) {
		try {
			property.setValue(newVal);
			hideError();
		} catch (PropertyException e) {
			showError(e.getLocalizedMessage());
		}
		// }

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		storeValue();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}
}
