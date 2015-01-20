package jasima_gui.dialogs.streamEditor;

public class DetailsPageIntConst extends DetailsPageBase {

	public static final String TITLE = "Constant integer numbers";
	public static final String DESCRIPTION = "Defines a list of repeating integers.";
	public static final String INPUT_TYPE = "jasima.shopSim.util.modelDef.streams.IntConstDef";

	public DetailsPageIntConst() {
		super();
		addIntegerListProperty("values", "values");
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
