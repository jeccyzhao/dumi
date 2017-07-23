package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.report.TypoInspectionDataModel;
import static com.nokia.oss.sdm.tools.dumi.report.TypoInspectionDataModel.ErrorItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.language.English;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.UppercaseSentenceStartRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/21.
 */
public abstract class AbstractSpellingInspector
{
    protected static final Logger LOGGER = Logger.getLogger(AbstractSpellingInspector.class);

    public TypoInspectionDataModel process (String file)
    {
        LOGGER.info("Processing file " + file);
        try
        {
            return doProcess(file);
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to process file " + file, e);
            return null;
        }
    }

    public abstract TypoInspectionDataModel doProcess (String file) throws Exception;

    protected List<ErrorItem> inspectText (String text) throws IOException
    {
        if (text != null && !"".equals(text))
        {
            List<RuleMatch> matches = ApplicationContext.getInstance().getLangTool().check(text.toLowerCase());
            if (matches.size() > 0)
            {
                List<ErrorItem> potentialErrorItems = new ArrayList<ErrorItem>();
                for (RuleMatch match : matches)
                {
                    ErrorItem errorItem = buildErrorItem(match, text);
                    if (errorItem != null)
                    {
                        potentialErrorItems.add(errorItem);
                    }
                }
                return potentialErrorItems;
            }
        }

        return null;
    }

    private ErrorItem buildErrorItem (final RuleMatch ruleMatch, final String text)
    {
        if (ruleMatch != null)
        {
            int fromPos = ruleMatch.getFromPos();
            int toPos = ruleMatch.getToPos();
            String errorWord = text.substring(fromPos, toPos);

            boolean trackError = true;
            if (ruleMatch.getRule() instanceof UppercaseSentenceStartRule)
            {
                // Incorrect word with upper case, such as MEMRY will be ignored by language tool during checking
                // Workaround by converting the text to lower case for checking
                if (!errorWord.equals(errorWord.toLowerCase()))
                {
                    trackError = false;
                }
            }

            if (trackError)
            {
                ErrorItem errorItem = new ErrorItem();
                errorItem.setErrorStartPos(fromPos);
                errorItem.setErrorEndPos(toPos);
                errorItem.setErrorDescription(ruleMatch.getMessage());
                errorItem.setSuggestReplacements(StringUtils.join(ruleMatch.getSuggestedReplacements().toArray(), ","));
                errorItem.setErrorWord(errorWord);
                return errorItem;
            }
        }

        return null;
    }
}
