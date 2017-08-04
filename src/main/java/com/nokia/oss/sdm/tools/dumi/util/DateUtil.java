package com.nokia.oss.sdm.tools.dumi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by x36zhao on 2017/7/23.
 */
public class DateUtil
{
    private static String defaultDatePattern = "yyyyMMddHHmmss";
    private static SimpleDateFormat defaultFormatter = new SimpleDateFormat(defaultDatePattern);

    public static String parseTime(Date date)
    {
        return parseTime(date, defaultDatePattern);
    }

    public static String parseTime(Date date, String pattern)
    {
        return new SimpleDateFormat(pattern).format(date);
    }
}
