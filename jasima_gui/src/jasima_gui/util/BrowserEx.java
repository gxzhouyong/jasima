/*******************************************************************************
 * Copyright (c) 2010-2015 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.2.
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
 *******************************************************************************/
package jasima_gui.util;

import java.util.ArrayList;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class BrowserEx extends Browser implements ProgressListener {

	public static final int DEFAULT_MAXIMUM_HEIGHT = 200;

	protected ArrayList<Listener> sizeListeners = new ArrayList<>();
	protected int maximumHeight = DEFAULT_MAXIMUM_HEIGHT;

	public BrowserEx(Composite parent, int style) {
		super(parent, style);
		addProgressListener(this);
	}

	public void addSizeListener(Listener listener) {
		sizeListeners.add(listener);
	}

	public void removeSizeListener(Listener listener) {
		sizeListeners.remove(listener);
	}

	@Override
	protected void checkSubclass() {
		// we *are* subclassing Browser
	}

	@Override
	public Point computeSize(int wHint, int hHint) {
		// ignore hHint
		Point oldSize = getSize();
		if (wHint <= 0)
			wHint = 100;
		setSize(wHint, oldSize.y);
		final String getHeight = "var contentElement = document.getElementById('jasima-content');" //
				+ "if(!contentElement) return 0;" //
				+ "return contentElement.scrollHeight";
		double height = (Double) evaluate(getHeight);
		setSize(oldSize);
		return new Point(wHint, Math.min(maximumHeight, (int) Math.ceil(height)));
	}

	@Override
	public void changed(ProgressEvent event) {
	}

	@Override
	public void completed(ProgressEvent event) {
		for (Listener lstnr : sizeListeners) {
			lstnr.handleEvent(new Event());
		}
	}

}
