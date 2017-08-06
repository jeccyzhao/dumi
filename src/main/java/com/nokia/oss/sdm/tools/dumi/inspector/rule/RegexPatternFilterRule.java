package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x36zhao on 2017/8/2.
 */
public class RegexPatternFilterRule extends BaseFilterRule
{
    private static final String FILE_RULE = "res/rules/rule_regexPattern.txt";

    @Override
    public String getRuleName ()
    {
        return FILE_RULE;
    }

    @Override
    public boolean isPhraseAccepted (String text)
    {
        for (FilterText filterText : phrases)
        {
            String regex = filterText.getText();
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
        for (FilterText filterText : phrases)
        {
            String regex = filterText.getText();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find())
            {
                String matchedText = matcher.group();
                filteredText.add(matchedText);

                text = text.replace(matchedText, "#placeholder#");
            }
        }

        return filteredText;
    }
}
