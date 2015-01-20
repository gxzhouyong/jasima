package jasima_gui.dialogs.streamEditor.util;

import jasima_gui.dialogs.streamEditor.FormProperty;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IMessage;

public abstract class ConstraintValidator implements IMessage {

	public ConstraintValidator() {
		super();
	}

	public abstract boolean isValid();

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public int getMessageType() {
		return IMessageProvider.ERROR;
	}

	@Override
	public Object getKey() {
		return null;
	}

	@Override
	public Object getData() {
		return null;
	}

	@Override
	public Control getControl() {
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	public FormProperty[] dependsOn() {
		return new FormProperty[] {};
	}

}
