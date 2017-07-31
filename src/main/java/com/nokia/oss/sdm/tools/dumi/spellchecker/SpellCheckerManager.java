package com.nokia.oss.sdm.tools.dumi.spellchecker;

import com.nokia.oss.sdm.tools.dumi.spellchecker.bundle.BundledDictionaryProvider;
import com.nokia.oss.sdm.tools.dumi.spellchecker.bundle.DefaultBundledDictionariesProvider;
import com.nokia.oss.sdm.tools.dumi.spellchecker.engine.BaseSuggestionProvider;
import com.nokia.oss.sdm.tools.dumi.spellchecker.loader.StreamLoader;
import com.nokia.oss.sdm.tools.dumi.spellchecker.engine.SpellCheckerEngine;
import com.nokia.oss.sdm.tools.dumi.spellchecker.engine.SpellCheckerFactory;
import com.nokia.oss.sdm.tools.dumi.spellchecker.engine.SuggestionProvider;
import com.nokia.oss.sdm.tools.dumi.spellchecker.util.Strings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public final class SpellCheckerManager
{
    private static final int MAX_SUGGESTIONS_THRESHOLD = 5;
    private static final int MAX_METRICS = 1;

    private SpellCheckerEngine spellChecker = SpellCheckerFactory.create();
    private final SuggestionProvider suggestionProvider = new BaseSuggestionProvider(this);
    private final BundledDictionaryProvider dictionaryProvider = new DefaultBundledDictionariesProvider();
    private static SpellCheckerManager INSTANCE = new SpellCheckerManager();

    private SpellCheckerManager()
    {
        loadEngineDictionary();
    }

    public static SpellCheckerManager getInstance()
    {
        return INSTANCE;
    }

    private void loadEngineDictionary()
    {
        spellChecker.reset();
        for (String dictionary : dictionaryProvider.getBundledDictionaries())
        {
            final InputStream stream = this.getClass().getResourceAsStream("/" + dictionary);
            if (stream != null)
            {
                spellChecker.loadDictionary(new StreamLoader(stream, dictionary));
            }
        }
    }

    public List<String> getRawSuggestions(String word)
    {
        if (!spellChecker.isCorrect(word))
        {
            List<String> suggestions = spellChecker.getSuggestions(word, MAX_SUGGESTIONS_THRESHOLD, MAX_METRICS);
            if (suggestions.size() != 0)
            {
                boolean capitalized = Strings.isCapitalized(word);
                boolean upperCases = Strings.isUpperCase(word);
                if (capitalized)
                {
                    Strings.capitalize(suggestions);
                }
                else if (upperCases)
                {
                    Strings.upperCase(suggestions);
                }
            }
            List<String> result = new ArrayList<String>();
            for (String s : suggestions)
            {
                if (!result.contains(s))
                {
                    result.add(s);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    public List<String> getSuggestions(String text)
    {
        return suggestionProvider.getSuggestions(text);
    }

    public boolean hasProblem(String word)
    {
        return !spellChecker.isCorrect(word);
    }
}
