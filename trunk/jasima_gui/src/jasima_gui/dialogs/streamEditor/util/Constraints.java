package jasima_gui.dialogs.streamEditor.util;

import jasima_gui.dialogs.streamEditor.FormProperty;


public class Constraints {

	public static ConstraintValidator biggerThan(FormProperty p1,
			FormProperty p2) {
		return new SmallerThan(p1, p2, true);
	}

	public static ConstraintValidator biggerThan(FormProperty p1,
			Integer constant) {
		return new SmallerThan(p1, constant, true);
	}

	public static ConstraintValidator biggerThan(FormProperty p1,
			Double constant) {
		return new SmallerThan(p1, constant, true);
	}

	public static ConstraintValidator positive(FormProperty p1) {
		return new BiggerThan(p1, 0.0, true);
	}

	public static ConstraintValidator smallerThan(FormProperty p1,
			FormProperty p2) {
		return new SmallerThan(p1, p2, true);
	}

	public static ConstraintValidator smallerThan(FormProperty p1,
			Integer constant) {
		return new SmallerThan(p1, constant, true);
	}

	public static ConstraintValidator smallerThan(FormProperty p1,
			Double constant) {
		return new SmallerThan(p1, constant, true);
	}

	public static ConstraintValidator negative(FormProperty p1) {
		return new SmallerThan(p1, 0.0, true);
	}

	public static ConstraintValidator smallerOrEqualThan(FormProperty p1,
			FormProperty p2) {
		return new SmallerThan(p1, p2, false);
	}

	private Constraints() {
	}

}
