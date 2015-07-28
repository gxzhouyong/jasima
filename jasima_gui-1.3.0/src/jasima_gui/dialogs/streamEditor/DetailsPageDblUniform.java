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
package jasima_gui.dialogs.streamEditor;

import static jasima_gui.dialogs.streamEditor.util.Constraints.smallerThan;

public class DetailsPageDblUniform extends DetailsPageBase {

	public static final String TITLE = "Uniform distribution";
	public static final String DESCRIPTION = "Defines a uniform continuous distribution between a lower and an upper bound. Please enter real numbers for minimum and maximum values.";
	public static final String INPUT_TYPE = "jasima.shopSim.util.modelDef.streams.DblUniformDef";

	public DetailsPageDblUniform() {
		super();
		FormProperty p1 = addDoubleProperty("minValue", "minimum value");
		FormProperty p2 = addDoubleProperty("maxValue", "maximum value");
		addConstraint(smallerThan(p1, p2));
	}

	@Override
	public String getInputType() {
		return INPUT_TYPE;
	}

	@Override
	protected String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
