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

public enum ConversionReportCategory {
	NEW_PROPERTY("The following properties are new and will be set to their default values:"), //
	PROPERTY_DISAPPEARED("The following properties no longer exist and will be discarded:"), //
	ALLOWED_VALUES_CHANGED("The following properties could not be set to their previously saved values:"), //
	TYPE_CHANGED("The following properties have changed their types and will be set to their default values:");

	private ConversionReportCategory(String introText) {
		this.introText = introText;
	}

	public String introText;
}
