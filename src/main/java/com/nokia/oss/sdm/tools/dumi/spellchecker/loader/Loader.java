package com.nokia.oss.sdm.tools.dumi.spellchecker.loader;

import com.nokia.oss.sdm.tools.dumi.spellchecker.dict.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public interface Loader
{
    List<String> load();
    String getName();
}
