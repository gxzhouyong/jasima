package minifab.control;

import jasima.shopSim.core.IndividualMachine;
import jasima.shopSim.core.Job;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.PriorityQueue;
import jasima.shopSim.prioRules.basic.TieBreakerFASFS;
import minifab.model.MiniFabOperation;
import minifab.model.MiniFabWithOpsExperiment;
import minifab.model.OperatorGroup;

/**
 * This is a kind of dummy PR for machine resources that only looks at the
 * decisions of the operator pool and assigns the right priority values to match
 * the operator's decision.
 * 
 * @author Torsten Hildebrandt
 */
@SuppressWarnings("serial")
public class MachinesAsOpSlavesPR extends PR {

	private OperatorGroup ops;
	private boolean haveProcOperation;

	public MachinesAsOpSlavesPR() {
		super();

		setTieBreaker(new TieBreakerFASFS());
	}

	@Override
	public void init() {
		super.init();

		ops = (OperatorGroup) getOwner().shop().getWorkstationByName(
				MiniFabWithOpsExperiment.OPS_POOL_NAME);
		assert ops != null;
	}

	@Override
	public void beforeCalc(PriorityQueue<? extends PrioRuleTarget> q) {
		haveProcOperation = false;

		// determine job to be started here so we know whether to keep the
		// machine idle
		for (int i = 0, n = q.size(); i < n; i++) {
			Job j = (Job) q.get(i);
			MiniFabOperation op = (MiniFabOperation) j.getCurrentOperation();

			// is there a processing operation
			if (op.isProcessingOperation()) {
				haveProcOperation = true;
				break;
			}
		}
	}

	@Override
	public boolean keepIdle() {
		if (haveProcOperation) {
			// processing operations can be started right away
			return false;
		} else {
			// for everything else we have to check what the operator did
			MiniFabOperation op = (MiniFabOperation) ops.jobLastStarted()
					.getCurrentOperation();
			return op.machine != getOwner();
		}
	}

	@Override
	public double calcPrio(PrioRuleTarget entry) {
		if (entry == ops.jobLastStarted()) {
			IndividualMachine im = (IndividualMachine) entry
					.valueStoreGet(CheckResourceAvailability.LAST_MACHINE);
			// force unloading operations on the correct machine
			if (getOwner().currMachine == im) {
				return +2;
			} else {
				return +1;
			}
		} else {
			return -1;
		}
	}

}
