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
