package jasima_gui.dialogs.streamEditor;

public class DetailsPageIntEmpirical extends DetailsPageBase {

	public static final String TITLE = "Empirical integer distribution";
	public static final String DESCRIPTION = "Defines a list of integers each occurring with a certain probablity.";
	public static final String INPUT_TYPE = "jasima.shopSim.util.modelDef.streams.IntEmpDef";

	public DetailsPageIntEmpirical() {
		super();
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
