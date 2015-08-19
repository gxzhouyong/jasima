package example;

import jasima.shopSim.core.Operation;

public class MiniFabOperation extends Operation {
	
	public double loadTime = 0.0;
	public double mainProcTime = Double.NaN;
	public double unloadTime = 0.0;

	public MiniFabOperation() {
		super();
	}

}
