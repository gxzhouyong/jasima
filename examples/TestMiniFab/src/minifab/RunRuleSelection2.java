package minifab;

import jasima.core.experiment.FullFactorialExperiment;
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
 * levels). For each factor combination 10 replications (i.e. seed values) of
 * the base experiment are performed. In contrast to RunRuleSelection this time
 * we don't use a MultipleReplicationExperiment but instead set certain seeds
 * directly. This is useful to be able to perform pairwise tests.
 */
public class RunRuleSelection2 {

	public static final PR[] RULES = {
			new FCFS().setFinalTieBreaker(new TieBreakerFASFS()),
			new TieBreakerFASFS(),
			new SPT().setFinalTieBreaker(new TieBreakerFASFS()) };

	public static void main(String[] args) throws Exception {
		// create and configure experiment
		StaticShopExperiment sse = createBaseExperiment();

		// we want to test multiple settings
		FullFactorialExperiment ffe = new FullFactorialExperiment();
		ffe.setBaseExperiment(sse);
		// seed of ffe will have no effect
		ffe.setInitialSeed(23);

		ffe.addNotifierListener(new ConsolePrinter());
		ffe.addNotifierListener(new ExcelSaver());

		// add experimental factors we want to vary
		ffe.addFactors("instFileName", "miniFabNoOps70.txt",
				"miniFabNoOps80.txt", "miniFabNoOps90.txt");
		ffe.addFactors("sequencingRule", RULES);
		ffe.addFactors("initialSeed", 23, 24, 25, 26, 27, 28, 29, 30, 31, 32);

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
