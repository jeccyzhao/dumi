package com.nokia.oss.sdm.tools.dumi.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author rmagda
 *
 */
public final class DomUtil
{

    private DomUtil()
    {

    }

    /**
     * @param source
     * @return
     * @throws Exception
     */
    public static Document getXMLfromFile(final File source) throws Exception
    {
        return getXMLfromFile(source, true);
    }

    /**
     *
     * @param source
     * @param ignoreDtdValidation
     * @return
     * @throws Exception
     */
    public static Document getXMLfromFile(final File source, boolean ignoreDtdValidation) throws Exception
    {
        Document document = null;

        try
        {
            final DocumentBuilderFactory documentBuilderFactory =
                    DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            if (ignoreDtdValidation)
            {
                documentBuilder.setEntityResolver(new IgnoreDTDEntityResolver());
            }

            document = documentBuilder.parse(source);

            document.normalize();
        }
        catch (final Exception e)
        {
            // LOGGER.error("Creating XML from file failed: " + e.getMessage(), e);
            throw e;
        }

        //LOGGER.debug("XML successfully converted from file: " + source);

        return document;
    }

    public String getXMLStringFromFile(String filePath)
    {
        String messageStr = "";

        try
        {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filePath));

            document.normalize();

            StringWriter writer = new StringWriter();
            StreamResult result2 = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
            transformer.transform(source, result2);
            messageStr = writer.toString();
        }
        catch (Exception e)
        {
            // LOGGER.error("Creating message faild: " + e.getMessage(), e);
        }

        return messageStr;
    }

    public static void saveXMLtoFile(final Document xml,
                                     final File destination) throws TransformerFactoryConfigurationError, TransformerException
    {
        // Prepare the DOM document for writing
        final Source source = new DOMSource(xml);

        final Result result = new StreamResult(destination);

        // Write the DOM document to the file
        final Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);
    }

    /**
     *
     * @param node
     * @return all node children as Document
     * @throws TransformerException
     */
    public static Document convertNodeIntoDocument(final Node node)
            throws TransformerException
    {
        final TransformerFactory tf = TransformerFactory.newInstance();
        final Transformer xf = tf.newTransformer();
        final DOMResult dr = new DOMResult();
        xf.transform(new DOMSource(node), dr);
        final Document newDoc = (Document)dr.getNode();
        return newDoc;
    }

    /**
     * Methods logs:
     * <ul>
     * <li>if more than one node is found (on debug)
     * <li>if nothing can be found (on error) + RuntimeException
     * <li>if node has children (on warn)
     * </ul>
     *
     * @param nodeName
     * @param doc
     * @return value of first found node
     * @throws IllegalStateException if nothing can be found
     */
    public static String getValueOfElement(final String nodeName, final Document doc)
            throws IllegalStateException
    {
        final NodeList nodeList = doc.getElementsByTagName(nodeName);
        final int found = nodeList.getLength();

        if (found == 0)
        {
            final String msg = "Could not find " + nodeName + " in " + doc.getNodeName();
            // LOGGER.error(msg);
            throw new IllegalStateException(msg);
        }
        else if (found != 1)
        {
            // LOGGER.debug("getValueOfElement found " + nodeList.getLength() + " nodes");
        }

        final Node node = nodeList.item(0);
        if (node.getChildNodes().getLength() != 0)
        {
            // LOGGER.warn("Found node " + node.getTextContent() + " has children");
        }

        final String returnValue = node.getTextContent();

        // LOGGER.info(returnValue);

        return returnValue;
    }

    public static String nodeToString(final Node node)
    {
        try
        {
            final Source source = new DOMSource(node);
            final StringWriter stringWriter = new StringWriter();
            final Result result = new StreamResult(stringWriter);
            final TransformerFactory factory = TransformerFactory.newInstance();
            final Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        }
        catch (final TransformerConfigurationException e)
        {
            // LOGGER.error("Exception while converting node to string", e);
        }
        catch (final TransformerException e)
        {
            // LOGGER.error("Exception while converting node to string", e);
        }
        return null;
    }

    public static Node getNextSibling(final Node node,
                                      final short nodeType)
    {
        // NOPMD
        // short
        // because
        // org.w3c.dom.Node
        // uses
        // shorts
        if (node == null)
        {
            return null;
        }

        Node nextNode = node.getNextSibling();
        while (nextNode != null && nextNode.getNodeType() != nodeType)
        {
            nextNode = nextNode.getNextSibling();
        }

        return nextNode;
    }

    public static Node getNextSibling(final Node node,
                                      final short nodeType, final String name)
    {
        // NOPMD
        // short
        // because
        // org.w3c.dom.Node
        // uses
        // shorts

        if (node == null)
        {
            return null;
        }

        Node nextNode = getNextSibling(node, nodeType);
        while (nextNode != null && !nextNode.getNodeName().equalsIgnoreCase(name))
        {
            nextNode = getNextSibling(nextNode, nodeType);
        }

        return nextNode;
    }

    /**
     * Entity resolver without DTD validation
     *
     * @author Jeccy.Zhao
     *
     */
    private static class IgnoreDTDEntityResolver implements EntityResolver
    {
        public InputSource resolveEntity(String publicId,
                                         String systemId) throws SAXException, IOException
        {
            return new InputSource(
                    new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
        }
    }
}
