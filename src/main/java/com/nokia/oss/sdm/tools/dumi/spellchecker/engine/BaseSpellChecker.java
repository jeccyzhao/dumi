package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

import com.nokia.oss.sdm.tools.dumi.spellchecker.dict.BundledDictionary;
import com.nokia.oss.sdm.tools.dumi.spellchecker.dict.Dictionary;
import com.nokia.oss.sdm.tools.dumi.spellchecker.dict.EditableDictionary;
import com.nokia.oss.sdm.tools.dumi.spellchecker.loader.Loader;
import com.nokia.oss.sdm.tools.dumi.spellchecker.util.StringUtil;

import java.util.*;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class BaseSpellChecker implements SpellCheckerEngine
{
    private Set<Dictionary> bundledDictionaries = new HashSet<>();
    private final Set<EditableDictionary> userDictionaries = new HashSet<>();
    private final Transformation transform = new BaseTransformation();

    @Override
    public boolean isIgnored (String paramString)
    {
        return false;
    }

    @Override
    public boolean isCorrect (String word)
    {
       final String transformed = transform.transform(word);
       if (transformed == null) return true;

       int bundled = isCorrect(transformed, bundledDictionaries);
       int user = isCorrect(transformed, userDictionaries);
       return bundled == 0 || user == 0;
    }

    private static int isCorrect(String transformed, Collection<? extends Dictionary> dictionaries)
    {
        if (dictionaries == null || dictionaries.size() == 0)
        {
            return -1;
        }

        int errors = 0;
        for (Dictionary dictionary : dictionaries)
        {
            if (dictionary == null) continue;
            boolean contains = dictionary.contains(transformed);
            if (!contains)
            {
                ++errors;
            }
            else
            {
                return 0;
            }
        }

        if (errors == dictionaries.size()) return errors;
        return -1;
    }

    @Override
    public List<String> getSuggestions (String word, int threshold, int quality)
    {
        String transformed = transform.transform(word);
        if (transformed == null) return Collections.emptyList();

        final List<Suggestion> suggestions = new ArrayList<>();
        List<String> rawSuggestions = new ArrayList<>();
        rawSuggestions.addAll(restore(transformed.charAt(0), 0, Integer.MAX_VALUE, bundledDictionaries));
        rawSuggestions.addAll(restore(word.charAt(0), 0, Integer.MAX_VALUE, userDictionaries));

        if (rawSuggestions.isEmpty()) return Collections.emptyList();
        for (String rawSuggestion : rawSuggestions)
        {
            final int distance = EditDistance.calculateMetrics(transformed, rawSuggestion);
            suggestions.add(new Suggestion(rawSuggestion, distance));
        }
        List<String> result = new ArrayList<String>();
        if (suggestions.isEmpty())
        {
            return result;
        }
        Collections.sort(suggestions);
        int bestMetrics = suggestions.get(0).getMetrics();
        for (int i = 0; i < threshold; i++)
        {
            if (suggestions.size() <= i || bestMetrics - suggestions.get(i).getMetrics() > quality)
            {
                break;
            }
            result.add(i, suggestions.get(i).getWord());
        }

        return result;
    }

    private static List<String> restore(char startFrom, int i, int j, Collection<? extends Dictionary> dictionaries)
    {
        if (dictionaries == null)
        {
            return Collections.emptyList();
        }

        List<String> results = new ArrayList<String>();
        for (Dictionary o : dictionaries)
        {
            results.addAll(restore(startFrom, i, j, o));
        }

        return results;
    }

    private static List<String> restore(final char first, final int i, final int j, Dictionary dictionary)
    {
        if (dictionary == null)
        {
            return Collections.emptyList();
        }

        final List<String> result = new ArrayList<String>();
        for (String word : dictionary.getWords())
        {
            if (!StringUtil.isEmpty(word) && word.charAt(0) == first)
            {
                result.add(word);
            }
        }

        return result;
    }

    @Override
    public List<String> getVariants (String paramString)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean isDictionaryLoad (String name)
    {
        return getBundledDictionaryByName(name) != null;
    }

    @Override
    public void loadDictionary (Loader loader)
    {
        List<String> words = loader.load();
        if (words != null)
        {
            Dictionary dictionary = BundledDictionary.create(loader.getName(), words);
            if (dictionary != null)
            {
                bundledDictionaries.add(dictionary);
            }
        }
    }

    @Override
    public void removeDictionary (String name)
    {
        final Dictionary dictionaryByName = getBundledDictionaryByName(name);
        if (dictionaryByName != null)
        {
            bundledDictionaries.remove(dictionaryByName);
        }
    }

    @Override
    public void reset ()
    {
        bundledDictionaries.clear();
        userDictionaries.clear();
    }

    @Override
    public Transformation getTransformation ()
    {
        return transform;
    }

    private Dictionary getBundledDictionaryByName(String name)
    {
        for (Dictionary dictionary : bundledDictionaries)
        {
            if (name.equals(dictionary.getName()))
            {
                return dictionary;
            }
        }
        return null;
    }
}
