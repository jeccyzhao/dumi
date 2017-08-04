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
}
