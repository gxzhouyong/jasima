/*******************************************************************************
 * Copyright (c) 2010-2015 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.2.
 *
 * jasima is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jasima is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jasima.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
