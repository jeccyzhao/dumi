package com.nokia.oss.sdm.tools.dumi.spellchecker.bundle;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class DefaultBundledDictionariesProvider implements BundledDictionaryProvider
{
    private static final String DICT_FOLDER = "dict/";

    @Override
    public String[] getBundledDictionaries ()
    {
        return getDictionaryFile("english.dic", "english.dic", "jetbrains.dic", "oss.dic");
    }

    private static String[] getDictionaryFile (String... files)
    {
        int len = files.length;
        String[] dictFile = new String[len];
        for (int i = 0; i < len; i++)
        {
            dictFile[i] = DICT_FOLDER + files[i];
        }

        return dictFile;
    }
}
