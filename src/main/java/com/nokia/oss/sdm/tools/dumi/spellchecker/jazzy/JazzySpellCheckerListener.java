package com.nokia.oss.sdm.tools.dumi.spellchecker.jazzy;

import com.nokia.oss.sdm.tools.dumi.spellchecker.engine.SpellCheckerEngine;
import com.nokia.oss.sdm.tools.dumi.spellchecker.engine.Transformation;
import com.nokia.oss.sdm.tools.dumi.spellchecker.loader.Loader;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class JazzySpellCheckerListener implements SpellCheckListener
{
    private List<String> listOfMisspelledWords = new ArrayList<>();

    @Override
    public void spellingError (SpellCheckEvent event)
    {
        event.ignoreWord(true);
        String invalidWord = event.getInvalidWord();
        System.out.print(" --- Invalid word: " + invalidWord + "   [");
        for (Object suggestion : event.getSuggestions())
        {
            System.out.print(suggestion + ",");
        }
        System.out.println("]");

        listOfMisspelledWords.add(event.getInvalidWord());
    }

    public List<String> getListOfMisspelledWords()
    {
        return listOfMisspelledWords;
    }
}
