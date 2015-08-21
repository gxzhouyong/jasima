package minifab.control;

import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import minifab.model.MiniFabOperation;

@SuppressWarnings("serial")
public class PrioritiseProcOperations extends PR {

	public PrioritiseProcOperations() {
		super();
	}

	@Override
	public double calcPrio(PrioRuleTarget job) {
		MiniFabOperation op = (MiniFabOperation) job.getCurrentOperation();

		if (op.isProcessingOperation())
			return +1;
		else
			// load or unload
			return -1;
	}

}
