package jasima_gui.launcher;

import org.eclipse.debug.core.ILaunchConfigurationType;

public class ExcelLaunchShortcut extends SimulationLaunchShortcut {

	@Override
	protected String getDefaultArgs() {
		return super.getDefaultArgs() + " --xlsres";
	}

	@Override
	protected String getLauncherClass() {
		return "jasima.core.util.run.ExcelExperimentRunner";
	}

	protected ILaunchConfigurationType getConfigurationType() {
		return getLaunchManager().getLaunchConfigurationType(
				"jasima_gui.jasimaLaunchConfigurationType");
	}
}
