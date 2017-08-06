package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import com.nokia.oss.sdm.tools.dumi.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

/**
 * Created by x36zhao on 2017/8/2.
 */
public abstract class BaseFilterRule implements FilterRule
{
    protected List<FilterText> phrases = new ArrayList<>();
    protected String name;
    protected boolean loaded;

    @Override
    public List<FilterText> getAcceptedPhrases ()
    {
        return phrases;
    }

    @Override
    public void loadRule()
    {
        List<String> lines = FileUtil.getFileContent(getRuleName());
        if (lines != null && lines.size() > 0)
        {
            for (String line : lines)
            {
                FilterText filterText = new FilterText();
                int pos = line.lastIndexOf("#");
                if (pos > -1)
                {
                    filterText.setText(line.substring(0, pos));
                    filterText.setRemark(line.substring(pos + 1));
                }
                else
                {
                    filterText.setText(line);
                }

                filterText.setBuiltIn(true);
                phrases.add(filterText);
            }
        }
    }

    @Override
    public void removeRule (String text)
    {
        FilterText filterText = getByText(text);
        if (filterText != null)
        {
            phrases.remove(filterText);
        }
    }

    @Override
    public void addRule (String text, String remark)
    {
        boolean exists = getByText(text) != null;
        if (!exists)
        {
            phrases.add(new FilterText(text, remark));
        }
    }

    @Override
    public void addRule (FilterText filterText)
    {
        if (filterText != null)
        {
            addRule(filterText.getText(), filterText.getRemark());
        }
    }

    private FilterText getByText (String text)
    {
        if (phrases != null)
        {
            boolean exists = false;
            for (FilterText filterText : phrases)
            {
                if (filterText.getText().equals(text))
                {
                    return filterText;
                }
            }
        }

        return null;
    }
}
