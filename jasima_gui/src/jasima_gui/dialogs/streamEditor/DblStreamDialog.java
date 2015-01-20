package jasima_gui.dialogs.streamEditor;

import jasima_gui.editor.TopLevelEditor;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

public class DblStreamDialog extends FormDialog {

	private MasterBlock mdb;
	private TopLevelEditor editor;

	public DblStreamDialog(Shell shell, TopLevelEditor editor) {
		super(shell);
		setHelpAvailable(false);
		this.editor = editor;

	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		super.createFormContent(mform);

		mdb = new MasterBlock();
		mdb.createContent(mform);
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control c = super.createButtonBar(parent);
		mdb.okButton = getButton(IDialogConstants.OK_ID);
		return c;
	}

	@Override
	public boolean close() {
		mdb.getDetailsPart().selectionChanged(null, null);
		return super.close();
	}

	public void initContents(Object[] streamDefs, DetailsPageBase[] pages) {
		mdb.initContents(streamDefs, pages);
	}

	public Object getStreamDef() {
		return mdb.getStreamDef();
	}

	public void setStreamDef(Object streamDef) {
		mdb.setStreamDef(streamDef);
	}
}
