package com.nokia.oss.sdm.tools.dumi.spellchecker.inspections;

import com.nokia.oss.sdm.tools.dumi.spellchecker.util.TextRange;

import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface Splitter
{
    void split(String text, TextRange range, List<String> splitWords);
}
