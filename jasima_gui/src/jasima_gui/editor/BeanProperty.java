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

import jasima_gui.util.TypeUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.text.javadoc.JavadocContentAccess2;

@SuppressWarnings("restriction")
public class BeanProperty implements IProperty {

	protected final IProperty parent;
	protected final TopLevelEditor editor;
	protected final PropertyDescriptor pd;

	public BeanProperty(IProperty parent, TopLevelEditor editor,
			PropertyDescriptor pd) {
		this.parent = parent;
		this.editor = editor;
		this.pd = pd;
	}

	@Override
	public boolean isImportant() {
		return pd.isPreferred();
	}

	@Override
	public boolean isWritable() {
		return pd.getWriteMethod() != null;
	}

	@Override
	public String getName() {
		return pd.getDisplayName();
	}

	@Override
	public String getHTMLDescription() {
		// TODO doc from getter?
		try {
			Method writeMethod = pd.getWriteMethod();
			IType type = editor.getJavaProject().findType(
					writeMethod.getDeclaringClass().getCanonicalName());
			for (IMethod mtd : type.getMethods()) {
				if (mtd.getNumberOfParameters() != 1)
					continue;
				if (mtd.getElementName().equals(writeMethod.getName())) {
					return JavadocContentAccess2.getHTMLContent(mtd, true);
				}
			}
		} catch (JavaModelException e) {
			// ignore
		}
		return null;
	}

	@Override
	public Type getType() {
		return pd.getReadMethod().getGenericReturnType();
	}

	@Override
	public Object getValue() throws PropertyException {
		try {
			if (pd.getReadMethod() == null) {
				// handle this here so the rest can assume there is always a
				// getter - this shouldn't even be reached because write-only
				// properties are filtered in listProperties
				throw new PropertyException(String.format(
						"Property %s doesn't have a getter.", this));
			}
			return pd.getReadMethod().invoke(parent.getValue());
		} catch (InvocationTargetException e) {
			throw PropertyException.newGetException(this,
					e.getTargetException());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValue(Object val) throws PropertyException {
		try {
			Object value = parent.getValue();
			pd.getWriteMethod().invoke(value, val);
			parent.setValue(value);
		} catch (InvocationTargetException e) {
			throw PropertyException.newSetException(this,
					e.getTargetException());
		} catch (IllegalArgumentException e) {
			throw PropertyException.newSetException(this, e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canBeNull() {
		return !TypeUtil.toClass(getType()).isPrimitive();
	}

	@Override
	public String toString() {
		return getName();
	}
}
