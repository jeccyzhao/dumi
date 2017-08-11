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
    List<FilterText> getAcceptedPhrases();
    boolean isPhraseAccepted (String text);
    void addRule (String text, String remark);
    void addRule (FilterText filterText);
    void addRules (List<FilterText> filterTexts);
    void removeRule (String text);
    List<String> filterText (String text);
}
