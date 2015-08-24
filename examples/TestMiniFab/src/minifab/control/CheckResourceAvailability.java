package minifab.control;

import jasima.shopSim.core.IndividualMachine;
import jasima.shopSim.core.Job;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.PriorityQueue;
import jasima.shopSim.core.WorkStation;

import java.util.Collection;

import minifab.model.MiniFabOperation;
import minifab.model.SyncMachinesAndOps;

/**
 * Tests whether both machine and operator are available.
 * 
 * @author Torsten Hildebrandt
 */
@SuppressWarnings("serial")
public class CheckResourceAvailability extends PR {

	public static final String LAST_MACHINE = "lastMachine";

	private boolean canStartAny;

	public CheckResourceAvailability() {
		super();
	}

	@Override
	public void beforeCalc(PriorityQueue<? extends PrioRuleTarget> q) {
		super.beforeCalc(q);

		canStartAny = false;
		for (int i = 0; i < q.size(); i++) {
			Job j = (Job) q.get(i);

			if (canStart(j)) {
				canStartAny = true;
				break;
			}
		}
	}

	@Override
	public boolean keepIdle() {
		return !canStartAny;
	}

	@Override
	public double calcPrio(PrioRuleTarget job) {
		if (canStart((Job) job)) {
			return +1;
		} else {
			return -1;
		}
	}

	public static boolean canStart(Job j) {
		MiniFabOperation op = (MiniFabOperation) j.getCurrentOperation();

		// operator available
		boolean canStart = op.operator == null
				|| op.operator.numFreeMachines() > 0;

		// machine available?
		if (canStart) {
			canStart = compatibleMachines(op.machine, j) > 0;
		}

		return canStart;
	}

	private static int compatibleMachines(WorkStation m, Job j) {
		MiniFabOperation op = (MiniFabOperation) j.getCurrentOperation();
		MiniFabOperation loadOp = op.loadOp;

		IndividualMachine lastMachine = (IndividualMachine) j
				.valueStoreGet(LAST_MACHINE);
		assert lastMachine != null;

		Collection<IndividualMachine> ms = m.getFreeMachines();
		int n = ms.size();
		// are they really available or waiting for other proc or unload
		// operation
		for (IndividualMachine im : ms) {
			Object o = m.valueStoreGet(SyncMachinesAndOps.createKeyName(im));
			if (o != null && o != loadOp)
				n--;
		}

		return n;
	}

}
