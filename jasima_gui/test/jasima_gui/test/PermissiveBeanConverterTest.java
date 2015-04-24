package jasima_gui.test;

import jasima_gui.ConversionReport;
import jasima_gui.PermissiveBeanConverter;
import jasima_gui.util.IOUtil;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class PermissiveBeanConverterTest extends TestCase {

	protected XStream xStream;
	protected PermissiveBeanConverter converter;

	@Override
	protected void setUp() throws Exception {
		xStream = new XStream(new DomDriver());
		converter = new PermissiveBeanConverter(xStream.getMapper());
		xStream.registerConverter(converter, -10);
	}

	protected String readFile(String name) {
		try {
			return IOUtil.readFully(new InputStreamReader(PermissiveBeanConverterTest.class.getResourceAsStream(name),
					"utf-8"));
		} catch(UnsupportedEncodingException e) {
			throw new Error(e);
		}
	}

	@Test
	public void testEmptyReport() throws Exception {
		String input = readFile("pbc_clean_input.xml");

		converter.startConversionReport();
		xStream.fromXML(input); // ignore result
		ConversionReport report = converter.finishConversionReport();
		assertNull(report);
	}

	@Test
	public void testReports() throws Exception {
		for (int i = 1; i <= 4; ++i) {
			String input = readFile("pbc_input_" + i + ".xml");
			String expectedReport = readFile("pbc_report_" + i + ".xml");

			converter.startConversionReport();
			xStream.fromXML(input); // ignore result
			ConversionReport report = converter.finishConversionReport();
			assertEquals(expectedReport, report.toString());
		}
	}

}
