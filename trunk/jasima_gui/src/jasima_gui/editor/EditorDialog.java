package jasima_gui.editor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.thoughtworks.xstream.XStream;

public class EditorDialog extends Dialog {

	protected TopLevelEditor tle;
	protected IProperty property;
	protected Object oldValue;
	protected EditorWidget editor;

	public EditorDialog(TopLevelEditor tle, IProperty property) {
		this(tle, property, PlatformUI.getWorkbench()
				.getModalDialogShellProvider());
	}

	public EditorDialog(TopLevelEditor tle, IProperty property,
			IShellProvider shellProvider) {
		super(shellProvider);
		this.tle = tle;
		this.property = property;
	}

	@Override
	public int open() {
		try {
			oldValue = property.getValue();

			if (oldValue != null) {
				// we need a generic way to clone objects
				XStream xstr = tle.getXStream();
				oldValue = xstr.fromXML(xstr.toXML(oldValue));
			}
		} catch (PropertyException e) {
			e.printStackTrace();
			// TODO
		}

		int retVal = super.open();
		if (retVal != OK) {
			try {
				property.setValue(oldValue);
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		return retVal;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Edit " + property.getName());
	}

	@Override
	protected Control createContents(Composite parent) {
		Control retVal = super.createContents(parent);
		tle.getToolkit().adapt(retVal, false, false);
		return retVal;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control retVal = super.createButtonBar(parent);
		tle.getToolkit().adapt(retVal, false, false);
		return retVal;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite retVal = (Composite) super.createDialogArea(parent);
		tle.getToolkit().adapt(retVal, false, false);

		ScrolledForm form = tle.getToolkit().createScrolledForm(retVal);
		form.setExpandVertical(false);
		GridDataFactory.fillDefaults().grab(true, true).hint(600, 400)
				.applyTo(form);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		form.getBody().setLayout(gridLayout);

		editor = EditorWidgetFactory.getInstance().createEditorWidget(tle,
				form.getBody(), property, null);
		GridDataFactory.fillDefaults().grab(true, true).hint(0, SWT.DEFAULT)
				.applyTo(editor);
		editor.loadValue();

		return retVal;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Control toolBar = editor.getToolBar();
		if (toolBar != null) {
			((GridLayout) parent.getLayout()).numColumns++;
			GridDataFactory.swtDefaults().grab(true, false)
					.align(SWT.BEGINNING, SWT.CENTER).applyTo(toolBar);
			toolBar.setParent(parent);
		}
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
