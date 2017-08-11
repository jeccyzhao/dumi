package com.nokia.oss.sdm.tools.dumi.inspector.transform;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by x36zhao on 2017/8/2.
 */
public interface Transformation
{
    String transform(String text);

    default Set<String> transform(Collection<String> text)
    {
        if (text == null || text.isEmpty())
        {
            return null;
        }
        Set<String> result = new HashSet<String>();
        for (String word : text)
        {
            String transformed = transform(word);
            if (transformed != null)
            {
                result.add(transformed);
            }
        }
        return result;
    }

    int getDeltaIndex (String word, int from, String transformedText);
}
