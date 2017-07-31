package com.nokia.oss.sdm.tools.dumi.spellchecker.inspections;

import com.nokia.oss.sdm.tools.dumi.spellchecker.util.StringUtil;
import com.nokia.oss.sdm.tools.dumi.spellchecker.util.TextRange;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class TextSplitter extends BaseSplitter
{
    private static final TextSplitter INSTANCE = new TextSplitter();
    private static final Pattern EXTENDED_WORD_AND_SPECIAL = Pattern.compile("(&[^;]+;)|(([#]|0x[0-9]*)?\\p{L}+'?\\p{L}[_\\p{L}]*)");

    public static TextSplitter getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void split (String text, TextRange range, List<String> splitWords)
    {
        if (text == null || StringUtil.isEmpty(text))
        {
            return;
        }

        doSplit(text, range, splitWords);
    }

    protected void doSplit(String text, TextRange range, List<String> splitWords)
    {
        final WordSplitter ws = WordSplitter.getInstance();
        Matcher matcher = EXTENDED_WORD_AND_SPECIAL.matcher(text);
        matcher.region(range.getStartOffset(), range.getEndOffset());
        while (matcher.find())
        {
            TextRange found = new TextRange(matcher.start(), matcher.end());
            ws.split(text, found, splitWords);
        }
    }
}
