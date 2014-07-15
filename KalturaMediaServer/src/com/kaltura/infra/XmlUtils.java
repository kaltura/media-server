package com.kaltura.infra;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlUtils {
	private static XPathFactory xPathFactory = XPathFactory.newInstance();

	public static void merge(File outputFile, String expression, String identifierAttribute, File... files) throws Exception {
		Document doc = merge(expression, identifierAttribute, files);
		write(doc, outputFile);
	}

	private static Document merge(String expression, String identifierAttribute, File... files) throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document base = docBuilder.parse(files[0]);
		removeTextNodes(base);

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
			removeTextNodes(merge);
			nextResults = (Node) compiledExpression.evaluate(merge, XPathConstants.NODE);
			while (nextResults.hasChildNodes()) {
				kid = nextResults.getFirstChild();
				nextResults.removeChild(kid);
				
				if(!(kid instanceof Element)){
					continue;
				}
				
				Attr identifier = (Attr) kid.getAttributes().getNamedItem(identifierAttribute);
				if(identifier != null){
					XPathExpression compiledIdentifierExpression = xpath.compile(expression + "/*[@" + identifierAttribute + " = '" + identifier.getValue() + "']");
					Node existingNode = (Node) compiledIdentifierExpression.evaluate(base, XPathConstants.NODE);
					if(existingNode != null){
						existingNode.getParentNode().removeChild(existingNode);
					}
				}
				
				kid = (Element) base.importNode(kid, true);
				results.appendChild(kid);
			}
		}

		indentNodes(base);
		return base;
	}

	private static void removeTextNodes(Document doc) throws XPathExpressionException{
		XPathExpression compiledExpression = xPathFactory.newXPath().compile("//text()");  
		NodeList textNodes = (NodeList) compiledExpression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < textNodes.getLength(); i++) {
		    Node emptyTextNode = textNodes.item(i);
		    emptyTextNode.getParentNode().removeChild(emptyTextNode);
		}
	}

	private static void indentNodes(Document doc){
		indentNodes(doc, "\n");
	}

	private static void indentNodes(Node node, String indent){
		if(!node.hasChildNodes()){
			return;
		}
		
		Text textNode;
		Document doc = node.getOwnerDocument();
		if(node instanceof Document){
			doc = (Document) node;
		}
		
		Node kid = node.getFirstChild();
		String nextIndent = indent + "\t";
		while(kid != null) {
			System.out.println(kid.getNodeName());
			
			if(!(node instanceof Document)){
				textNode = doc.createTextNode(indent);
				node.insertBefore(textNode, kid);
			}
			indentNodes(kid, nextIndent);
			
			if(kid.hasChildNodes()){
				textNode = doc.createTextNode(indent);
				kid.appendChild(textNode);
			}

			kid = kid.getNextSibling();
		}
	}

	public static void write(Document doc, String filePath) throws Exception {
		write(doc, new File(filePath));
	}

	public static void write(Document doc, File file) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		Result result = new StreamResult(file);
		transformer.transform(source, result);
	}

}
