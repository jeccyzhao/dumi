package com.nokia.oss.sdm.tools.dumi.util;

/**
 * Created by x36zhao on 2017/8/3.
 */
public class StringUtil
{
    public static String join (String[] strs, String joiner)
    {
        StringBuilder builder = new StringBuilder();
        if (strs != null && strs.length > 0)
        {
            for (String str : strs)
            {
                builder.append(str);
                builder.append(joiner);
            }
        }

        return builder.toString();
    }

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
