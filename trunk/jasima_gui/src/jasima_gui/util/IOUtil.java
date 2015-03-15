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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class IOUtil {

	private IOUtil() {
		// never called
	}

	/**
	 * Reads from <code>is</code> until either an error occurs or EOF is
	 * reached.
	 * 
	 * @param is
	 *            the stream to read from
	 * @return all data read from <code>is</code>, or <code>null</code> if an
	 *         error occurred
	 */
	public static byte[] readFully(InputStream is) {
		final int BUFFER_SIZE = 65000;
		try {
			// could be improved by using an ArrayList<byte[]>
			ByteArrayOutputStream bos = new ByteArrayOutputStream(is.available());
			byte[] buffer = new byte[BUFFER_SIZE];
			int r;
			while ((r = is.read(buffer)) != -1) {
				bos.write(buffer, 0, r);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	public static String readFully(Reader rdr) {
		final int BUFFER_SIZE = 32000;
		try {
			StringBuffer buf = new StringBuffer();
			char[] buffer = new char[BUFFER_SIZE];
			int r;
			while ((r = rdr.read(buffer)) != -1) {
				buf.append(buffer, 0, r);
			}
			return buf.toString();
		} catch (IOException e) {
			return null;
		}
	}

}
