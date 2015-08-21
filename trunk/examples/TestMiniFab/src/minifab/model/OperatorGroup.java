package minifab.model;

import jasima.shopSim.core.Job;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.WorkStation;
import jasima.shopSim.core.IndividualMachine.MachineState;

public class OperatorGroup extends WorkStation {

	public OperatorGroup() {
		this(1);
	}

	public OperatorGroup(int numInGroup) {
		super(numInGroup);
	}

	@Override
	public void startProc(PrioRuleTarget batch) {
		super.startProc(batch);
	}

	/**
	 * Called when an operation of Job j is finished. This is the same method as
	 * in WorkStation, only jobs are not notified (this is done by the
	 * machines and has to occur only once).
	 */
	@Override
	protected void depart() {
		assert currMachine.state == MachineState.WORKING;

		PrioRuleTarget b = currMachine.curJob;
		currMachine.curJob = null;

		currMachine.state = MachineState.IDLE;
		currMachine.procFinished = -1.0d;
		currMachine.procStarted = -1.0d;
		freeMachines.addFirst(currMachine);

		numBusy--;

		if (numListener() > 0) {
			justCompleted = b;
			fire(WS_JOB_COMPLETED);
			justCompleted = null;
		}

		// for (int i = 0, n = b.numJobsInBatch(); i < n; i++) {
		// Job j = b.job(i);
		// j.endProcessing();
		// send jobs to next machine
		// j.proceed();
		// }

		currMachine = null;

		// start next job on this machine
		if (numJobsWaiting() > 0)
			selectAndStart();
	}

}
