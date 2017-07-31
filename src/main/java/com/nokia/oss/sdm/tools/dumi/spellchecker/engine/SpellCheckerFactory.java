package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

/**
 * Created by x36zhao on 2017/7/31.
 */
public final class SpellCheckerFactory
{
    private SpellCheckerFactory()
    {
    }

    public static SpellCheckerEngine create()
    {
        return new BaseSpellChecker();
    }
}
