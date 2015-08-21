package minifab.control;

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
public class CheckResourceAvailability extends PR {

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

			if (op.canStart()) {
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
		if (op.canStart()) {
			return +1;
		} else {
			return -1;
		}
	}

}
