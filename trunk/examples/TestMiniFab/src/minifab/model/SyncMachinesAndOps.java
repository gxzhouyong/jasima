package minifab.model;

import jasima.shopSim.core.IndividualMachine;
import jasima.shopSim.core.Job;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.WorkStation;
import jasima.shopSim.util.WorkStationListenerBase;
import minifab.control.CheckResourceAvailability;

/**
 * This listener is installed on machines to synchronize their decisions with
 * those of the operator pool.
 * 
 * @author Torsten Hildebrandt
 */
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
		if (justStarted == null) {
			return;
		}

		justStarted.valueStorePut(CheckResourceAvailability.LAST_MACHINE,
				m.currMachine);

		MiniFabOperation op = (MiniFabOperation) justStarted
				.getCurrentOperation();

		boolean removeRes = ops.jobsLastStarted().remove(justStarted);
		assert removeRes || op.operator==null;

		String key = createKeyName(m.currMachine);
		if (op.isLoadOperation()) {
			m.valueStorePut(key, op);
		} else {
			// check precedence constraint, machine can't be used by other
			// jobs till unloading completed
			Object value = m.valueStoreGet(key);
			if (value != op.loadOp)
				assert value == op.loadOp : m.shop().simTime() + "\t" + m
						+ "\t" + m.currMachine;
		}
	}

	public static String createKeyName(IndividualMachine im) {
		String key = "occupied." + im.toString();
		return key;
	}

	@Override
	protected void operationCompleted(WorkStation m,
			PrioRuleTarget justCompleted) {
		MiniFabOperation op = (MiniFabOperation) justCompleted
				.getCurrentOperation();
		if (op.isUnloadOperation()) {
			Object oldVal = m.valueStoreRemove(createKeyName(m.currMachine));
			assert oldVal == op.loadOp;
		}

		justCompleted.valueStorePut(CheckResourceAvailability.LAST_MACHINE,
				m.currMachine);
		// trigger new selection also for operations that did not require the
		// operator
		ops.selectAndStart();
	}
}
