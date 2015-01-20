package jasima_gui.dialogs.streamEditor.util;

import jasima_gui.dialogs.streamEditor.DetailsPageBase;
import jasima_gui.dialogs.streamEditor.FormProperty;
import jasima_gui.dialogs.streamEditor.DetailsPageBase.FormParseError;

import org.eclipse.swt.widgets.Text;

public class DoubleProperty extends FormProperty {
	public DoubleProperty(DetailsPageBase owner, String propertyName,
			String labelText) {
		super(owner, propertyName, labelText);
	}

	@Override
	public Object parseFromGui() {
		String s = ((Text) widget).getText();
		try {
			// convert value
			Double d = Double.parseDouble(s);
			return d;
		} catch (NumberFormatException ignore) {
			String desc = String.format("'%s' is not a valid real number.", s);
			String hover = "Please enter a real number (with dot \nas decimal separator).";
			return new FormParseError(desc, hover, ignore);
		}
	}
}