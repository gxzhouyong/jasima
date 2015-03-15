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
package jasima_gui;

import jasima_gui.util.TypeUtil;

import java.util.Formatter;

public class ConversionReport {
	public static final String HREF_CONFIRM = "jasima-command:confirm-conversion-report";
	protected Formatter fmt = null;

	public void finish() {
		if (fmt != null) {
			fmt.format("<p><a href='%s'>Confirm</a> and continue editing</p>", HREF_CONFIRM);
			fmt.format("</form>");
		}
	}

	public boolean isEmpty() {
		return fmt == null;
	}

	public String toString() {
		return fmt.toString();
	}

	protected void ensureFormatterExists() {
		if (fmt == null) {
			fmt = new Formatter();
			fmt.format("<form>");
			fmt.format("<p>The following changes occured to classes since the file was saved:</p>");
		}
	}

	public void newProperty(Class<?> type, String propertyName) {
		ensureFormatterExists();
		String st = TypeUtil.toString(type, true);
		fmt.format("<li><span font='code'>%s</span> now has the new property “<span font='code'>%s</span>”</li>", st,
				propertyName);
	}

	public void propertyDisappeared(Class<?> type, String propertyName) {
		ensureFormatterExists();
		String st = TypeUtil.toString(type, true);
		fmt.format("<li><span font='code'>%s</span> no longer has the property “<span font='code'>%s</span>”</li>", st,
				propertyName);
	}
}
