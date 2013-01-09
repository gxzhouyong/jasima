package jasima_gui;

import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandImageService;
import org.eclipse.ui.commands.ICommandService;

public class JasimaAction extends Action {

	public JasimaAction(String id) {
		try {
			if (id.startsWith("...")) {
				id = "jasima_gui.commands." + id.substring(3);
			}
			setText(((ICommandService) PlatformUI.getWorkbench().getService(
					ICommandService.class)).getCommand(id).getName());
			setImageDescriptor(((ICommandImageService) PlatformUI
					.getWorkbench().getService(ICommandImageService.class))
					.getImageDescriptor(id));
		} catch (NotDefinedException e) {
			throw new RuntimeException(e);
		}
	}

}
