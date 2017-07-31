package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

import com.nokia.oss.sdm.tools.dumi.spellchecker.loader.Loader;

import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface SpellCheckerEngine
{
    boolean isIgnored(String paramString);

    boolean isCorrect(String paramString);

    List<String> getSuggestions(String word, int threshold, int quality);

    List<String> getVariants(String paramString);

    boolean isDictionaryLoad(String name);

    void loadDictionary(Loader loader);

    void removeDictionary(String name);

    void reset();

    Transformation getTransformation();
}
