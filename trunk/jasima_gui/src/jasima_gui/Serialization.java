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

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Serialization {
	protected IJavaProject project;
	protected XStream xStream;
	protected EclipseProjectClassLoader epcl;

	public Serialization(IProject project) {
		this(JavaCore.create(project));
	}

	public Serialization(IJavaProject project) {
		this.project = project;
		epcl = new EclipseProjectClassLoader(project);
		xStream = new XStream(new DomDriver());
		xStream.registerConverter(new JavaBeanConverter(xStream.getMapper()), -10);
		xStream.setClassLoader(epcl);
	}
	
	public EclipseProjectClassLoader getClassLoader() {
		return epcl;
	}
	
	public IJavaProject getProject() {
		return project;
	}
	
	public XStream getXStream() {
		return xStream;
	}
}
