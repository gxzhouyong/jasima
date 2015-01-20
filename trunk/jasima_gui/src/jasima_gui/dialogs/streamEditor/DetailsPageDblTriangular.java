package jasima_gui.dialogs.streamEditor;

import static jasima_gui.dialogs.streamEditor.util.Constraints.smallerOrEqualThan;

public class DetailsPageDblTriangular extends DetailsPageBase {

	public static final String INPUT_TYPE = "jasima.shopSim.util.modelDef.streams.DblTriangularDef";
	public static final String TITLE = "Triangular distribution";
	public static final String DESCRIPTION = "Defines a triangular destribution by its minimum and "
			+ "maximum value as well as its mode (most frequent value).";

	public DetailsPageDblTriangular() {
		super();

		FormProperty min = addDoubleProperty("minValue", "minimum value");
		FormProperty mode = addDoubleProperty("modeValue", "mode value");
		FormProperty max = addDoubleProperty("maxValue", "maximum value");

		addConstraint(smallerOrEqualThan(min, mode));
		addConstraint(smallerOrEqualThan(mode, max));
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
