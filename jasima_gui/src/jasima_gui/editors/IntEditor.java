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
