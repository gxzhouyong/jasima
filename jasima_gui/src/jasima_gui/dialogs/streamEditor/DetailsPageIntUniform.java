package jasima_gui.dialogs.streamEditor;

import static jasima_gui.dialogs.streamEditor.util.Constraints.smallerThan;

public class DetailsPageIntUniform extends DetailsPageBase {

	public static final String TITLE = "Uniform discrete distribution";
	public static final String DESCRIPTION = "Defines a uniform discrete distribution between a lower and an upper bound. "
			+ "Please enter integers for minimum and maximum values.";
	public static final String INPUT_TYPE = "jasima.shopSim.util.modelDef.streams.IntUniformDef";

	public DetailsPageIntUniform() {
		super();

		FormProperty min = addIntegerProperty("minValue", "minimum value");
		FormProperty max = addIntegerProperty("maxValue", "maximum value");
		addConstraint(smallerThan(min, max));
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
