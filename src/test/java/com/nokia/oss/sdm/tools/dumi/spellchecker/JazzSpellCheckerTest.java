package com.nokia.oss.sdm.tools.dumi.spellchecker;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class JazzSpellCheckerTest implements SpellCheckListener
{
    private final String DICTIONARY_FILE = "D:\\Workspace\\SVN\\tools\\dumi\\src\\test\\resources\\english.dic";

    private final String string1 = "This is a sample test string with no misspellings.";   // first test string
    private final String string2 = "Viagra will make your male m'ember MEORY larger";            // second test string

    private SpellChecker spellChecker;
    private ArrayList listOfMisspelledWords;

    public static void main (String[] args)
    {
        new JazzSpellCheckerTest();
    }

    public JazzSpellCheckerTest()
    {
        createDictionary();
        spellChecker.addSpellCheckListener(this);

        // run the test on string1
        StringWordTokenizer texTok =
                new StringWordTokenizer(string1, new TeXWordFinder());
        populateListOfMisspelledWords(texTok);
        printWordsInMisspelledList();

        // run a second test, this one on string2
        texTok = new StringWordTokenizer(string2, new TeXWordFinder());
        populateListOfMisspelledWords(texTok);
        printWordsInMisspelledList();
    }

    private void createDictionary()
    {
        File dict = new File(DICTIONARY_FILE);
        try
        {
            spellChecker = new SpellChecker(new SpellDictionaryHashMap(dict));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Dictionary File '" + dict + "' not found! Quitting. " + e);
            System.exit(1);
        }
        catch (IOException ex)
        {
            System.err.println("IOException occurred while trying to read the dictionary file: " + ex);
            System.exit(2);
        }
    }

    private void populateListOfMisspelledWords(StringWordTokenizer texTok)
    {
        listOfMisspelledWords = new ArrayList();
        spellChecker.checkSpelling(texTok);
    }

    private void printWordsInMisspelledList()
    {
        Iterator it = listOfMisspelledWords.iterator();
        while (it.hasNext())
        {
            System.out.println("listOfMisspelledWords: " + it.next());
        }
    }

    @Override
    public void spellingError (SpellCheckEvent event)
    {
        event.ignoreWord(true);
        listOfMisspelledWords.add(event.getInvalidWord());
        for (Object suggestion : event.getSuggestions())
        {
            //System.out.println(suggestion);
        }
    }
}
