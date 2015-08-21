package minifab.model;

import jasima.shopSim.core.Job;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.WorkStation;
import jasima.shopSim.util.WorkStationListenerBase;

public class SyncMachinesAndOps2 extends WorkStationListenerBase {

	public SyncMachinesAndOps2() {
		super();
	}

	@Override
	protected void operationStarted(WorkStation m, PrioRuleTarget justStarted,
			int oldSetupState, int newSetupState, double setupTime) {
		Job j = (Job) justStarted;
		MiniFabOperation op = (MiniFabOperation) j.getCurrentOperation();

		OperatorGroup ws = (OperatorGroup) op.machine;
		ws.removeFromQueue(j);
		ws.currMachine = ws.freeMachines.peek();
		ws.startProc(justStarted);
	}

}
