package perfStudy;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.core.PriorityQueue;

@SuppressWarnings("serial")
public class Rule_ASP2013_Rule_6 extends PR {
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
		double SJ = j.getDueDate() - j.remainingProcTime()
				- j.getShop().simTime();
		double WINQ = jasima.shopSim.prioRules.upDownStream.WINQ.winq(j);

		return Math.abs(pd(pd(pd(pd(RJ, SJ), PR), PR), Math.max(APR, WINQ)))
				* Math.abs(pd((pd((pd(SJ, APR) - SJ), Math.min(RT, SJ)) * Math
						.min(RT, SJ)), Math.min((pd(RJ, SJ) * pd(RJ, SJ)), RT)));
	}

	/**
	 * Protected division.
	 * 
	 */
	public static final double pd(double a, double b) {
		if (b == 0)
			return 1;
		else
			return a / b;
	}

	@Override
	public String getName() {
		return "ASP2013_Rule_6";
	}
}