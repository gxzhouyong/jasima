/*******************************************************************************
 * Copyright (c) 2010-2013 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.0.
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

import java.lang.reflect.Type;

public interface IProperty {

	/**
	 * Of course, all properties are important - some are just especially
	 * important.
	 * 
	 * @return true if this property is especially important
	 */
	public boolean isImportant();

	public boolean isWritable();

	public String getName();

	public String getHTMLDescription();

	public Type getType();

	/**
	 * Determines if the property can be set to null. This is always false if
	 * the property has a primitive type. It may still be true if the setter
	 * prevents null values. It can artificially be set to false by EditorWidget
	 * implementations that handle the object's life cycle, so that those who
	 * don't can still be used as soon as an actual instance exists.
	 * 
	 * @return false if the property can never be set to null
	 */
	public boolean canBeNull();

	public Object getValue() throws PropertyException;

	public void setValue(Object val) throws PropertyException;

}
