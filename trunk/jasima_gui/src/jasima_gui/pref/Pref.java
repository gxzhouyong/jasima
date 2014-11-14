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
package jasima_gui.pref;

import jasima_gui.Activator;

import org.eclipse.jface.preference.IPreferenceStore;

public abstract class Pref {

	public static final StrPref EXP_RES_FMT = new StrPref(
			"default-experiment-result-format", "--xlsres");

	public static final StrPref XLS_EXP_RES_FMT = new StrPref(
			"default-excel-experiment-result-format", "--xlsres");

	public static final StrPref JASIMA_VERSION = new StrPref(
			"default-jasima-version", "1.1.0");

	public final String key;

	Pref(String key) {
		this.key = key;
	}

	public static IPreferenceStore prefStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public abstract void initDefault();
}
