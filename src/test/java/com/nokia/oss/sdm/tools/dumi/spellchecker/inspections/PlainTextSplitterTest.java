package com.nokia.oss.sdm.tools.dumi.spellchecker.inspections;

import com.nokia.oss.sdm.tools.dumi.spellchecker.util.TextRange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class PlainTextSplitterTest
{
    @Test
    public void testWordInCapsAfterDelimiter()
    {
        String text = ",BADD";
        correctListToCheck(PlainTextSplitter.getInstance(), text, "BADD");
    }

    @Test
    public void testSplitSimpleCamelCase()
    {
        String text = "simpleCamelCase";
        correctListToCheck(IdentifierSplitter.getInstance(), text, "simple", "Camel", "Case");
    }

    @Test
    public void testWordsInSingleQuotesWithSep()
    {
        String text = "'test-something'";
        correctListToCheck(PlainTextSplitter.getInstance(), text, "test", "something");
    }

    @Test
    public void testEmail()
    {
        String text = "some text with email (shkate.test@gmail.com) inside";
        correctListToCheck(PlainTextSplitter.getInstance(), text, "some", "text", "with", "email", "inside");
    }

    private static List<String> wordsToCheck(Splitter splitter, final String text)
    {
        final List<String> words = new ArrayList<>();
        splitter.split(text, TextRange.allOf(text), words);
        return words;
    }

    private static void correctListToCheck(Splitter splitter, String text, String... expected)
    {
        List<String> words = wordsToCheck(splitter, text);
        List<String> expectedWords = Arrays.asList(expected);
        assertEquals("Splitting:'" + text + "'", expectedWords.toString(), words != null ? words.toString() : "[]");
    }
}
