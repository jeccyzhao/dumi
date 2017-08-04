package com.nokia.oss.sdm.tools.dumi.inspector.splitter;

import com.nokia.oss.sdm.tools.dumi.util.Strings;
import com.nokia.oss.sdm.tools.dumi.util.TextRange;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class IdentifierSplitter extends BaseSplitter
{
    private static final IdentifierSplitter INSTANCE = new IdentifierSplitter();
    private static final Pattern WORD = Pattern.compile("\\b\\p{L}*'?\\p{L}*");
    private static final Pattern WORD_IN_QUOTES = Pattern.compile("'([^']*)'");

    public static IdentifierSplitter getInstance()
    {
        return INSTANCE;
    }

    @Override
    public List<String> split (String text, TextRange range)
    {
        List<String> result = new ArrayList<>();
        if (text == null || range.getLength() < 1 || range.getStartOffset() < 0)
        {
            return null;
        }

        List<TextRange> extracted = excludeByPattern(text, range, WORD_IN_QUOTES, 1);
        for (TextRange textRange : extracted)
        {
            List<TextRange> words = splitByCase(text, textRange);
            if (words.size() == 0)
            {
                continue;
            }

            if (words.size() == 1)
            {
                addWord(false, words.get(0), text, result);
                continue;
            }

            boolean isCapitalized = Strings.isCapitalized(text, words.get(0));
            boolean containsShortWord = containsShortWord(words);

            if (isCapitalized && containsShortWord)
            {
                continue;
            }

            boolean isAllWordsAreUpperCased = isAllWordsAreUpperCased(text, words);
            for (TextRange word : words)
            {
                boolean uc = Strings.isUpperCased(text, word);
                boolean flag = (uc && !isAllWordsAreUpperCased);
                Matcher matcher = WORD.matcher(text.substring(word.getStartOffset(), word.getEndOffset()));
                if (matcher.find())
                {
                    TextRange found = matcherRange(word, matcher);
                    addWord(flag, found, text, result);
                }
            }
        }

        return result;
    }

    private static List<TextRange> splitByCase(String text, TextRange range)
    {
        //System.out.println("text = " + text + " range = " + range);
        List<TextRange> result = new ArrayList<>();
        int i = range.getStartOffset();
        int s = -1;
        int prevType = Character.MATH_SYMBOL;
        while (i < range.getEndOffset())
        {
            final char ch = text.charAt(i);
            if (ch >= '\u3040' && ch <= '\u309f' || // Hiragana
                    ch >= '\u30A0' && ch <= '\u30ff' || // Katakana
                    ch >= '\u4E00' && ch <= '\u9FFF' || // CJK Unified ideographs
                    ch >= '\uF900' && ch <= '\uFAFF' || // CJK Compatibility Ideographs
                    ch >= '\uFF00' && ch <= '\uFFEF' //Halfwidth and Fullwidth Forms of Katakana & Fullwidth ASCII variants
                    )
            {
                if (s >= 0)
                {
                    add(text, result, i, s);
                    s = -1;
                }
                prevType = Character.MATH_SYMBOL;
                ++i;
                continue;
            }

            final int type = Character.getType(ch);
            if (type == Character.LOWERCASE_LETTER ||
                    type == Character.UPPERCASE_LETTER ||
                    type == Character.TITLECASE_LETTER ||
                    type == Character.OTHER_LETTER ||
                    type == Character.MODIFIER_LETTER ||
                    type == Character.OTHER_PUNCTUATION
                    )
            {
                //letter
                if (s < 0)
                {
                    //start
                    s = i;
                } else if (s >= 0 && type == Character.UPPERCASE_LETTER && prevType == Character.LOWERCASE_LETTER)
                {
                    //a|Camel
                    add(text, result, i, s);
                    s = i;
                } else if (i - s >= 1 && type == Character.LOWERCASE_LETTER && prevType == Character.UPPERCASE_LETTER)
                {
                    //CAPITALN|ext
                    add(text, result, i - 1, s);
                    s = i - 1;
                }
            } else if (s >= 0)
            {
                //non-letter
                add(text, result, i, s);
                s = -1;
            }
            prevType = type;
            i++;
        }
        //remainder
        if (s >= 0)
        {
            add(text, result, i, s);
        }
        return result;
    }

    private static void add(String text, List<TextRange> result, int i, int s)
    {
        if (i - s > 1)
        {
            final TextRange textRange = new TextRange(s, i);
            //System.out.println("textRange = " + textRange + " = "+ textRange.substring(text));
            result.add(textRange);
        }
    }
}
