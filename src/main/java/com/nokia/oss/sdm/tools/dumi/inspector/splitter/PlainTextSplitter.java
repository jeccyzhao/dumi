package com.nokia.oss.sdm.tools.dumi.inspector.splitter;

import com.nokia.oss.sdm.tools.dumi.util.TextRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class PlainTextSplitter extends BaseSplitter
{
    private static final PlainTextSplitter INSTANCE = new PlainTextSplitter();
    private static final Pattern SPLIT_PATTERN = Pattern.compile("(\\s|\\b)");
    private static final Pattern MAIL = Pattern.compile("([\\p{L}0-9\\.\\-\\_]+@([\\p{L}0-9\\-\\_]+\\.)+(com|net|[a-z]{2}))");
    private static final Pattern URL = Pattern.compile("((ftp|http|file|https)://([^/]+)(/\\w*)?(/\\w*))");

    public static PlainTextSplitter getInstance()
    {
        return INSTANCE;
    }

    @Override
    public List<String> split (String text, TextRange range)
    {
        final List<String> result = new ArrayList<>();
        final String substring = range.substring(text).replace('\b', '\n').replace('\f', '\n');
        final TextSplitter textSplitter = TextSplitter.getInstance();
        int from = range.getStartOffset();
        int till;
        Matcher matcher = SPLIT_PATTERN.matcher(range.substring(text));
        while (true)
        {
            List<TextRange> toCheck;
            TextRange wRange;
            String word;
            if (matcher.find())
            {
                TextRange found = matcherRange(range, matcher);
                till = found.getStartOffset();
                if (badSize(from, till))
                {
                    continue;
                }
                wRange = new TextRange(from, till);
                word = wRange.substring(text);
                from = found.getEndOffset();
            }
            else
            {
                // end hit or zero matches
                wRange = new TextRange(from, range.getEndOffset());
                word = wRange.substring(text);
            }
            if (word.contains("@"))
            {
                toCheck = excludeByPattern(text, wRange, MAIL, 0);
            }
            else if (word.contains("://"))
            {
                toCheck = excludeByPattern(text, wRange, URL, 0);
            }
            else
            {
                toCheck = Collections.singletonList(wRange);
            }
            for (TextRange r : toCheck)
            {
                result.addAll(textSplitter.split(text, r));
            }
            if (matcher.hitEnd()) break;
        }
        //words.add(text);
        return result;
    }

}
