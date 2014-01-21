package com.kaltura.infra;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlUtils {

	public static void main(String[] args) throws Exception {
		// proper error/exception handling omitted for brevity
		File file1 = new File("1.smil");
		File file2 = new File("2.smil");
		Document doc = merge("/smil/body/switch", file1, file2);
		write(doc, "out.smil");
	}

	public static void merge(File outputFile, String expression, File... files) throws Exception {
		Document doc = merge(expression, files);
		write(doc, outputFile);
	}

	public static Document merge(String expression, File... files) throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document base = docBuilder.parse(files[0]);

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression compiledExpression = xpath.compile(expression);
		
		Node results = (Node) compiledExpression.evaluate(base, XPathConstants.NODE);
		if (results == null) {
			throw new IOException(files[0] + ": expression does not evaluate to node");
		}

		Document merge;
		Node nextResults;
		Node kid;

		for (int i = 1; i < files.length; i++) {
			merge = docBuilder.parse(files[i]);
			nextResults = (Node) compiledExpression.evaluate(merge, XPathConstants.NODE);
			while (nextResults.hasChildNodes()) {
				kid = nextResults.getFirstChild();
				nextResults.removeChild(kid);
				kid = base.importNode(kid, true);
				results.appendChild(kid);
			}
		}

		return base;
	}

	private static void write(Document doc, String filePath) throws Exception {
		write(doc, new File(filePath));
	}

	private static void write(Document doc, File file) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		Result result = new StreamResult(file);
		transformer.transform(source, result);
	}

}
