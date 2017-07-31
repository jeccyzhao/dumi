package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class BaseTransformation implements Transformation
{
    @Override
    public String transform(String word)
    {
        if (word == null) return null;
        word = word.trim();
        if (word.length() < 3)
        {
            return null;
        }

        return word.toLowerCase(Locale.ENGLISH);
    }
}
