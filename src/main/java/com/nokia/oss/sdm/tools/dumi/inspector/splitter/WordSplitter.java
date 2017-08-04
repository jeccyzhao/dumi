package com.nokia.oss.sdm.tools.dumi.inspector.splitter;

import com.nokia.oss.sdm.tools.dumi.util.TextRange;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class WordSplitter extends BaseSplitter
{
    private static final WordSplitter INSTANCE = new WordSplitter();
    private static final Pattern SPECIAL = Pattern.compile("&\\p{Alnum}{2};?|#\\p{Alnum}{3,6}|0x\\p{Alnum}?");

    public static WordSplitter getInstance()
    {
        return INSTANCE;
    }

    @Override
    public List<String> split (String text, TextRange range)
    {
        if (text == null || range.getLength() <= 1)
        {
            return null;
        }

        List<String> result = new ArrayList<>();
        Matcher specialMatcher = SPECIAL.matcher(text);
        specialMatcher.region(range.getStartOffset(), range.getEndOffset());
        if (specialMatcher.find())
        {
            TextRange found = new TextRange(specialMatcher.start(), specialMatcher.end());
            addWord(true, found, text, result);
        }
        else
        {
            return IdentifierSplitter.getInstance().split(text, range);
        }

        return result;
    }
}
