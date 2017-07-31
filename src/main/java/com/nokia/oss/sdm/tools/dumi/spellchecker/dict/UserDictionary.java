package com.nokia.oss.sdm.tools.dumi.spellchecker.dict;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class UserDictionary implements EditableDictionary
{
    private final String name;
    private final Set<String> words = new HashSet<>();

    public UserDictionary(String name)
    {
        this.name = name;
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

    @Override
    public Set<String> getEditableWords ()
    {
        return words;
    }

    @Override
    public void addToDictionary (String word)
    {
        if (word != null)
        {
            words.add(word);
        }
    }

    @Override
    public void removeFromDictionary (String word)
    {
        if (word != null)
        {
            words.remove(word);
        }
    }

    @Override
    public void addToDictionary (Collection<String> words)
    {
        if (words != null && !words.isEmpty())
        {
            for (String word : words)
            {
                addToDictionary(word);
            }
        }
    }

    @Override
    public void replaceAll (Collection<String> words)
    {
        clear();
        addToDictionary(words);
    }

    @Override
    public void clear ()
    {
        words.clear();
    }

    @Override
    public String toString()
    {
        return "UserDictionary {" + "name='" + name + '\'' + ", words.count=" + words.size() + '}';
    }
}
