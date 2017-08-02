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
        //doTest("Wait", "");
    }
    private void doTest(String word, String expected)
    {
        List<String> result = SpellCheckerManager.getInstance().getSuggestions(word);
        assertEquals(expected, result.get(0));
    }
}
