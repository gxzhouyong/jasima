package jasima_gui.util;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public class EnablingSelectionListener implements SelectionListener {

	protected final Control control;

	public EnablingSelectionListener(Control control) {
		this.control = control;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Widget w = e.widget;
		if (w instanceof Button) {
			control.setEnabled(((Button) w).getSelection());
		} else {
			assert false;
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}

}
