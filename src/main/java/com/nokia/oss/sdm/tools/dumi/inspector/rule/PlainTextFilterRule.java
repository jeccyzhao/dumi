package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import com.nokia.oss.sdm.tools.dumi.util.FileUtil;

import java.util.List;

/**
 * Created by x36zhao on 2017/8/2.
 */
public class PlainTextFilterRule extends BaseFilterRule
{
    private static final String FILE_RULE = "rules/rule_plainText.txt";

    @Override
    public String getRuleName ()
    {
        return FILE_RULE;
    }

    @Override
    public boolean isPhraseAccepted (String text)
    {
        return pharses.contains(text);
    }
}
