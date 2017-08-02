package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import com.nokia.oss.sdm.tools.dumi.util.FileUtil;

import java.util.List;

/**
 * Created by x36zhao on 2017/8/2.
 */
public interface FilterRule
{
    void loadRule();
    String getRuleName();
    List<String> getAcceptedPharases();
    boolean isPhraseAccepted (String text);
    void addRule (String text);
}
