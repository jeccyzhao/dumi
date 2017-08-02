package com.nokia.oss.sdm.tools.dumi.inspector.splitter;

import com.nokia.oss.sdm.tools.dumi.spellchecker.util.TextRange;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x36zhao on 2017/7/31.
 */
public abstract class BaseSplitter implements Splitter
{
    public static final int MIN_RANGE_LENGTH = 1;

    protected static void addWord(boolean ignore, TextRange found, String text, List<String> splitWords)
    {
        if (found == null || ignore)
        {
            return;
        }

        boolean tooShort = (found.getEndOffset() - found.getStartOffset()) <= MIN_RANGE_LENGTH;
        if (tooShort)
        {
            return;
        }

        splitWords.add(found.substring(text));
    }

    protected static boolean badSize(int from, int till)
    {
        int l = till - from;
        return l <= MIN_RANGE_LENGTH;
    }

    static protected List<TextRange> excludeByPattern(String text, TextRange range, Pattern toExclude, int groupToInclude)
    {
        List<TextRange> toCheck = new ArrayList<>();
        int from = range.getStartOffset();
        int till;
        boolean addLast = true;
        Matcher matcher = toExclude.matcher(range.substring(text));
        try
        {
            while (matcher.find())
            {
                TextRange found = matcherRange(range, matcher);
                till = found.getStartOffset();
                if (range.getEndOffset() - found.getEndOffset() < MIN_RANGE_LENGTH)
                {
                    addLast = false;
                }
                if (!badSize(from, till))
                {
                    toCheck.add(new TextRange(from, till));
                }
                if (groupToInclude > 0)
                {
                    TextRange contentFound = matcherRange(range, matcher, groupToInclude);
                    if (badSize(contentFound.getEndOffset(), contentFound.getStartOffset()))
                    {
                        toCheck.add(TextRange.create(contentFound));
                    }
                }
                from = found.getEndOffset();
            }
            till = range.getEndOffset();
            if (badSize(from, till))
            {
                return toCheck;
            }
            if (addLast)
            {
                toCheck.add(new TextRange(from, till));
            }
            return toCheck;
        }
        catch (Exception e)
        {
            return Collections.singletonList(range);
        }
    }

    protected static boolean containsShortWord(List<TextRange> words)
    {
        for (TextRange word : words)
        {
            if (word.getLength() < MIN_RANGE_LENGTH)
            {
                return true;
            }
        }
        return false;
    }

    protected static TextRange matcherRange(TextRange range, Matcher matcher)
    {
        return subRange(range, matcher.start(), matcher.end());
    }

    protected static TextRange matcherRange(TextRange range, Matcher matcher, int group)
    {
        return subRange(range, matcher.start(group), matcher.end(group));
    }

    protected static TextRange subRange(TextRange range, int start, int end)
    {
        return TextRange.from(range.getStartOffset() + start, end - start);
    }

    protected static boolean isAllWordsAreUpperCased(String text, List<TextRange> words)
    {
        for (TextRange word : words)
        {
            CharacterIterator it = new StringCharacterIterator(text, word.getStartOffset(), word.getEndOffset(), word.getStartOffset());
            for (char c = it.first(); c != CharacterIterator.DONE; c = it.next())
            {
                if (!Character.isUpperCase(c))
                {
                    return false;
                }
            }
        }

        return true;
    }
}
