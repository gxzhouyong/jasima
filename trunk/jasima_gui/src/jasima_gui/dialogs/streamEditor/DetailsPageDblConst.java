package jasima_gui.dialogs.streamEditor;

public class DetailsPageDblConst extends DetailsPageBase {

	public static final String TITLE = "Constant numbers";
	public static final String DESCRIPTION = "Defines a list of repeating real numbers.";
	public static final String INPUT_TYPE = "jasima.shopSim.util.modelDef.streams.DblConstDef";

	public DetailsPageDblConst() {
		super();
		addDoubleListProperty("values", "values");
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
