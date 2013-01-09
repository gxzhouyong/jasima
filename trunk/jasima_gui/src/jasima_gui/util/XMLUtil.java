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
