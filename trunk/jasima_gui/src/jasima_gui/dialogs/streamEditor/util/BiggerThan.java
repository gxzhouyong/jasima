package jasima_gui.dialogs.streamEditor.util;

import jasima_gui.dialogs.streamEditor.FormProperty;


public class BiggerThan extends SmallerThan {

	public BiggerThan(FormProperty p1, FormProperty p2, boolean strict) {
		super(p1, p2, strict);
	}

	public BiggerThan(FormProperty p1, Comparable constant, boolean strict) {
		super(p1, constant, strict);
	}

	@Override
	protected boolean doComparison(Comparable<Object> n1, Comparable<Object> n2) {
		if (strict)
			return n1.compareTo(n2) > 0;
		else
			return n1.compareTo(n2) >= 0;
	}

	@Override
	public String getMessage() {
		return String.format("'%s' has to be larger than '%s'.", p1.labelText,
				p2 != null ? p2.labelText : constant);
	}

}
