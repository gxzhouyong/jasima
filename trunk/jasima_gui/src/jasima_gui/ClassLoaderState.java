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
