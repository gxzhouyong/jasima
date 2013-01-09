package jasima_gui.launcher;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

public class SimulationLaunchShortcut implements ILaunchShortcut2 {

	public void launch(IResource resource, String mode) {
		if (resource == null)
			return;
		try {
			String pathArg = resource.getProjectRelativePath().toString();
			if (pathArg.contains("\"") || pathArg.contains("\\")) {
				Status status = new Status(Status.ERROR, "jasima_gui",
						"The file name must not contain backslashes or quotation marks.");
				ErrorDialog.openError(PlatformUI.getWorkbench()
						.getModalDialogShellProvider().getShell(), "Error",
						"Can't create launch configuration.", status);
				return;
			}
			ILaunchConfigurationType configType = getConfigurationType();

			ILaunchConfiguration[] configs = DebugPlugin.getDefault()
					.getLaunchManager()
					.getLaunchConfigurations(getConfigurationType());
			for (ILaunchConfiguration config : configs) {
				if (config.getAttribute(ATTR_PROGRAM_ARGUMENTS, "").contains(
						pathArg)) {
					DebugUITools.launch(config, mode);
					return;
				}
			}

			String baseName = resource.getProjectRelativePath()
					.removeFileExtension().toString();
			ILaunchConfigurationWorkingCopy wc = configType.newInstance(null,
					getLaunchManager()
							.generateLaunchConfigurationName(baseName));
			wc.setAttribute(ATTR_MAIN_TYPE_NAME, getLauncherClass());
			wc.setAttribute(ATTR_PROJECT_NAME, resource.getProject().getName());
			wc.setAttribute(ATTR_PROGRAM_ARGUMENTS, getDefaultArgs() + " \""
					+ pathArg + "\"");
			// wc.setMappedResources(new IResource[] { resource });
			// can't keep it up to date anyway...
			ILaunchConfiguration config = wc.doSave();
			DebugUITools.launch(config, mode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String getDefaultArgs() {
		return "--printres";
	}

	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

	protected ILaunchConfigurationType getConfigurationType() {
		return getLaunchManager().getLaunchConfigurationType(
				"jasima_gui.jasimaLaunchConfigurationType");
	}

	protected String getLauncherClass() {
		return "jasima.core.util.run.XmlExperimentRunner";
	}

	@Override
	public IResource getLaunchableResource(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 1) {
				Object element = ss.getFirstElement();
				if (element instanceof IAdaptable) {
					return (IResource) ((IAdaptable) element)
							.getAdapter(IResource.class);
				}
			}
		}
		return null;
	}

	@Override
	public IResource getLaunchableResource(IEditorPart editorpart) {
		IEditorInput inp = editorpart.getEditorInput();
		if (!(inp instanceof IFileEditorInput)) {
			return null;
		}
		return ((IFileEditorInput) inp).getFile();
	}

	@Override
	public void launch(ISelection selection, String mode) {
		launch(getLaunchableResource(selection), mode);
	}

	@Override
	public void launch(IEditorPart editorpart, String mode) {
		launch(getLaunchableResource(editorpart), mode);
	}

	@Override
	public ILaunchConfiguration[] getLaunchConfigurations(ISelection selection) {
		// let the framework resolve configurations based on resource mapping
		return null;
	}

	@Override
	public ILaunchConfiguration[] getLaunchConfigurations(IEditorPart editorpart) {
		// let the framework resolve configurations based on resource mapping
		return null;
	}

}
