package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import com.nokia.oss.sdm.tools.dumi.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x36zhao on 2017/8/2.
 */
public class RegexPatternFilterRule extends BaseFilterRule
{
    private static final String FILE_RULE = "rules/rule_regexPattern.txt";

    @Override
    public String getRuleName ()
    {
        return FILE_RULE;
    }

    @Override
    public boolean isPhraseAccepted (String text)
    {
        for (String regex : pharses)
        {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find())
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> filterText (String text)
    {
        List<String> filteredText = new ArrayList<>();
        for (String regex : pharses)
        {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find())
            {
                filteredText.add(matcher.group());
            }
        }

        return filteredText;
    }
}
