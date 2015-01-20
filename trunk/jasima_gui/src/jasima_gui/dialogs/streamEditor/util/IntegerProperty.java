package jasima_gui.dialogs.streamEditor.util;

import jasima_gui.dialogs.streamEditor.DetailsPageBase;
import jasima_gui.dialogs.streamEditor.FormProperty;
import jasima_gui.dialogs.streamEditor.DetailsPageBase.FormParseError;

import org.eclipse.swt.widgets.Text;

public class IntegerProperty extends FormProperty {
	public IntegerProperty(DetailsPageBase owner, String propertyName,
			String labelText) {
		super(owner, propertyName, labelText);
	}

	@Override
	public Object parseFromGui() {
		String s = ((Text) widget).getText();
		try {
			// convert value
			Integer i = Integer.parseInt(s);
			return i;
		} catch (NumberFormatException ignore) {
			String msg = String.format("'%s' is not a valid integer.",
					s);
			String hover = "Please enter an integer number.";
			return new FormParseError(msg, hover, ignore);
		}
	}
}