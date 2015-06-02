package perfStudy;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.prioRules.gp.GPRuleBase;
import jasima.shopSim.prioRules.upDownStream.PTPlusWINQPlusNPT;

@SuppressWarnings("serial")
public class Rule_EC2014_Tree_Base_Norm_ND extends GPRuleBase {
	@Override
	public double calcPrio(PrioRuleTarget j) {
		double ptNorm = norm(j.getCurrentOperation().procTime, 1.0, 47.0);
		double winqNorm = norm(
				jasima.shopSim.prioRules.upDownStream.WINQ.winq(j), 0.0, 410.0);
		double nptNorm = norm(PTPlusWINQPlusNPT.npt(j), 0.0, 47.0);

		return add(
				mul(sub(div(1, 1), mul(winqNorm, nptNorm)),
						ifte(div(
								max(nptNorm, 1),
								div(ifte(ptNorm, ptNorm, nptNorm),
										mul(ptNorm, winqNorm))),
								mul(max(ifte(winqNorm, ptNorm, ptNorm),
										sub(div(1, 1),
												max(nptNorm,
														ifte(winqNorm, ptNorm,
																ptNorm)))),
										nptNorm), 1)),
				sub(sub(add(
						mul(sub(ifte(0, 1, nptNorm), mul(winqNorm, nptNorm)),
								ifte(ifte(max(ptNorm, 1), ptNorm,
										mul(1, winqNorm)), mul(1, nptNorm),
										sub(ptNorm, nptNorm))),
						sub(sub(div(1, 1), mul(winqNorm, nptNorm)),
								max(sub(1,
										add(div(1, 1),
												div(max(winqNorm,
														sub(div(1, 1),
																max(mul(div(
																		1,
																		ifte(0,
																				winqNorm,
																				0)),
																		add(ptNorm,
																				0)),
																		1))),
														mul(mul(ptNorm, 0),
																winqNorm)))),
										add(ptNorm, ptNorm)))),
						max(add(1, 1), max(nptNorm, 0))),
						max(sub(max(add(1, 1), max(nptNorm, 0)),
								add(mul(div(
										sub(sub(winqNorm, ptNorm),
												mul(ptNorm, 0)),
										mul(div(mul(1, 1), ifte(0, winqNorm, 0)),
												add(ptNorm, 0))),
										ifte(mul(
												max(ifte(winqNorm, ptNorm,
														ptNorm), div(ptNorm, 1)),
												nptNorm),
												mul(max(ifte(winqNorm, ptNorm,
														ptNorm), div(ptNorm, 1)),
														nptNorm),
												sub(ptNorm, nptNorm))),
										sub(sub(sub(div(0, nptNorm),
												div(ptNorm, 1)),
												max(div(ifte(
														1,
														nptNorm,
														add(1,
																sub(nptNorm,
																		winqNorm))),
														add(max(winqNorm,
																max(winqNorm, 0)),
																sub(max(1,
																		ptNorm),
																		max(winqNorm,
																				0)))),
														sub(ptNorm, ptNorm))),
												max(ifte(1, ptNorm, ptNorm),
														ptNorm)))),
								div(0, nptNorm))));

	}

	private double norm(double v, double min, double max) {

		double x = (v - min) / (max - min);
		return x * 2.0 + 1e-6;

	}

	@Override
	public String getName() {
		return "EC2014_Tree_Base_Norm_ND_Rule";
	}
}