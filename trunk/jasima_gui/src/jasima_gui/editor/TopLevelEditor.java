/*******************************************************************************
 * Copyright (c) 2010, 2014 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.1.
 *
 * jasima is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jasima is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jasima.  If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id$
 *******************************************************************************/
package jasima_gui.editor;

import jasima_gui.JavaLinkHandler;
import jasima_gui.ProjectCache;
import jasima_gui.PropertyToolTip;
import jasima_gui.util.XMLUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.text.javadoc.JavadocContentAccess2;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.thoughtworks.xstream.XStream;

@SuppressWarnings("restriction")
public class TopLevelEditor extends EditorPart implements SelectionListener {

	protected static final String CLASS_URL_PREFIX = "jasima-javaclass:";
	protected static final String DUMMY_URL = "about:invalid";
	protected static final String HREF_MORE = "jasima-command:more";
	protected static final String HREF_LESS = "jasima-command:less";
	protected static final int MAX_DESCRIPTION_HEIGHT = 200;
	private EditorUpdater updater;
	private Object root;
	private FormToolkit toolkit = null;
	private ScrolledForm form;
	private boolean dirty = false;
	private IProject project;
	private XStream xStream;
	private ClassLoader classLoader;

	public TopLevelEditor() {
		updater = new EditorUpdater(this);
	}

	public IJavaProject getJavaProject() {
		return ProjectCache.getCache(project).getJavaProject();
	}

	public XStream getXStream() {
		return xStream;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public FormToolkit getToolkit() {
		return toolkit;
	}

	@Override
	public void dispose() {
		updater.dispose();
		if (toolkit != null) {
			toolkit.dispose();
		}
		super.dispose();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		try {
			IFileEditorInput fei = (IFileEditorInput) input;
			setFileInput(fei);
			setSite(site);
			fei.getFile().refreshLocal(0, null);
			InputStream is = fei.getStorage().getContents();
			try {
				xStream = ProjectCache.getCache(fei.getFile().getProject())
						.getXStream();
				classLoader = xStream.getClassLoader();
				root = xStream.fromXML(is);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			root = e;
		}
	}

	/**
	 * Sets the input to this editor.
	 * 
	 * If the new input is contained in a different project than the old one,
	 * the editor will not update and check the class path. In that case, the
	 * editor should only be used to save the file and then be closed and
	 * re-opened.
	 * 
	 * @param input
	 *            the editor input
	 */
	protected void setFileInput(IFileEditorInput input) {
		project = input.getFile().getProject();
		super.setInput(input);
		updateHeadline();
	}

	protected boolean isValidData() {
		return !(root instanceof Exception);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return isValidData();
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void updateHeadline() {
		if (form == null)
			return;

		setPartName(getEditorInput().getName());

		Link headline = new Link(form.getForm().getHead(), 0);
		headline.setForeground(form.getForeground());
		headline.setBackground(null);
		headline.setFont(form.getFont());
		if (isValidData()) {
			headline.setText(String.format("%s - <a href=\"%s%s\">%s</a>",
					getEditorInput().getName(), CLASS_URL_PREFIX, root
							.getClass().getCanonicalName(), root.getClass()
							.getSimpleName()));
		} else {
			headline.setText(getEditorInput().getName());
		}
		headline.addSelectionListener(this);

		Control head = form.getForm().getHeadClient();
		if (head != null)
			head.dispose();
		form.setHeadClient(headline);
		toolkit.decorateFormHeading(form.getForm());
	}

	protected static String buildDocument(String javadoc) {
		StringBuilder htmlDoc = new StringBuilder();
		htmlDoc.append("<!DOCTYPE html><html><head>" //
				+ "<title>Tooltip</title><style type='text/css'>");
		htmlDoc.append(PropertyToolTip.getJavadocStylesheet());
		htmlDoc.append("html {padding: 0px; margin: 0px} " //
				+ "body {padding: 0px; margin: 0px} " //
				+ "dl {margin: 0px} " //
				+ "dt {margin-top: 0.5em}");
		htmlDoc.append("</style></head><body><div id=\"jasima-content\">");
		htmlDoc.append(javadoc);
		htmlDoc.append("</div></body></html>");
		return htmlDoc.toString();
	}

	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createScrolledForm(parent);
		form.setExpandHorizontal(true);
		updateHeadline();

		if (!isValidData()) {
			GridLayout grid = new GridLayout(2, false);
			grid.marginTop = 10;
			form.getBody().setLayout(grid);
			String msg = String.format("Error reading input: %s: %s", root
					.getClass().getSimpleName(),
					String.valueOf(((Exception) root).getLocalizedMessage())
							.replaceFirst("^ *: *", ""));
			Label icon = toolkit.createLabel(form.getBody(), null);
			icon.setImage(form.getDisplay().getSystemImage(SWT.ERROR));
			toolkit.createLabel(form.getBody(), msg, SWT.WRAP);
			return;
		}

		Layout layout = new Layout() {
			static final int SPACING = 10;
			static final int VMARGIN = 10;
			static final int HMARGIN = 5;

			@Override
			protected Point computeSize(Composite composite, int wHint,
					int hHint, boolean flushCache) {
				Point retVal = new Point(composite.getSize().x, 0);
				if (retVal.x == 0) {
					retVal.x = -1;
				} else {
					retVal.x -= 2 * HMARGIN;
				}
				retVal.y += VMARGIN;
				for (Control c : composite.getChildren()) {
					retVal.y += SPACING;
					retVal.y += determineSize(c, SWT.DEFAULT).y;
				}
				retVal.y += VMARGIN - SPACING;
				retVal.x = 0;
				return retVal;
			}

			@Override
			protected void layout(Composite composite, boolean flushCache) {
				int w = composite.getSize().x - 2 * HMARGIN;
				int posY = VMARGIN;
				for (Control c : composite.getChildren()) {
					Point size = determineSize(c, w);
					c.setSize(size);
					c.setLocation(HMARGIN, posY);
					posY += size.y;
					posY += SPACING;
				}
			}

			protected Point determineSize(Control c, int width) {
				Object ld = c.getLayoutData();
				if (ld instanceof Point) {
					Point p = (Point) ld;
					int wHint = (p.x == SWT.DEFAULT) ? width : p.x;
					return c.computeSize(wHint, p.y);
				}
				return c.computeSize(width, SWT.DEFAULT);
			}
		};
		form.getBody().setLayout(layout);

		createJavaDocDescription();

		createMainEditor();
	}

	protected void createMainEditor() {
		IProperty topLevelProperty = new IProperty() {
			public void setValue(Object val) throws PropertyException {
				root = val;
				makeDirty();
			}

			public boolean isWritable() {
				return true;
			}

			public boolean isImportant() {
				return true;
			}

			public Object getValue() throws PropertyException {
				return root;
			}

			public Class<?> getType() {
				return root.getClass();
			}

			public String getName() {
				return getEditorInput().getName();
			}

			@Override
			public String getHTMLDescription() {
				return "";
			}

			@Override
			public boolean canBeNull() {
				return false;
			}
		};

		EditorWidget editor = EditorWidgetFactory.getInstance()
				.createEditorWidget(this, form.getBody(), topLevelProperty,
						null);
		editor.loadValue();
	}

	protected void createJavaDocDescription() {
		// get JavaDoc as HTML (content only)
		String doc;
		try {
			IType type = getJavaProject().findType(
					root.getClass().getCanonicalName());
			doc = JavadocContentAccess2.getHTMLContent(type, true);
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}

		if (doc == null || doc.trim().length() == 0)
			return;

		String summary = createSummary(doc);
		if (summary == null) {
			summary = doc;
		} else {
			summary += String.format(" <a href=\"%s\">%s</a>", HREF_MORE,
					"more");
			doc += String.format("<br><a href=\"%s\">%s</a>", HREF_LESS,
					"hide detailed description");
		}

		final String summaryDoc = buildDocument(summary);
		final String mainDoc = buildDocument(doc);

		final Point browserLayoutData = new Point(SWT.DEFAULT,
				MAX_DESCRIPTION_HEIGHT);
		final Browser browser = new Browser(form.getBody(), SWT.NONE);
		browser.setLayoutData(browserLayoutData);
		browser.setText(summaryDoc, false);

		final String getHeight = browser.getBrowserType().equals("webkit") //
				? "return document.documentElement.scrollHeight" //
				: "return document.getElementById(\"jasima-content\").scrollHeight + 1";

		// automatically resize the Browser to match its contents
		browser.addProgressListener(new ProgressListener() {
			@Override
			public void completed(ProgressEvent event) {
				Double height = (Double) browser.evaluate(getHeight);
				if (height == null)
					return;
				browserLayoutData.y = Math.min(MAX_DESCRIPTION_HEIGHT, ((int) Math.ceil(height)));
				form.getBody().layout(true, true);
				form.reflow(true);
			}

			@Override
			public void changed(ProgressEvent event) {
				// ignore
			}
		});

		final LocationListener linkHandler = JavaElementLinks
				.createLocationListener(new JavaLinkHandler());

		browser.addLocationListener(new LocationListener() {
			@Override
			public void changing(LocationEvent event) {
				if (event.location.equals(DUMMY_URL))
					return;

				// when calling setText, first navigate to DUMMY_URL,
				// hopefully firing ProgressListener.completed on MSIE

				if (event.location.equals(HREF_LESS)) {
					browser.setUrl(DUMMY_URL);
					browser.setText(summaryDoc, false);
					event.doit = false;
				} else if (event.location.equals(HREF_MORE)) {
					browser.setUrl(DUMMY_URL);
					browser.setText(mainDoc, false);
					event.doit = false;
				} else {
					linkHandler.changing(event);
				}
			}

			public void changed(LocationEvent event) {
				linkHandler.changed(event);
			}
		});
	}

	/**
	 * Find first sentence of "doc".
	 */
	private String createSummary(String doc) {
		int summaryEnd = doc.indexOf(". ") + 1;
		if (summaryEnd == 0) {
			summaryEnd = doc.indexOf(".\n") + 1;
		}
		if (summaryEnd == 0) {
			summaryEnd = doc.indexOf("<dl>");
		}

		if (summaryEnd != -1) {
			String summary = doc.substring(0, summaryEnd).trim();
			if (summary.isEmpty()) {
				summary = "<span style=\"color:#888\">No Javadoc summary.</span>";
			}
			return summary;
		} else
			return null;
	}

	@Override
	public void setFocus() {
		form.setFocus();
	}

	protected void doSaveReally() throws CoreException {
		assert isValidData();
		byte[] byteArr = XMLUtil.serialize(ProjectCache.getCache(project)
				.getXStream(), root);
		IFileEditorInput fei = (IFileEditorInput) getEditorInput();
		if (fei.getFile().exists()) {
			fei.getFile().setContents(new ByteArrayInputStream(byteArr), false,
					true, null);
		} else {
			fei.getFile()
					.create(new ByteArrayInputStream(byteArr), false, null);
		}
		dirty = false;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			doSaveReally();
		} catch (CoreException e) {
			ErrorDialog.openError(PlatformUI.getWorkbench()
					.getModalDialogShellProvider().getShell(),
					"Couldn't save file", null, e.getStatus());
		}
	}

	@Override
	public void doSaveAs() {
		// TODO compare to AbstractDecoratedTextEditor.performSaveAs
		SaveAsDialog dlg = new SaveAsDialog(PlatformUI.getWorkbench()
				.getModalDialogShellProvider().getShell());
		IFileEditorInput oldInput = (IFileEditorInput) getEditorInput();
		dlg.setOriginalFile(oldInput.getFile());
		dlg.create();
		if (dlg.open() == SaveAsDialog.CANCEL)
			return;
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(dlg.getResult());
		FileEditorInput input = new FileEditorInput(file);
		setInput(input);
		try {
			doSaveReally();
			firePropertyChange(PROP_INPUT);
			updateHeadline();
		} catch (CoreException e) {
			setInput(oldInput);
			ErrorDialog.openError(PlatformUI.getWorkbench()
					.getModalDialogShellProvider().getShell(),
					"Couldn't save file", null, e.getStatus());
		}
	}

	public void makeDirty() {
		if (dirty)
			return;
		dirty = true;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// ignore
	}

	@Override
	public void widgetSelected(SelectionEvent evt) {
		try {
			String href = evt.text;
			if (href.startsWith(CLASS_URL_PREFIX)) {
				IJavaElement elem = getJavaProject().findType(
						href.substring(CLASS_URL_PREFIX.length()));
				JavaLinkHandler.openJavadoc(elem);
			}
		} catch (Exception e) {
			// ignore
		}
	}
}
