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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

public class EditorUpdater implements IResourceChangeListener, IResourceDeltaVisitor {

	protected final TopLevelEditor editor;

	public EditorUpdater(final TopLevelEditor editor) {
		this.editor = editor;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		try {
			delta.accept(this);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		// either deleted or moved or renamed
		if (delta.getKind() == IResourceDelta.REMOVED) {
			final IFileEditorInput inp = (IFileEditorInput) editor.getEditorInput();
			final IPath movedTo = delta.getMovedToPath();
			if (delta.getResource().equals(inp.getFile())) {
				editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						IWorkbenchPage page = editor.getSite().getPage();
						if (movedTo != null) {
							IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(movedTo);
							IFileEditorInput newInput = new FileEditorInput(newFile);
							editor.setFileInput(newInput);
							if (!newFile.getProject().equals(inp.getFile().getProject())) {
								if (!page.closeEditor(editor, true)) {
									page.closeEditor(editor, false); // FIXME
								}
								try {
									page.openEditor(newInput, "jasima_gui.editors.ObjectTreeEditor");
								} catch (PartInitException e) {
									e.printStackTrace();
								}
							}
						} else {
							page.closeEditor(editor, false);
						}
					}
				});
			}
		}

		return true;
	}
}
