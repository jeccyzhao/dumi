package com.nokia.oss.sdm.tools.dumi.inspector.transform;

import com.nokia.oss.sdm.tools.dumi.inspector.rule.FilterRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by x36zhao on 2017/8/2.
 */
public class TextTransformation implements Transformation
{
    private static final String PLACE_HOLDER = "#";
    private String rawText;
    private List<FilterRule> filterRules;
    private Map<Integer, String> placeHolders;

    public TextTransformation (String text, List<FilterRule> filterRules)
    {
        this.rawText = text;
        this.filterRules = filterRules;
        this.placeHolders = new HashMap<>();
    }

    @Override
    public String transform (String text)
    {
        int index = 0;
        if (filterRules != null)
        {
            for (FilterRule filterRule : filterRules)
            {
                List<String> filterTextList = filterRule.filterText(text);
                if (filterTextList != null)
                {
                    for (String filterText : filterTextList)
                    {
                        placeHolders.put(index, filterText);
                        text = text.replace(filterText, makePlaceholder(index));
                        index++;
                    }
                }
            }
        }

        return text;
    }

    @Override
    public int getDeltaIndex (String word, String transformedText)
    {
        int deltaIndex = 0;
        int wordPos = transformedText.indexOf(word);
        for (int placeHolderIndex : placeHolders.keySet())
        {
            String placeHolderText = makePlaceholder(placeHolderIndex);
            int placeHolderPos = transformedText.indexOf(placeHolderText);
            if (wordPos > placeHolderPos)
            {
                String rawStr = placeHolders.get(placeHolderIndex);
                wordPos = rawStr.length() + wordPos;
                deltaIndex += rawStr.length() - placeHolderText.length();
            }
        }

        return deltaIndex;
    }

    private String makePlaceholder (int index)
    {
        return PLACE_HOLDER + index + PLACE_HOLDER;
    }

    public String getRawText()
    {
        return rawText;
    }
}
