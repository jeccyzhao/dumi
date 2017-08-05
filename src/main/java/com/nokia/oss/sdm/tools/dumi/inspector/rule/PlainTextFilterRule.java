package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import java.util.List;

/**
 * Created by x36zhao on 2017/8/2.
 */
public class PlainTextFilterRule extends BaseFilterRule
{
    private static final String FILE_RULE = "res/rules/rule_plainText.txt";

    @Override
    public String getRuleName ()
    {
        return FILE_RULE;
    }

//    @Override
//    public void loadRule()
//    {
//        List<String> lines = FileUtil.getFileContent(getRuleName());
//        if (lines != null && lines.size() > 0)
//        {
//            for (String line : lines)
//            {
//
//            }
//        }
//        for (String rule : res.rules)
//        {
//            phrases.add(rule.toLowerCase());
//            phrases.add(rule.toUpperCase());
//        }
//    }

    @Override
    public boolean isPhraseAccepted (String text)
    {
        return phrases.contains(text) || phrases.contains(text.toLowerCase());
    }

    @Override
    public List<String> filterText (String text)
    {
        return null;
    }
}
