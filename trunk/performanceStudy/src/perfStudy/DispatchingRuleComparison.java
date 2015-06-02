package perfStudy;

import jasima.core.experiment.FullFactorialExperiment;
import jasima.core.experiment.MultipleReplicationExperiment;
import jasima.core.random.discrete.IntUniformRange;
import jasima.core.util.ConsolePrinter;
import jasima.core.util.ExcelSaver;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment.Scenario;
import jasima.shopSim.prioRules.basic.SPT;
import jasima.shopSim.prioRules.basic.TieBreakerFASFS;
import jasima.shopSim.prioRules.gp.GECCO2010_genSeed_10reps;
import jasima.shopSim.prioRules.gp.GECCO2010_genSeed_2reps;
import jasima.shopSim.prioRules.upDownStream.IFTMinusUITPlusNPT;
import jasima.shopSim.prioRules.upDownStream.PTPlusWINQ;
import jasima.shopSim.prioRules.upDownStream.PTPlusWINQPlusNPT;
import jasima.shopSim.util.BatchStatCollector;

public class DispatchingRuleComparison {

	public static void main(String[] args) {
		// create the main experiment
		DynamicShopExperiment be = new DynamicShopExperiment();
		// set some parameters as required
		be.setNumOpsMax(10);
		be.setStopArrivalsAfterNumJobs(10000);
		// BatchStatCollector creates the statistics we are interested in
		BatchStatCollector bsc = new BatchStatCollector();
		bsc.setIgnoreFirst(2000);
		bsc.setBatchSize(8000);
		// so add it to "be"
		be.addShopListener(bsc);

		// we want 30 independent replications
		MultipleReplicationExperiment mre = new MultipleReplicationExperiment(
				be, 30);

		// use FullFactorialExperiment to test various parameter combinations,
		// each running a MultipleReplicationExperiment
		FullFactorialExperiment ffe = new FullFactorialExperiment();
		ffe.setBaseExperiment(mre);
		ffe.setInitialSeed(1234567890);

		// test each value of the enum Scenario (i.e., FLOW_SHOP and JOB_SHOP)
		ffe.addFactors("baseExperiment.scenario", Scenario.class);
		// use 80% and 95% as utilization levels to test
		ffe.addFactors("baseExperiment.utilLevel", 0.8, 0.95);
		// vary minimum number of operations per job, max is fixed at 10
		ffe.addFactors("baseExperiment.numOpsMin", 2, 10);
		// use two different processing time distributions
		ffe.addFactors("baseExperiment.procTimes", new IntUniformRange(1, 49),
				new IntUniformRange(15, 35));

		// finally we test 11 different values as a sequencing rule, this time
		// we use the method addFactor() instead of addFactors() to add a single
		// factor value with each call
		ffe.addFactor("baseExperiment.sequencingRule", new TieBreakerFASFS());
		ffe.addFactor("baseExperiment.sequencingRule",
				new SPT().setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule",
				new PTPlusWINQ().setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule", new PTPlusWINQPlusNPT()
				.setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule", new IFTMinusUITPlusNPT()
				.setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule",
				new GECCO2010_genSeed_2reps()
						.setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule",
				new GECCO2010_genSeed_10reps()
						.setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule",
				new Rule_ASP2013_Rule_6()
						.setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule",
				new Rule_EC2014_Lin_Base()
						.setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule",
				new Rule_EC2014_Tree_Base_Norm_ND()
						.setFinalTieBreaker(new TieBreakerFASFS()));
		ffe.addFactor("baseExperiment.sequencingRule",
				new Rule_EC2014_Tree_Ext_Norm_ND()
						.setFinalTieBreaker(new TieBreakerFASFS()));

		// print some status messages to the console during the run
		ConsolePrinter printer = new ConsolePrinter();
		ffe.addNotifierListener(printer);

		// we want results saved in an Excel file
		ExcelSaver saver = new ExcelSaver();
		ffe.addNotifierListener(saver);

		// run the experiment, we are testing 2*2*2*2*11=176 parameter
		// combinations
		ffe.runExperiment();

		// print results of top-level experiment to console
		ffe.printResults();
	}
}
