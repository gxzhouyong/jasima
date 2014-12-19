import jasima.core.experiment.AbstractMultiConfExperiment.ComplexFactorSetter;
import jasima.core.experiment.Experiment;
import jasima.core.experiment.FullFactorialExperiment;
import jasima.core.experiment.MultipleReplicationExperiment;
import jasima.core.util.ConsolePrinter;
import jasima.core.util.ExcelSaver;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.PriorityQueue;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment;
import jasima.shopSim.models.dynamicShop.DynamicShopExperiment.Scenario;
import jasima.shopSim.prioRules.basic.SPT;
import jasima.shopSim.prioRules.basic.TieBreakerFASFS;
import jasima.shopSim.prioRules.gp.GECCO2010_genSeed_2reps;
import jasima.shopSim.prioRules.gp.GECCO2010_genSeed_10reps;
import jasima.shopSim.prioRules.gp.GPRuleBase;
import jasima.shopSim.prioRules.meta.IgnoreFutureJobs;
import jasima.shopSim.prioRules.upDownStream.IFTMinusUITPlusNPT;
import jasima.shopSim.prioRules.upDownStream.PTPlusWINQ;
import jasima.shopSim.prioRules.upDownStream.PTPlusWINQPlusNPT;
import jasima.shopSim.util.BatchStatCollector;

public class DispatchingRuleComparisons {

	public static void main(String[] args) {
		DynamicShopExperiment be = new DynamicShopExperiment();
		be.setNumOpsMax(10);
		be.setEnableLookAhead(true);
		be.setStopArrivalsAfterNumJobs(10000);
		BatchStatCollector bsc = new BatchStatCollector();
		bsc.setIgnoreFirst(2000);
		bsc.setBatchSize(8000);
		be.addShopListener(bsc);

		MultipleReplicationExperiment mre = new MultipleReplicationExperiment(be, 30);

		FullFactorialExperiment ffe = new FullFactorialExperiment();
		ffe.setInitialSeed(1234567890);
		ffe.setCommonRandomNumbers(true);
		ffe.setBaseExperiment(mre);

		ffe.addFactor("baseExperiment.scenario", Scenario.JOB_SHOP);
		ffe.addFactor("baseExperiment.scenario", Scenario.FLOW_SHOP);
		ffe.addFactor("baseExperiment.utilLevel", 0.8);
		ffe.addFactor("baseExperiment.utilLevel", 0.95);
		ffe.addFactor("baseExperiment.numOpsMin", 2);
		ffe.addFactor("baseExperiment.numOpsMin", 10);
		ffe.addFactor("baseExperiment.opProcTime", new opProcPair(1, 49));
		ffe.addFactor("baseExperiment.opProcTime", new opProcPair(15, 35));

		ffe.addFactor("baseExperiment.sequencingRule", new IgnoreFutureJobs(new TieBreakerFASFS()));
		PR[] nonDelayRules = new PR[10];
		nonDelayRules[0] = new SPT();
		nonDelayRules[1] = new PTPlusWINQ();
		nonDelayRules[2] = new PTPlusWINQPlusNPT();
		nonDelayRules[3] = new IFTMinusUITPlusNPT();
		nonDelayRules[4] = new GECCO2010_genSeed_2reps();
		nonDelayRules[5] = new GECCO2010_genSeed_10reps();
		nonDelayRules[6] = ASP2013_Rule_6;
		nonDelayRules[7] = EC2014_Lin_Base_Rule;
		nonDelayRules[8] = EC2014_Tree_Base_Norm_ND_Rule;
		nonDelayRules[9] = EC2014_Tree_Ext_Norm_ND_Rule;
		addNonDelayRules(ffe, nonDelayRules);

		ExcelSaver saver = new ExcelSaver();
		ConsolePrinter printer = new ConsolePrinter();
		ffe.addNotifierListener(printer);
		ffe.addNotifierListener(saver);

		ffe.runExperiment();
		ffe.printResults();

	}

	public static class opProcPair implements ComplexFactorSetter {

		int min;
		int max;

		public opProcPair(int min, int max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public void configureExperiment(Experiment e) {
			MultipleReplicationExperiment mre = (MultipleReplicationExperiment) e;
			DynamicShopExperiment dse = (DynamicShopExperiment) mre.getBaseExperiment();
			dse.setOpProcTime(min, max);
		}

	}

	public static void addNonDelayRules(FullFactorialExperiment ffe, PR[] nonDelayRules) {
		PR dummy;
		for (int i = 0; i < nonDelayRules.length; i++) {
			nonDelayRules[i].setTieBreaker(new TieBreakerFASFS());
			dummy = new IgnoreFutureJobs(nonDelayRules[i]);
			ffe.addFactor("baseExperiment.sequencingRule", dummy);
		}
	}

	static PR ASP2013_Rule_6 = new PR() {

		protected double APR;
		
		@Override
		public void beforeCalc(PriorityQueue<?> q) {
			int numJobs = 0;
			APR = 0.0d;

			for (int i = 0; i < q.size(); i++) {
				PrioRuleTarget j = q.get(i);

				if (arrivesTooLate(j))
					continue;

				APR += j.getCurrentOperation().procTime;
				numJobs++;
			}
			APR = (APR / numJobs);

			super.beforeCalc(q);
		}

        @Override

        public double calcPrio(PrioRuleTarget j) {

            double RJ = j.getArriveTime();

        	double PR = j.getCurrentOperation().procTime;

        	double RT = j.remainingProcTime();

        	double SJ = j.getDueDate() - j.remainingProcTime() - j.getShop().simTime();

            double WINQ = jasima.shopSim.prioRules.upDownStream.WINQ.winq(j);

            return Math.abs(pd(pd(pd(pd(RJ, SJ),PR),PR),Math.max(APR, WINQ))) * Math.abs(pd((pd((pd(SJ,APR)-SJ), Math.min(RT, SJ)) * Math.min(RT, SJ))
            		,Math.min((pd(RJ, SJ) * pd(RJ, SJ)), RT)));
        }

        public double pd(double a, double b) {

            if (b==0)
            	return 1;

            else
            	return a/b;

        }

    	@Override
    	public String getName() {
    		return "ASP2013_Rule_6";
    	}

	};

	static PR EC2014_Lin_Base_Rule = new PR() {

        @Override

        public double calcPrio(PrioRuleTarget j) {

               double pt = j.getCurrentOperation().procTime;

               double winq = jasima.shopSim.prioRules.upDownStream.WINQ.winq(j);

               double npt = PTPlusWINQPlusNPT.npt(j);



               return -2.0*pt -0.230834684*winq -0.675256546*npt;

        }

    	@Override
    	public String getName() {
    		return "EC2014_Lin_Base_Rule";
    	}

	};

	static PR EC2014_Tree_Base_Norm_ND_Rule = new GPRuleBase() {

        @Override

        public double calcPrio(PrioRuleTarget j) {

               double ptNorm = norm(j.getCurrentOperation().procTime, 1.0,

                            47.0);

               double winqNorm = norm(

                            jasima.shopSim.prioRules.upDownStream.WINQ.winq(j),

                            0.0, 410.0);

               double nptNorm = norm(PTPlusWINQPlusNPT.npt(j), 0.0, 47.0);



               return add(mul(sub(div(1, 1), mul(winqNorm, nptNorm)), ifte(div(max(nptNorm, 1), div(ifte(ptNorm, ptNorm, nptNorm), mul(ptNorm, winqNorm))), mul(max(ifte(winqNorm, ptNorm, ptNorm), sub(div(1, 1), max(nptNorm, ifte(winqNorm, ptNorm, ptNorm)))), nptNorm), 1)), sub(sub(add(mul(sub(ifte(0, 1, nptNorm), mul(winqNorm, nptNorm)), ifte(ifte(max(ptNorm, 1), ptNorm, mul(1, winqNorm)), mul(1, nptNorm), sub(ptNorm, nptNorm))),  sub(sub(div(1, 1), mul(winqNorm, nptNorm)), max(sub(1, add(div(1, 1), div(max(winqNorm, sub(div(1, 1), max(mul(div(1, ifte(0, winqNorm, 0)), add(ptNorm, 0)), 1))), mul(mul(ptNorm, 0), winqNorm)))), add(ptNorm, ptNorm)))),  max(add(1, 1), max(nptNorm, 0))), max(sub(max(add(1, 1), max(nptNorm, 0)), add(mul(div(sub(sub(winqNorm, ptNorm), mul(ptNorm, 0)), mul(div(mul(1, 1), ifte(0, winqNorm, 0)), add(ptNorm, 0))), ifte(mul(max(ifte(winqNorm, ptNorm, ptNorm), div(ptNorm, 1)), nptNorm), mul(max(ifte(winqNorm, ptNorm, ptNorm), div(ptNorm, 1)), nptNorm), sub(ptNorm, nptNorm))), sub(sub(sub(div(0, nptNorm), div(ptNorm, 1)), max(div(ifte(1, nptNorm, add(1, sub(nptNorm, winqNorm))), add(max(winqNorm, max(winqNorm, 0)), sub(max(1, ptNorm), max(winqNorm, 0)))), sub(ptNorm, ptNorm))), max(ifte(1, ptNorm, ptNorm), ptNorm)))), div(0, nptNorm))));                  

        }

        private double norm(double v, double min, double max) {

        	   double x = (v - min) / (max - min);
               return x * 2.0 + 1e-6;

        }

    	@Override
    	public String getName() {
    		return "EC2014_Tree_Base_Norm_ND_Rule";
    	}

	};

	static PR EC2014_Tree_Ext_Norm_ND_Rule = new GPRuleBase() {

        @Override

        public double calcPrio(PrioRuleTarget j) {

               double ptNorm = norm(j.getCurrentOperation().procTime, 1.0,

                            47.0);

               double winqNorm = norm(

                            jasima.shopSim.prioRules.upDownStream.WINQ.winq(j),

                            0.0, 410.0);

               double nptNorm = norm(PTPlusWINQPlusNPT.npt(j), 0.0, 47.0);

               double tisNorm = norm(j.getShop().simTime() - j.getRelDate(),

                            0.0, 2770.0);

               double rptNorm = norm(j.remainingProcTime(), 0.0, 264.0);

               double qtNorm = norm(j.getShop().simTime() - j.getArriveTime(),

                            0.0, 1500.0);

               double rnopsNorm = norm(j.numOpsLeft(), 1.0, 10.0);

               

               return sub(sub(sub(div(sub(add(add(sub(add(div(rnopsNorm, ptNorm), sub(sub(add(ifte(winqNorm, nptNorm, qtNorm), mul(mul(winqNorm, nptNorm), ifte(winqNorm, sub(0, sub(ptNorm, tisNorm)), qtNorm))), mul(mul(mul(rnopsNorm, nptNorm), winqNorm), nptNorm)), mul(winqNorm, nptNorm))), ptNorm), mul(winqNorm, winqNorm)), ifte(sub(ptNorm, add(mul(sub(sub(ifte(add(qtNorm, 0), 0, ifte(winqNorm, mul(qtNorm, 1), rnopsNorm)), add(ptNorm, nptNorm)), mul(rnopsNorm, nptNorm)), sub(sub(add(ifte(winqNorm, nptNorm, qtNorm), mul(nptNorm, sub(0, 0))), add(sub(1, ptNorm), add(sub(0, sub(ptNorm, tisNorm)), add(sub(ptNorm, tisNorm), winqNorm)))), mul(winqNorm, nptNorm))), ifte(winqNorm, sub(add(mul(sub(sub(winqNorm, add(ptNorm, nptNorm)), ptNorm), mul(winqNorm, ptNorm)), mul(rnopsNorm, nptNorm)), 0), rptNorm))), add(sub(mul(rnopsNorm, nptNorm), ptNorm), div(qtNorm, max(qtNorm, max(winqNorm, winqNorm)))), rnopsNorm)), mul(mul(rnopsNorm, nptNorm), mul(winqNorm, ptNorm))), add(ptNorm, winqNorm)), ptNorm), 1), mul(winqNorm, nptNorm));

        }

        private double norm(double v, double min, double max) {

        	   double x = (v - min) / (max - min);
     	   	   return x * 2.0 + 1e-6;

        }

    	@Override
    	public String getName() {
    		return "EC2014_Tree_Ext_Norm_ND_Rule";
    	}

	};

}
