package com.nokia.oss.sdm.tools.dumi.spellchecker.suggestion;

import com.nokia.oss.sdm.tools.dumi.inspector.PlainTextSpellingInspector;
import com.nokia.oss.sdm.tools.dumi.inspector.SpellingInspectorFactory;
import com.nokia.oss.sdm.tools.dumi.spellchecker.SpellCheckerManager;
import com.nokia.oss.sdm.tools.dumi.spellchecker.inspections.PlainTextSplitter;
import com.nokia.oss.sdm.tools.dumi.spellchecker.util.TextRange;
import com.nokia.oss.sdm.tools.dumi.util.FileUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class SuggestionProviderTest
{
    @Test
    public void testSuggestions ()
    {
        doTest("upgade", "upgrade");
        doTest("MEMRY", "MEMORY");
    }

    @Test
    public void testSuggestFromFile () throws Exception
    {
        //PlainTextSpellingInspector plainTextSpellingInspector = new PlainTextSpellingInspector();
        //plainTextSpellingInspector.doProcess("D:\\Workspace\\SVN\\adaptations\\com.nsn.hp.hphw\\com.nsn.hp.hphw-10\\test\\oss_activity0_0.log");


        List<String> lines = FileUtil.getFileContent(
                "D:\\Workspace\\SVN\\adaptations\\com.nsn.hp.hphw\\com.nsn.hp.hphw-10\\test\\oss_activity0_0.log", false);

        System.out.println("Total lines " + lines.size());

        long start = System.nanoTime();
        int lineNum = 0;
        for (String line : lines)
        {
            System.out.println("Processing line - " + lineNum + ": " + line);
            final List<String> words = new ArrayList<>();
            PlainTextSplitter.getInstance().split(line, TextRange.allOf(line), words);
            if (words != null && words.size() > 0)
            {
                for (String word : words)
                {
                    List<String> suggestions = SpellCheckerManager.getInstance().getSuggestions(word);
                    if (suggestions.get(0) != null)
                    {
                        System.out.println(" --- Line" + lineNum + " : " + word + " --> " + suggestions);
                        suggestions = null;
                    }
                }
            }

            lineNum++;
        }

        System.out.println(System.nanoTime() - start + " ns");
    }

    private void doTest(String word, String expected)
    {
        List<String> result = SpellCheckerManager.getInstance().getSuggestions(word);
        assertEquals(expected, result.get(0));
    }
}
