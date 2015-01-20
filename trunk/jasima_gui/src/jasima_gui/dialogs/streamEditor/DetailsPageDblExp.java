package jasima_gui.dialogs.streamEditor;

import static jasima_gui.dialogs.streamEditor.util.Constraints.positive;

public class DetailsPageDblExp extends DetailsPageBase {

	public static final String TITLE = "Exponential distribution";
	public static final String DESCRIPTION = "Defines an exponential destribution with a certain mean value.";
	public static final String INPUT_TYPE = "jasima.shopSim.util.modelDef.streams.DblExponentialDef";

	public DetailsPageDblExp() {
		super();
		FormProperty mean = addDoubleProperty("mean", "mean value");
		addConstraint(positive(mean));
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
