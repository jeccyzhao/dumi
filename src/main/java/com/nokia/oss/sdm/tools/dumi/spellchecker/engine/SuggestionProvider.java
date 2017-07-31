package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface SuggestionProvider
{
    List<String> getSuggestions(String text);
}
