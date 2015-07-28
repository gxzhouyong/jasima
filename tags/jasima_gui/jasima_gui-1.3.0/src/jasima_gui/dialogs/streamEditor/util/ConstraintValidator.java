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
