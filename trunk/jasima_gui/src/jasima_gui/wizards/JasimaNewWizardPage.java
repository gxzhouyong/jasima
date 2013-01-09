package jasima_gui.wizards;

import jasima_gui.ProjectCache;

import java.lang.reflect.Modifier;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (jasima).
 */

public class JasimaNewWizardPage extends WizardPage {
	private Text containerText;

	private Text fileText;

	private Text typeText;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public JasimaNewWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("New Jasima experiment");
		setDescription("Create a new Jasima experiment in XML format");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText("&Container:");
		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&File name:");
		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		new Label(container, SWT.NULL); // empty cell

		label = new Label(container, SWT.NULL);
		label.setText("&Experiment class:");
		typeText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		typeText.setText("jasima.shopSim.models.holthaus.HolthausExperiment");
		typeText.setLayoutData(gd);
		typeText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectType();
			}
		});

		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IJavaElement) {
				obj = ((IJavaElement) obj).getResource();
			}
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
			}
		}
		fileText.setText("new_experiment.jasima");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	private void selectType() {
		try {
			IResource container = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(new Path(getContainerName()));

			IJavaProject proj = JavaCore.create(container.getProject());
			SelectionDialog dlg = JavaUI.createTypeDialog(getShell(), null,
					SearchEngine.createStrictHierarchyScope(proj,
							proj.findType("jasima.core.experiment.Experiment"),
							true, true, null),
					IJavaElementSearchConstants.CONSIDER_CLASSES, false, "?");
			if (dlg.open() != SelectionDialog.OK) {
				return;
			}
			IType type = (IType) dlg.getResult()[0];
			typeText.setText(type.getFullyQualifiedName());
		} catch (JavaModelException e) {
			// ignore
		}
	}

	/**
	 * Ensures that all dialog fields are set correctly
	 */
	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		try {
			if (!container.getProject().hasNature(JavaCore.NATURE_ID)) {
				updateStatus("Project must be a Java project");
				return;
			}
		} catch (CoreException e) {
			updateStatus(e.getLocalizedMessage());
			return;
		}

		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc < 0
				|| !fileName.substring(dotLoc + 1).equalsIgnoreCase("jasima")) {
			updateStatus("File extension must be \"jasima\"");
			return;
		}
		if (((IContainer) container).getFile(new Path(fileName)).exists()) {
			updateStatus("File must not exist");
			return;
		}

		ClassLoader ldr = ProjectCache.getCache(container.getProject())
				.getClassLoader();
		Class<?> experiment;
		try {
			experiment = ldr.loadClass("jasima.core.experiment.Experiment");
		} catch (ClassNotFoundException e) {
			updateStatus("Target project's class path must contain Jasima");
			return;
		}
		try {
			Class<?> klass = ldr.loadClass(getTypeName());
			if (Modifier.isAbstract(klass.getModifiers())) {
				updateStatus("Experiment must not be abstract");
				return;
			}
			if (!experiment.isAssignableFrom(klass)) {
				updateStatus("Selected class must inherit Experiment");
				return;
			}
			klass.newInstance();
		} catch (ClassNotFoundException e) {
			updateStatus("The selected class must exist");
			return;
		} catch (IllegalAccessException e) {
			updateStatus("Experiment's constructor must be accessable");
			return;
		} catch (InstantiationException e) {
			updateStatus("Experiment must have a default constructor");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}

	public String getTypeName() {
		return typeText.getText();
	}
}