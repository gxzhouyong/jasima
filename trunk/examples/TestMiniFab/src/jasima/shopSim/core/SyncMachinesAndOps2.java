package jasima.shopSim.core;

import jasima.shopSim.util.WorkStationListenerBase;
import example.MiniFabOperation;

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
