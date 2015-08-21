package minifab.model;

import jasima.core.simulation.Event;
import jasima.shopSim.core.Job;
import jasima.shopSim.core.Operation;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.WorkStation;

public class OperatorGroup extends WorkStation {

	private static final double SMALL_TIME = 1e-4;
	private Job jobLastStarted;

	public OperatorGroup() {
		this(1);
	}

	public OperatorGroup(int numInGroup) {
		super(numInGroup);
	}

	@Override
	public void selectAndStart() {
		// slightly before normal selection (higher event priority), so normal
		// machines can see the decision of the operators
		Event selectEvent = new Event(shop().simTime(), SELECT_PRIO - 1000) {
			@Override
			public void handle() {
				// are there jobs that could be started and is there at
				// least one free machine
				if (numBusy < numInGroup() && numJobsWaiting() > 0) {
					// at least 1 machine idle, start job selection
					selectAndStart0();
				}
			}
		};

		// execute asynchronously so all jobs arrived/departed before selection
		shop.schedule(selectEvent);
	}

	@Override
	protected double procTime(Operation op) {
		MiniFabOperation o = (MiniFabOperation) op;
		if (o.operator == null) {
			return SMALL_TIME;
		} else {
			return super.procTime(op);
		}
	}

	/**
	 * Start processing the current batch/job.
	 */
	@Override
	public void startProc(PrioRuleTarget batch) {
		super.startProc(batch);

		jobLastStarted = (Job) batch;

		// be use machine always follows operator
		batch.getCurrentOperation().machine.selectAndStart();

		// allow multiple operations to be started at (almost) the same time
		Event selectEvent = new Event(shop().simTime() + SMALL_TIME,
				SELECT_PRIO - 1000) {
			@Override
			public void handle() {
				// are there jobs that could be started and is there at
				// least one free machine
				if (numBusy < numInGroup() && numJobsWaiting() > 0) {
					// at least 1 machine idle, start job selection
					selectAndStart0();
				}
			}
		};
		shop.schedule(selectEvent);
	}

	@Override
	protected void notifyJobsOfProcStart(PrioRuleTarget batch) {
		// do nothing
	}

	@Override
	protected void notifyJobsOfDepart(PrioRuleTarget b) {
		// do nothing
	}

	public Job jobLastStarted() {
		return jobLastStarted;
	}
}
