package perfStudy;
import jasima.shopSim.core.PrioRuleTarget;
import jasima.shopSim.prioRules.gp.GPRuleBase;
import jasima.shopSim.prioRules.upDownStream.PTPlusWINQPlusNPT;

@SuppressWarnings("serial")
public class Rule_EC2014_Tree_Ext_Norm_ND extends GPRuleBase {
	@Override
	public double calcPrio(PrioRuleTarget j) {
		double ptNorm = norm(j.getCurrentOperation().procTime, 1.0, 47.0);
		double winqNorm = norm(
				jasima.shopSim.prioRules.upDownStream.WINQ.winq(j), 0.0, 410.0);
		double nptNorm = norm(PTPlusWINQPlusNPT.npt(j), 0.0, 47.0);
		double tisNorm = norm(j.getShop().simTime() - j.getRelDate(), 0.0,
				2770.0);
		double rptNorm = norm(j.remainingProcTime(), 0.0, 264.0);
		double qtNorm = norm(j.getShop().simTime() - j.getArriveTime(), 0.0,
				1500.0);
		double rnopsNorm = norm(j.numOpsLeft(), 1.0, 10.0);

		return sub(
				sub(sub(div(
						sub(add(add(
								sub(add(div(rnopsNorm, ptNorm),
										sub(sub(add(
												ifte(winqNorm, nptNorm, qtNorm),
												mul(mul(winqNorm, nptNorm),
														ifte(winqNorm,
																sub(0,
																		sub(ptNorm,
																				tisNorm)),
																qtNorm))),
												mul(mul(mul(rnopsNorm, nptNorm),
														winqNorm), nptNorm)),
												mul(winqNorm, nptNorm))),
										ptNorm), mul(winqNorm, winqNorm)),
								ifte(sub(
										ptNorm,
										add(mul(sub(
												sub(ifte(
														add(qtNorm, 0),
														0,
														ifte(winqNorm,
																mul(qtNorm, 1),
																rnopsNorm)),
														add(ptNorm, nptNorm)),
												mul(rnopsNorm, nptNorm)),
												sub(sub(add(
														ifte(winqNorm, nptNorm,
																qtNorm),
														mul(nptNorm, sub(0, 0))),
														add(sub(1, ptNorm),
																add(sub(0,
																		sub(ptNorm,
																				tisNorm)),
																		add(sub(ptNorm,
																				tisNorm),
																				winqNorm)))),
														mul(winqNorm, nptNorm))),
												ifte(winqNorm,
														sub(add(mul(
																sub(sub(winqNorm,
																		add(ptNorm,
																				nptNorm)),
																		ptNorm),
																mul(winqNorm,
																		ptNorm)),
																mul(rnopsNorm,
																		nptNorm)),
																0), rptNorm))),
										add(sub(mul(rnopsNorm, nptNorm), ptNorm),
												div(qtNorm,
														max(qtNorm,
																max(winqNorm,
																		winqNorm)))),
										rnopsNorm)),
								mul(mul(rnopsNorm, nptNorm),
										mul(winqNorm, ptNorm))),
						add(ptNorm, winqNorm)), ptNorm), 1),
				mul(winqNorm, nptNorm));

	}

	private double norm(double v, double min, double max) {

		double x = (v - min) / (max - min);
		return x * 2.0 + 1e-6;

	}

	@Override
	public String getName() {
		return "EC2014_Tree_Ext_Norm_ND_Rule";
	}
}