package minifab.model;

import jasima.shopSim.core.IndividualMachine;
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
		if (justStarted != null) {
			justStarted.valueStorePut(CheckResourceAvailability.LAST_MACHINE,
					m.currMachine);

			MiniFabOperation op = (MiniFabOperation) justStarted
					.getCurrentOperation();
			String key = createKeyName(m.currMachine);
			if (op.isLoadOperation()) {
				m.valueStorePut(key, op);
			} else {
				// check precedence constraint, machine can't be used by other
				// jobs till unloading completed
				assert m.valueStoreGet(key) == op.loadOp;
			}
		}
	}

	public static String createKeyName(IndividualMachine im) {
		String key = "occupied." + im.toString();
		return key;
	}

	@Override
	protected void operationCompleted(WorkStation m,
			PrioRuleTarget justCompleted) {
		Object oldVal = m.valueStoreRemove(createKeyName(m.currMachine));
		assert oldVal==((MiniFabOperation)justCompleted.getCurrentOperation()).loadOp;
		
		justCompleted.valueStorePut(CheckResourceAvailability.LAST_MACHINE,
				m.currMachine);
		// trigger new selection also for operations that did not require the
		// operator
		ops.selectAndStart();
	}
}
