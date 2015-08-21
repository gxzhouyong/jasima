package minifab.model;

import jasima.shopSim.core.Operation;
import jasima.shopSim.core.WorkStation;

public class MiniFabOperation extends Operation {

	public MiniFabOperation loadOp, procOp, unloadOp;
	public WorkStation operator;
	public double operatorTime;

	public MiniFabOperation() {
		super();
		loadOp = procOp = unloadOp = null;
		operator = null;
		operatorTime = 0.0;
	}

	public boolean isLoadOperation() {
		return this == loadOp;
	}

	public boolean isProcessingOperation() {
		return this == procOp;
	}

	public boolean isUnloadOperation() {
		return this == unloadOp;
	}

	public double totalProcTime() {
		return loadOp.procTime + procOp.procTime + unloadOp.procTime;
	}

}
