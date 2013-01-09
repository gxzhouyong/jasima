package jasima_gui.launcher;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

public class SimulationLauncher extends JavaLaunchDelegate {
	private static void addDebugEventListener(final ILaunchConfiguration conf) {
		final DebugPlugin debugPlugin = DebugPlugin.getDefault();
		debugPlugin.addDebugEventListener(new IDebugEventSetListener() {
			@Override
			public void handleDebugEvents(DebugEvent[] events) {
				for (DebugEvent evt : events) {
					if (evt.getKind() == DebugEvent.TERMINATE) {
						Object source = evt.getSource();
						if (source instanceof IProcess) {
							handleProcessTerminate((IProcess) source);
						}
					}
				}
			}

			private void handleProcessTerminate(IProcess proc) {
				if (!proc.getLaunch().getLaunchConfiguration().equals(conf))
					return;
				debugPlugin.removeDebugEventListener(this);
				try {
					String project = conf.getAttribute(ATTR_PROJECT_NAME,
							(String) null);
					if (project == null)
						return;
					ResourcesPlugin.getWorkspace().getRoot()
							.getProject(project)
							.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					// ignore
				}
			}
		});
	}

	@Override
	public IVMRunner getVMRunner(final ILaunchConfiguration launchCfg,
			String mode) throws CoreException {
		// FIXME this might leave garbage if the launch is aborted
		addDebugEventListener(launchCfg);
		return super.getVMRunner(launchCfg, mode);
	}
}
