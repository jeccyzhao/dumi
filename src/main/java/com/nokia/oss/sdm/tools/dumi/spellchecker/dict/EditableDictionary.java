package com.nokia.oss.sdm.tools.dumi.spellchecker.dict;

import java.util.Collection;
import java.util.Set;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface EditableDictionary extends Dictionary
{
    Set<String> getEditableWords();
    void addToDictionary(String word);
    void removeFromDictionary(String word);
    void addToDictionary(Collection<String> words);
    void replaceAll(Collection<String> words);
    void clear();
}
