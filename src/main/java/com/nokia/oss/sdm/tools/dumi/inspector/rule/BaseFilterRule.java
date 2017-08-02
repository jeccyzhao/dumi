package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import com.nokia.oss.sdm.tools.dumi.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/8/2.
 */
public abstract class BaseFilterRule implements FilterRule
{
    protected List<String> pharses = new ArrayList<>();
    protected String name;
    protected boolean loaded;

    @Override
    public List<String> getAcceptedPharases ()
    {
        return pharses;
    }

    @Override
    public void loadRule()
    {
        pharses = FileUtil.getFileContent(getRuleName());
    }

    @Override
    public void addRule (String text)
    {
        if (pharses != null && !pharses.contains(text))
        {
            pharses.add(text);
        }
    }
}
