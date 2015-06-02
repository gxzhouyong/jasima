package perfStudy;
import jasima.shopSim.core.PR;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.prioRules.upDownStream.PTPlusWINQPlusNPT;
import jasima.shopSim.prioRules.upDownStream.WINQ;

@SuppressWarnings("serial")
public class Rule_EC2014_Lin_Base extends PR {
	@Override
	public double calcPrio(PrioRuleTarget j) {
		double pt = j.getCurrentOperation().procTime;
		double winq = WINQ.winq(j);
		double npt = PTPlusWINQPlusNPT.npt(j);

		return -2.0 * pt - 0.230834684 * winq - 0.675256546 * npt;
	}

	@Override
	public String getName() {
		return "EC2014_Lin_Base_Rule";
	}
}