package example;

import jasima.shopSim.core.JobShopExperiment;
import jasima.shopSim.core.Route;
import jasima.shopSim.core.WorkStation;

public class MiniFabWithOpsExperiment extends JobShopExperiment {

	public static final int[] LOAD_TIMES = { 10, 0, 5, 0, 0, 0 };
	public static final int[] PROC_TIMES = { 20, 10, 10, 80, 105, 30 };
	public static final int[] UNLOAD_TIMES = { 10, 15, 10, 0, 0, 0 };
	public static final String[] WORKSTATIONS = { "G1_Mab", "G2_Mcd", "G3_Me",
			"G2_Mcd", "G1_Mab", "G3_Me" };

	private int numOps = 0;

	public MiniFabWithOpsExperiment() {
		super();
	}

	@Override
	protected void createShop() {
		super.createShop();

		createResources();
		createRoute();
		createJobSource();

	}

	private void createJobSource() {
		// TODO Auto-generated method stub

	}

	private void createRoute() {
		Route r = new Route();

		assert LOAD_TIMES.length == PROC_TIMES.length;
		assert PROC_TIMES.length == UNLOAD_TIMES.length;
		assert UNLOAD_TIMES.length == WORKSTATIONS.length;

		for (int i = 0; i < LOAD_TIMES.length; i++) {
			MiniFabOperation o = new MiniFabOperation();

			double l = LOAD_TIMES[i];
			double p = PROC_TIMES[i];
			double u = UNLOAD_TIMES[i];
			WorkStation ws = shop.getWorkstationByName(WORKSTATIONS[i]);

			o.machine = ws;
			o.procTime = l + p + u;
			o.loadTime = l;
			o.mainProcTime = p;
			o.unloadTime = u;

			r.addSequentialOperation(o);
		}

		shop.addRoute(r);
	}

	private void createResources() {
		WorkStation ma = new WorkStation(2);
		ma.setName("G1_Mab");
		shop.addMachine(ma);

		WorkStation mb = new WorkStation(2);
		mb.setName("G2_Mcd");
		shop.addMachine(mb);

		WorkStation mc = new WorkStation(1);
		mc.setName("G3_Me");
		shop.addMachine(mc);

		if (getNumOps() > 0) {
			WorkStation ops = new WorkStation(getNumOps());
			ops.setName("Ops");
			shop.addMachine(ops);
		}
	}

	public int getNumOps() {
		return numOps;
	}

	public void setNumOps(int numOps) {
		this.numOps = numOps;
	}

}
