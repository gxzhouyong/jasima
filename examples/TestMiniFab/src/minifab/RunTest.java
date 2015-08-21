package minifab;

import jasima.core.experiment.Experiment.ExpMsgCategory;
import jasima.core.util.ConsolePrinter;
import jasima.shopSim.prioRules.basic.SPT;
import jasima.shopSim.prioRules.basic.TieBreakerFASFS;
import jasima.shopSim.util.TraceFileProducer;
import minifab.model.MiniFabWithOpsExperiment;

public class RunTest {

	public static void main(String[] args) {
		MiniFabWithOpsExperiment ex = new MiniFabWithOpsExperiment();
		ex.setSequencingRule(new SPT()
				.setFinalTieBreaker(new TieBreakerFASFS()));
		ex.setSimulationLength(24 * 60);

		ex.addShopListener(new TraceFileProducer());
		ex.addNotifierListener(new ConsolePrinter(ExpMsgCategory.ALL));

		ex.runExperiment();
		ex.printResults();
	}

}
