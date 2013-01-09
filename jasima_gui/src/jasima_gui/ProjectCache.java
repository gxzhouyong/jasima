package jasima_gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ProjectCache {
	private static final Map<IProject, ProjectCache> cache = new WeakHashMap<IProject, ProjectCache>();

	public static ProjectCache getCache(IProject project) {
		ProjectCache retVal = cache.get(project);
		if (retVal == null) {
			cache.put(project, retVal = new ProjectCache(project));
		}
		return retVal;
	}

	private final IProject project;
	private IJavaProject javaProject = null;
	private ClassLoader classLoader = null;
	private XStream xStream = null;

	private ProjectCache(IProject project) {
		this.project = project;
	}

	public IJavaProject getJavaProject() {
		if (javaProject == null) {
			javaProject = JavaCore.create(project);
		}
		return javaProject;
	}

	protected static URL toAbsolutePath(IPath path)
			throws MalformedURLException {
		if (path == null)
			return null;
		if (!path.isAbsolute() || !path.toFile().exists()) {
			// path has to be resolved
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file == null)
				return null;
			path = file.getLocation();
		}
		if (path.toFile().isDirectory()) {
			// directories need trailing separators, otherwise URLClassLoader
			// will interpret the class path entry as a path to a JAR file
			path = path.addTrailingSeparator();
		}
		// URIUtil.toURI would remove the trailing separator!
		return path.toFile().toURI().toURL();
	}

	protected static void buildClassPath(Set<URL> cp, IJavaProject proj)
			throws JavaModelException, MalformedURLException {
		buildClassPath(cp, proj, false);
	}

	protected static void buildClassPath(Set<URL> cp, IJavaProject proj,
			boolean onlyExported) throws JavaModelException,
			MalformedURLException {
		IClasspathEntry[] classpath = proj.getResolvedClasspath(true);
		cp.add(toAbsolutePath(proj.getOutputLocation()));
		for (IClasspathEntry entry : classpath) {
			if (onlyExported && !entry.isExported())
				continue;
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				IPath path = entry.getOutputLocation();
				cp.add(toAbsolutePath(path));
			} else if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				// TODO should system libraries be excluded somehow?
				cp.add(toAbsolutePath(entry.getPath()));
			} else if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				IProject projEntry = (IProject) ResourcesPlugin.getWorkspace()
						.getRoot().findMember(entry.getPath());
				buildClassPath(cp, JavaCore.create(projEntry), true);
			}
		}
	}

	/**
	 * Returns an instance of ClassLoader that loads classes from the client
	 * project's class path.
	 * 
	 * The returned object may or may not be equal to a previously returned one.
	 * That means that there can be more than one class loader for the same
	 * project, and the classes loaded by them will be incompatible.
	 * 
	 * It's usually a good idea to use the ObjectTreeEditor's class loader.
	 * 
	 * @return a class loader
	 */
	public ClassLoader getClassLoader() {
		if (classLoader == null || true) {
			// TODO until there's a good plan to detect class path changes,
			// always rebuild classpath
			try {
				Set<URL> cp = new HashSet<URL>(); // we don't want duplicates
				buildClassPath(cp, getJavaProject());
				cp.remove(null);
				classLoader = new URLClassLoader(cp.toArray(new URL[cp.size()]));
			} catch (Exception e) {
				e.printStackTrace();
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		return classLoader;
	}

	/**
	 * Returns an instance of XStream that (de)serializes XML using the client
	 * project's class path.
	 * 
	 * The returned object may or may not be equal to a previously returned one.
	 * This method shares {@link #getClassLoader()}'s behavior regarding
	 * incompatible class definitions.
	 * 
	 * It's usually a good idea to use the ObjectTreeEditor's XStream instance.
	 * 
	 * @return an XStream instance
	 */
	public XStream getXStream() {
		// TODO see above
		if (xStream == null || true) {
			xStream = new XStream(new DomDriver());
			xStream.setClassLoader(getClassLoader());
		}
		return xStream;
	}

}
