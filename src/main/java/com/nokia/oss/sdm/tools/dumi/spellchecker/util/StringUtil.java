package com.nokia.oss.sdm.tools.dumi.spellchecker.util;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class StringUtil
{
    public static boolean isEmpty(final String s)
    {
        return s == null || s.length() == 0;
    }

    public static String capitalize(String s)
    {
        if (s.isEmpty()) return s;
        if (s.length() == 1) return s.toUpperCase();

        // Optimization
        if (Character.isUpperCase(s.charAt(0))) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
