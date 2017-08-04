package com.nokia.oss.sdm.tools.dumi.inspector.splitter;

import com.nokia.oss.sdm.tools.dumi.util.TextRange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by x36zhao on 2017/8/2.
 */
public class PlainTextSplitterTest
{
    @Test
    public void testWordInCapsAfterDelimiter()
    {
        String text = "2016-11-16T10:30:02.976+0200 | (2,203,826) | 000003d4 | WorkManager.Scheduler WorkManager : 3 | INFO | com.nsn.oss.mpm.scheduler.ejb.MPMPollSubscribersJobSessionBean. | ---------Scheduler: All NE ActiveSubscribers got, totally: 5---------";
        correctListToCheck(PlainTextSplitter.getInstance(), text, "Active", "Subscribers");
    }

    private static List<String> wordsToCheck(Splitter splitter, final String text)
    {
        final List<String> words = splitter.split(text, TextRange.allOf(text));
        return words;
    }

    private static void correctListToCheck(Splitter splitter, String text, String... expected)
    {
        List<String> words = wordsToCheck(splitter, text);
        List<String> expectedWords = Arrays.asList(expected);
        assertEquals("Splitting:'" + text + "'", expectedWords.toString(), words != null ? words.toString() : "[]");
    }
}
