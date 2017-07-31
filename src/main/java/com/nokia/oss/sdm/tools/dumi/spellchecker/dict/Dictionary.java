package com.nokia.oss.sdm.tools.dumi.spellchecker.dict;

import java.util.Set;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface Dictionary
{
    String getName();
    boolean contains(String word);
    boolean isEmpty();
    Set<String> getWords();
    int size();
    void traverse(String word);
}
