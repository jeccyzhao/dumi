package com.nokia.oss.sdm.tools.dumi.spellchecker;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class SpellCheckerManagerTest
{
    public static void main (String[] args)
    {
        SpellCheckerManager manager = SpellCheckerManager.getInstance();
        System.out.println(manager.hasProblem("This is a sample test string with no misspellings"));
    }
}
