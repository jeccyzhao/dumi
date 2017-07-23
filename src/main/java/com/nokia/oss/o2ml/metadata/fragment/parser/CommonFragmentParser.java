package com.nokia.oss.o2ml.metadata.fragment.parser;

import com.nokia.oss.o2ml.metadata.fragment.BaseFragment;
import com.nokia.oss.o2ml.metadata.fragment.CommonFragment;
import org.w3c.dom.Node;

/**
 * Created by x36zhao on 2017/7/23.
 */
public class CommonFragmentParser extends AbstractXmlParser
{
    public CommonFragmentParser ()
    {
        super();
    }

    /**
     * @return
     * @throws Exception
     */
    protected BaseFragment doParse () throws Exception
    {
        CommonFragment commonFragment = new CommonFragment();
        commonFragment.setFileName(xmlFile.getPath());

        Node rootNode = xmlDoc.getFirstChild();
        commonFragment.setVendor(evaluateAttributeValue(rootNode, "vendor"));
        commonFragment.setAdaptationId(evaluateAttributeValue(rootNode, "id"));
        commonFragment.setAdaptationRelease(evaluateAttributeValue(rootNode, "release"));
        commonFragment.setPresentation(evaluateAttributeValue(rootNode, "presentation"));
        commonFragment.setDbSchemaPrefix(evaluateAttributeValue(rootNode, "dbSchemaPrefix"));

        return commonFragment;
    }
}
