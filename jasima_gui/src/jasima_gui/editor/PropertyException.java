/*******************************************************************************
 * Copyright (c) 2010, 2014 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.1.
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
 *
 * $Id$
 *******************************************************************************/
package jasima_gui.editor;

public class PropertyException extends Exception {

	private static final long serialVersionUID = 4801173525092352477L;

	public PropertyException(String message) {
		super(message);
	}

	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public static PropertyException newGetException(IProperty prop,
			Throwable cause) {
		return new PropertyException(String.format(
				"Couldn't get value of property %s.", prop), cause);
	}

	public static PropertyException newSetException(IProperty prop,
			Throwable cause) {
		if (cause instanceof IllegalArgumentException) {
			return new PropertyException(String.format(
					"Bad property value: %s", cause.getLocalizedMessage()),
					cause);
		}
		return new PropertyException(String.format(
				"Couldn't set value of property %s.", prop), cause);
	}

}
