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
package jasima_gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IPath;

public class ClassLoaderState {
	protected ArrayList<ClassLoaderStateListener> listeners = new ArrayList<>();
	protected Set<String> dirtyClasses = new HashSet<>();

	public void addListener(ClassLoaderStateListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ClassLoaderStateListener listener) {
		listeners.remove(listener);
	}

	public boolean isDirty() {
		return !dirtyClasses.isEmpty();
	}

	public void markDirty(String klass, boolean dirty) {
		boolean wasDirty = isDirty();

		if (dirty) {
			dirtyClasses.add(klass);
		} else {
			dirtyClasses.remove(klass);
		}

		boolean isDirty = isDirty();

		if (isDirty != wasDirty) {
			for (ClassLoaderStateListener listener : listeners) {
				listener.dirtyChanged(isDirty);
			}
		}
	}
}
