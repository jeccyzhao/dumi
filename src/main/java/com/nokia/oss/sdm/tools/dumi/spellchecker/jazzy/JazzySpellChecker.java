package com.nokia.oss.sdm.tools.dumi.spellchecker.jazzy;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;

import java.io.*;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class JazzySpellChecker
{
    private static final String DICTIONARY_FILE = "/dict/english.dic";
    private static SpellDictionary dictionary;

    private SpellChecker spellChecker;
    private JazzySpellCheckerListener spellCheckerListener;

    static
    {
        loadDictionary();
    }

    private static void loadDictionary()
    {
        InputStream stream = JazzySpellCheckerListener.class.getResourceAsStream(DICTIONARY_FILE);
        try
        {
            if (stream != null)
            {
                dictionary = new SpellDictionaryHashMap(new InputStreamReader((stream)));
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Dictionary File '" + DICTIONARY_FILE + "' not found! Quitting. " + e);
        }
        catch (IOException ex)
        {
            System.err.println("IOException occurred while trying to read the dictionary file: " + ex);
        }
    }

    public List<String> check (String text) throws IOException
    {
        spellChecker = new SpellChecker(dictionary);
        spellCheckerListener = new JazzySpellCheckerListener();
        spellChecker.addSpellCheckListener(spellCheckerListener);

        StringWordTokenizer tokenizer = new StringWordTokenizer(text, new TeXWordFinder());
        spellChecker.checkSpelling(tokenizer);
        tokenizer = null;

        return spellCheckerListener.getListOfMisspelledWords();
    }
}
