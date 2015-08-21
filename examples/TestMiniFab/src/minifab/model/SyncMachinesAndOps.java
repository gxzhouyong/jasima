package minifab.model;

import jasima.shopSim.core.Job;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.WorkStation;
import jasima.shopSim.util.WorkStationListenerBase;

public class SyncMachinesAndOps extends WorkStationListenerBase {

	protected WorkStation ops;

	@Override
	protected void init(WorkStation m) {
		ops = m.shop().getWorkstationByName(
				MiniFabWithOpsExperiment.OPS_POOL_NAME);
	}

	@Override
	protected void arrival(WorkStation m, Job justArrived) {
		MiniFabOperation op = (MiniFabOperation) justArrived
				.getCurrentOperation();

		assert op.operator == ops || op.operator == null;
		if (ops != null)
			ops.enqueueOrProcess(justArrived);
	}

	@Override
	protected void operationCompleted(WorkStation m,
			PrioRuleTarget justCompleted) {
		if (ops != null)
			// trigger new selection also for operations that do not require the
			// operator
			ops.selectAndStart();
	}

}
