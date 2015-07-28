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
package jasima_gui.editor;

import java.util.Map;

import org.osgi.framework.Bundle;

public class DirectProperty implements IProperty {

	protected IProperty parent;
	protected boolean isImportant;
	protected String name;
	protected String description;
	protected Class<?> type;
	protected boolean canBeNull;

	public DirectProperty(Bundle contributingBundle, IProperty parent,
			Map<String, String> attributes) {
		this.parent = parent;
		isImportant = "true".equals(attributes.get("important"));
		name = attributes.get("name");
		description = attributes.get("description");
		try {
			type = contributingBundle.loadClass(attributes.get("type"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		canBeNull = "true".equals(attributes.get("canBeNull"));
	}

	@Override
	public boolean isImportant() {
		return isImportant;
	}

	@Override
	public boolean isWritable() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getHTMLDescription() {
		return description;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public boolean canBeNull() {
		return canBeNull;
	}

	@Override
	public Object getValue() throws PropertyException {
		return parent.getValue();
	}

	@Override
	public void setValue(Object val) throws PropertyException {
		parent.setValue(val);
	}

}
