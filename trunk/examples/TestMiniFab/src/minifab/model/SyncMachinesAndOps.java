package minifab.model;

import jasima.shopSim.core.Job;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.WorkStation;
import jasima.shopSim.util.WorkStationListenerBase;

public class SyncMachinesAndOps extends WorkStationListenerBase {

	protected OperatorGroup ops;

	@Override
	protected void init(WorkStation m) {
		ops = (OperatorGroup) m.shop().getWorkstationByName(
				MiniFabWithOpsExperiment.OPS_POOL_NAME);
	}

	@Override
	protected void arrival(WorkStation m, Job justArrived) {
		MiniFabOperation op = (MiniFabOperation) justArrived
				.getCurrentOperation();
		// just one op group for now
		assert op.operator == ops || op.operator == null;

		if (op.operator != null)
			op.operator.enqueueOrProcess(justArrived);
	}

	@Override
	protected void operationCompleted(WorkStation m,
			PrioRuleTarget justCompleted) {
		// trigger new selection also for operations that did not require the
		// operator
		if (ops != null)
			ops.selectAndStart();
	}
}
