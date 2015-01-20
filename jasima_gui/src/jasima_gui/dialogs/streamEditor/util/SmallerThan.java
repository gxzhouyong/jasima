package jasima_gui.dialogs.streamEditor.util;

import jasima_gui.dialogs.streamEditor.FormProperty;

import org.eclipse.jface.dialogs.IMessageProvider;

public class SmallerThan extends ConstraintValidator {

	protected final FormProperty p1;
	protected final FormProperty p2;
	protected final Comparable constant;
	protected final boolean strict;

	public SmallerThan(FormProperty p1, FormProperty p2, boolean strict) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.constant = null;
		this.strict = strict;
	}

	public SmallerThan(FormProperty p1, Comparable constant, boolean strict) {
		super();
		this.p1 = p1;
		this.p2 = null;
		this.constant = constant;
		this.strict = strict;
	}

	@Override
	public FormProperty[] dependsOn() {
		if (p2 != null)
			return new FormProperty[] { p1, p2 };
		else
			return new FormProperty[] { p1 };
	}

	@Override
	public boolean isValid() {
		Object v1 = p1.parseFromGui();
		Object v2 = p2 != null ? p2.parseFromGui() : constant;

		@SuppressWarnings("unchecked")
		Comparable<Object> n1 = (Comparable<Object>) v1;
		@SuppressWarnings("unchecked")
		Comparable<Object> n2 = (Comparable<Object>) v2;

		return doComparison(n1, n2);
	}

	protected boolean doComparison(Comparable<Object> n1, Comparable<Object> n2) {
		if (strict)
			return n1.compareTo(n2) < 0;
		else
			return n1.compareTo(n2) <= 0;
	}

	@Override
	public String getMessage() {
		return String.format("'%s' has to be smaller than '%s'.", p1.labelText,
				p2 != null ? p2.labelText : constant);
	}

	@Override
	public int getMessageType() {
		return IMessageProvider.ERROR;
	}

}
