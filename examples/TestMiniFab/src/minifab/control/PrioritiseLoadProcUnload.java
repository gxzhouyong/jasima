package minifab.control;

import minifab.model.MiniFabOperation;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;

@SuppressWarnings("serial")
public class PrioritiseLoadProcUnload extends PR {

	public PrioritiseLoadProcUnload() {
		super();
	}

	@Override
	public double calcPrio(PrioRuleTarget job) {
		MiniFabOperation op = (MiniFabOperation) job.getCurrentOperation();

		if (op.isLoadOperation())
			return 1;
		else if (op.isProcessingOperation())
			return 2;
		else if (op.isUnloadOperation())
			return 3;
		else
			throw new AssertionError();
	}

}
