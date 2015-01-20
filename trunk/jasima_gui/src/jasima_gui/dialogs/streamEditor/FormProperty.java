package jasima_gui.dialogs.streamEditor;

import jasima_gui.dialogs.streamEditor.DetailsPageBase.FormParseError;

import java.beans.PropertyDescriptor;
import java.util.Objects;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class FormProperty {
	protected final static Image decoImg;
	static {
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		decoImg = fieldDecoration.getImage();
	}

	public FormProperty(DetailsPageBase owner, String propertyName,
			String labelText) {
		super();
		this.owner = owner;
		this.propertyName = propertyName;
		this.labelText = labelText;
		this.widget = null;
		this.beanDescriptor = null;
		this.decoration = null;
	}

	public final DetailsPageBase owner;
	public final String propertyName;
	public final String labelText;

	private Object value;
	public PropertyDescriptor beanDescriptor;
	public FormParseError error;
	public boolean isDirty;
	public boolean isStale;

	protected Control widget;
	protected ControlDecoration decoration;

	public abstract Object parseFromGui();

	protected void updateGui() {
		// convert to string
		String s = String.valueOf(value);
		((Text) widget).setText(s);

		isStale = false;
		decoration.hide();
	}

	public void createWidget() {
		FormToolkit toolkit = owner.mForm.getToolkit();

		createLabel(toolkit);
		createEditControl(toolkit);
	}

	protected void createEditControl(FormToolkit toolkit) {
		// create text widget
		Text text = toolkit.createText(owner.getClient(), "", SWT.SINGLE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalIndent = decoImg.getBounds().width;
		text.setLayoutData(gd);

		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateModel();
			}
		});
		widget = text;

		createDecorator(text);
	}

	protected void createDecorator(Control text) {
		// add the decorator
		decoration = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
		decoration.setImage(decoImg);
		decoration.hide();
	}

	protected void createLabel(FormToolkit toolkit) {
		// create label
		toolkit.createLabel(owner.getClient(), labelText);
	}

	protected void updateModel() {
		if (owner.disableModifyEvent)
			return;

		Object newValue = parseFromGui();

		if (newValue instanceof FormParseError) {
			FormParseError e = (FormParseError) newValue;
			error = e;
		} else {
			error = null;
		}

		if (owner.checkGlobalConstraints() && !Objects.equals(value, newValue)) {
			value = newValue;
			isDirty = true;
		}
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		isStale = true;
	}

	public ControlDecoration getDecoration() {
		return decoration;
	}

}
