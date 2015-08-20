package example;

import jasima.shopSim.core.Job;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.PriorityQueue;

public class CheckOperatorAvailability extends PR {

	private boolean canStartAny;

	public CheckOperatorAvailability() {
		super();
	}

	@Override
	public double calcPrio(PrioRuleTarget entry) {
		return 0;
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

}
