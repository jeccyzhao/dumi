package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface Transformation
{
    String transform(String word);

    default Set<String> transform(Collection<String> words)
    {
        if (words == null || words.isEmpty())
        {
            return null;
        }
        Set<String> result = new HashSet<String>();
        for (String word : words)
        {
            String transformed = transform(word);
            if (transformed != null)
            {
                result.add(transformed);
            }
        }
        return result;
    }
}
