package com.nokia.oss.o2ml.metadata.fragment.parser;

import com.nokia.oss.o2ml.metadata.ModelAnnotation;
import com.nokia.oss.o2ml.metadata.ModelAnnotationElement;
import com.nokia.oss.o2ml.metadata.fragment.BaseFragment;
import com.nokia.oss.sdm.tools.dumi.util.DomUtil;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/21.
 */
public abstract class AbstractXmlParser
{
    /**
     * The LOGGER
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractXmlParser.class);

    /**
     * Instance of XML Document
     */
    protected Document xmlDoc;

    /**
     * XML file
     */
    protected File xmlFile;

    /**
     * Instance of XPath
     */
    protected final XPath xpath = XPathFactory.newInstance().newXPath();

    /**
     * The sole constructor
     *
     * @throws Exception
     */
    public AbstractXmlParser ()
    {
    }

    /**
     * Evaluates attribute value from node by the specified item name
     *
     * @param node
     * @param attributeName
     * @return
     */
    protected String evaluateAttributeValue (final Node node, String attributeName)
    {
        if (node.getAttributes() != null)
        {
            Node item = node.getAttributes().getNamedItem(attributeName);
            if (item != null)
            {
                return item.getTextContent();
            }
        }

        return null;
    }

    /**
     * Evaluates value via XPATH
     *
     * @param node
     * @param path
     * @return
     * @throws XPathExpressionException
     */
    protected String evaluateNodeAttrValueByPath (final Node node, String path, String attributeName) throws XPathExpressionException
    {
        Node element = evaluateNodeByPath(node, path);
        if (element != null)
        {
            return evaluateAttributeValue(element, attributeName);
        }

        return null;
    }

    protected Node evaluateNodeByPath (final Node node, String path) throws XPathExpressionException
    {
        if (node != null)
        {
            XPathExpression expr = xpath.compile(path);
            Node element = (Node) expr.evaluate(node, XPathConstants.NODE);
            if (element != null)
            {
               return element;
            }
        }

        return null;
    }


    /**
     * Evaluates annotation nodes
     *
     * @param node
     * @return
     */
    protected List<ModelAnnotation> evaluateAnnotations (final Node node)
    {
        List<ModelAnnotation> annotations = null;

        try
        {
            // annotations
            XPathExpression exprAnnotation = xpath.compile("annotations");
            NodeList annotationNodes = (NodeList) exprAnnotation.evaluate(
                    node, XPathConstants.NODESET);

            if (annotationNodes != null)
            {
                annotations = new ArrayList<ModelAnnotation>();
                for (int i = 0, size = annotationNodes.getLength(); i < size; i++)
                {
                    // evaluates annotation node
                    ModelAnnotation annotation = this.evaluateAnnotationNode(annotationNodes.item(i));
                    annotations.add(annotation);
                }
            }
        }
        catch (Exception e)
        {
            // TODO LOGGER HERE...
        }

        return annotations;
    }

    /**
     * Evaluates annotation node
     *
     * @param node
     * @return
     * @throws XPathExpressionException
     */
    private ModelAnnotation evaluateAnnotationNode (final Node node) throws XPathExpressionException
    {
        if (node != null)
        {
            ModelAnnotation annotation = new ModelAnnotation();

            // elements
            XPathExpression exprElements = xpath.compile("elements");
            NodeList eleNodes = (NodeList) exprElements.evaluate(
                    node, XPathConstants.NODESET);

            if (eleNodes != null)
            {
                for (int i = 0, size = eleNodes.getLength(); i < size; i++)
                {
                    ModelAnnotationElement element = this.evaluateAnnotationElementNode(eleNodes.item(i));
                    annotation.appendElements(element);
                }
            }

            return annotation;
        }

        return null;
    }

    /**
     * Evaluates annotation element node
     *
     * @param node
     * @return
     */
    private ModelAnnotationElement evaluateAnnotationElementNode (final Node node)
    {
        if (node != null)
        {
            ModelAnnotationElement element = new ModelAnnotationElement();
            element.setName(this.evaluateAttributeValue(node, "name"));
            element.setValue(this.evaluateAttributeValue(node, "value"));
            return element;
        }

        return null;
    }

    /**
     * Entry to parse the specified XML file
     *
     * @return
     */
    public BaseFragment parse (String fragmentFile) throws Exception
    {
        xmlFile = new File(fragmentFile);

        if (xmlFile.exists())
        {
            xmlDoc = DomUtil.getXMLfromFile(xmlFile);
        }
        else
        {
            throw new RuntimeException("The given xml file does not exist," + xmlFile);
        }

        LOGGER.debug("Parsing file: `" + xmlFile.getPath() + "`");
        return doParse();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    protected abstract BaseFragment doParse() throws Exception;

}

