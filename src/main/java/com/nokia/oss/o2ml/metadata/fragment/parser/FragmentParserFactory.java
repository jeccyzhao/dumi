package com.nokia.oss.o2ml.metadata.fragment.parser;

import com.nokia.oss.sdm.tools.dumi.exception.FragmentNotSupportedException;

import java.io.File;

/**
 * Created by x36zhao on 2017/7/22.
 */
public class FragmentParserFactory
{
    public static AbstractXmlParser createFragmentParser (String fileName) throws FragmentNotSupportedException
    {
        if (fileName != null)
        {
            if (fileName.endsWith(".man"))
            {
                return new AlarmManFragmentParser();
            }
            else if (fileName.endsWith(".common"))
            {
                return new CommonFragmentParser();
            }
        }

        throw new FragmentNotSupportedException("Not supported fragment: " + fileName);
    }
}
