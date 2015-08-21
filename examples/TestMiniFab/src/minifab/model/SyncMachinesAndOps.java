package minifab.model;

import jasima.shopSim.core.Job;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.WorkStation;
import jasima.shopSim.util.WorkStationListenerBase;
import minifab.control.CheckResourceAvailability;

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

		if (op.operator != null) {
			op.operator.enqueueOrProcess(justArrived);
			assert justArrived.getCurrMachine() == ops;
			justArrived.setCurrMachine(m);
		}
	}

	@Override
	protected void operationStarted(WorkStation m, PrioRuleTarget justStarted,
			int oldSetupState, int newSetupState, double setupTime) {
		if (justStarted != null)
			justStarted.valueStorePut(CheckResourceAvailability.LAST_MACHINE,
					m.currMachine);
	}

	@Override
	protected void operationCompleted(WorkStation m,
			PrioRuleTarget justCompleted) {
		justCompleted.valueStorePut(CheckResourceAvailability.LAST_MACHINE,
				m.currMachine);
		// trigger new selection also for operations that did not require the
		// operator
		ops.selectAndStart();
	}
}
