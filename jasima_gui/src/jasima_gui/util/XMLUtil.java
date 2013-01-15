/*******************************************************************************
 * Copyright (c) 2010-2013 Torsten Hildebrandt and jasima contributors
 *
 * This file is part of jasima, v1.0.
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
package jasima_gui.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import com.thoughtworks.xstream.XStream;

public class XMLUtil {

	private XMLUtil() {
	}

	public static byte[] serialize(XStream xs, Object o) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
			OutputStreamWriter wrtr = new OutputStreamWriter(bos, "UTF-8");
			wrtr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			xs.toXML(o, wrtr);
			wrtr.close();
			byte[] retVal = bos.toByteArray();
			return retVal;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
