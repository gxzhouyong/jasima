package minifab.control;

import jasima.shopSim.core.IndividualMachine;
import jasima.shopSim.core.Job;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.PriorityQueue;
import minifab.model.MiniFabOperation;

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
			MiniFabOperation op = (MiniFabOperation) j.getCurrentOperation();

			if (canStart(op, j)) {
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
		MiniFabOperation op = (MiniFabOperation) job.getCurrentOperation();
		if (canStart(op, (Job) job)) {
			return +1;
		} else {
			return -1;
		}
	}

	public static boolean canStart(MiniFabOperation op, Job j) {
		// operator available
		boolean canStart = op.operator == null
				|| op.operator.numFreeMachines() > 0;

		// machine available?
		if (canStart) {
			// load can be performed on any machine
			if (op.isLoadOperation()) {
				canStart = op.machine.numFreeMachines() > 0;
			} else {
				IndividualMachine lastMachine = (IndividualMachine) j
						.valueStoreGet(LAST_MACHINE);
				assert lastMachine != null;
				canStart = op.machine.isFree(lastMachine);
			}
		}

		return canStart;
	}

}
