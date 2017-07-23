package com.nokia.oss.sdm.tools.dumi.inspector;

/**
 * Created by x36zhao on 2017/7/23.
 */
public class SpellingInspectorFactory
{
    public static AbstractSpellingInspector createSpellingChecker (String fileName)
    {
        if (fileName != null)
        {
            if (fileName.endsWith(".man"))
            {
                return new FragmentSpellingInspector();
            }
            else
            {
                return new PlainTextSpellingInspector();
            }
        }

        return null;
    }
}
