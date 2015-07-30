package example;

import jasima.core.experiment.FullFactorialExperiment;
import jasima.core.experiment.MultipleReplicationExperiment;
import jasima.core.util.ConsolePrinter;
import jasima.core.util.ExcelSaver;
import jasima.shopSim.core.PR;
import jasima.shopSim.models.staticShop.StaticShopExperiment;
import jasima.shopSim.prioRules.basic.FCFS;
import jasima.shopSim.prioRules.basic.SPT;
import jasima.shopSim.prioRules.basic.TieBreakerFASFS;
import jasima.shopSim.util.BasicJobStatCollector;
import jasima.shopSim.util.MachineStatCollector;

/**
 * Tests 3 different dispatching rules on three different scenarios (utilization
 * levels). For each factor combination 30 replications of a base experiment (a
 * StaticShopExperiment reading its configuration from a text file) are
 * performed using a MultipleReplicationExperiment.
 */
public class RunRuleSelection {

	public static final PR[] RULES = {
			new FCFS().setFinalTieBreaker(new TieBreakerFASFS()),
			new TieBreakerFASFS(),
			new SPT().setFinalTieBreaker(new TieBreakerFASFS()) };

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("usage: "
					+ RunRuleSelection.class.getSimpleName() + " <seed>");
			return;
		}
		long seed = Long.parseLong(args[0]);

		// create and configure experiment
		StaticShopExperiment sse = createBaseExperiment();

		// we want multiple replications
		MultipleReplicationExperiment mre = new MultipleReplicationExperiment();
		mre.setBaseExperiment(sse);
		mre.setMaxReplications(30);

		// we want to test multiple settings
		FullFactorialExperiment ffe = new FullFactorialExperiment();
		ffe.setBaseExperiment(mre);

		// ffe will overwrite seed of mre which when run will overwrite seed of
		// sse
		ffe.setInitialSeed(seed);

		ffe.addNotifierListener(new ConsolePrinter());
		ffe.addNotifierListener(new ExcelSaver());

		// add experimental factors we want to vary
		ffe.addFactors("baseExperiment.instFileName", "miniFabNoOps70.txt",
				"miniFabNoOps80.txt", "miniFabNoOps90.txt");
		ffe.addFactors("baseExperiment.sequencingRule", RULES);

		// run it
		ffe.runExperiment();
		ffe.printResults();
	}

	private static StaticShopExperiment createBaseExperiment() {
		StaticShopExperiment sse = new StaticShopExperiment();
		sse.setName("static1");
		sse.setInitialSeed(42);
		sse.setInstFileName("miniFabNoOps90.txt");
		sse.setSimulationLength(10 * 360 * 24 * 60.0); // 10 years

		PR seqRule = new SPT().setFinalTieBreaker(new TieBreakerFASFS());
		sse.setSequencingRule(seqRule);

		// we want some additional statistics to be produced
		sse.addShopListener(new BasicJobStatCollector());
		sse.addMachineListener(new MachineStatCollector());

		return sse;
	}

}
