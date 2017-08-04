package com.nokia.oss.sdm.tools.dumi.inspector.splitter;

import com.nokia.oss.sdm.tools.dumi.util.TextRange;

import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface Splitter
{
    List<String> split (String text, TextRange range);
}
