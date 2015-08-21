package minifab;

import java.util.Map;

import jasima.core.experiment.Experiment.ExpMsgCategory;
import jasima.core.util.ConsolePrinter;
import jasima.core.util.ExcelSaver;
import jasima.shopSim.core.PR;
import jasima.shopSim.models.staticShop.StaticShopExperiment;
import jasima.shopSim.prioRules.basic.SPT;
import jasima.shopSim.prioRules.basic.TieBreakerFASFS;
import jasima.shopSim.util.BasicJobStatCollector;
import jasima.shopSim.util.MachineStatCollector;
import jasima.shopSim.util.TraceFileProducer;

public class RunSingle {

	public static void main(String[] args) {
		// create and configure experiment
		StaticShopExperiment sse = new StaticShopExperiment();
		sse.setName("static1");
		sse.setInitialSeed(42);
		sse.setInstFileName("miniFabNoOps90.txt");
		sse.setSimulationLength(1 * 360 * 24 * 60.0); // 10 years

		PR seqRule = new SPT().setFinalTieBreaker(new TieBreakerFASFS());
		sse.setSequencingRule(seqRule);

		// we want some additional statistics to be produced
		sse.addShopListener(new BasicJobStatCollector());
		sse.addMachineListener(new MachineStatCollector());

		// optionally create a detailed simulation trace
//		sse.addShopListener(new TraceFileProducer());

		// save results in Excel format (optional)
		sse.addNotifierListener(new ExcelSaver());

		// show any messages during run
		sse.addNotifierListener(new ConsolePrinter(ExpMsgCategory.ALL));

		// run experiment
		Map<String, Object> res = sse.runExperiment();

		// results can now be accessed in map "res"
		Double cmax = (Double) res.get("cMax");
		System.out.println("cMax:\t" + cmax);

		// print results to console
		sse.printResults();
	}

}
