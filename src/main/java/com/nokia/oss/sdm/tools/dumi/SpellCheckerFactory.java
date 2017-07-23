package com.nokia.oss.sdm.tools.dumi;

import com.nokia.oss.sdm.tools.dumi.inspector.AbstractSpellingInspector;
import com.nokia.oss.sdm.tools.dumi.inspector.FragmentSpellingInspector;

/**
 * Created by x36zhao on 2017/7/21.
 */
public class SpellCheckerFactory
{
    public static AbstractSpellingInspector getSpellChecker (String fileSuffix)
    {
        if (".man".equals(fileSuffix))
        {
            return new FragmentSpellingInspector();
        }

        return null;
    }
}
