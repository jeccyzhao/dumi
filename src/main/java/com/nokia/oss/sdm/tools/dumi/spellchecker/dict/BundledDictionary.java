package com.nokia.oss.sdm.tools.dumi.spellchecker.dict;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class BundledDictionary implements Dictionary
{
    private final String name;
    private final Set<String> words = new HashSet<>();

    public BundledDictionary (String name)
    {
        this.name = name;
    }

    public static BundledDictionary create (String name, final List<String> words)
    {
        final BundledDictionary dictionary = new BundledDictionary(name);
        for (String word : words)
        {
            dictionary.getWords().add(word);
        }
        return dictionary;
    }

    @Override
    public String getName ()
    {
        return this.name;
    }

    @Override
    public boolean contains (String word)
    {
        return words.contains(word);
    }

    @Override
    public boolean isEmpty ()
    {
        return false;
    }

    @Override
    public Set<String> getWords ()
    {
        return words;
    }

    @Override
    public int size ()
    {
        return words.size();
    }

    @Override
    public void traverse (String word)
    {
    }
}
