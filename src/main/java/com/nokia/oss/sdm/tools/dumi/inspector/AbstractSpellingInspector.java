package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel;
import static com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel.ErrorItem;

import com.nokia.oss.sdm.tools.dumi.spellchecker.SpellCheckerManager;
import com.nokia.oss.sdm.tools.dumi.spellchecker.inspections.PlainTextSplitter;
import com.nokia.oss.sdm.tools.dumi.spellchecker.jazzy.JazzySpellChecker;
import com.nokia.oss.sdm.tools.dumi.spellchecker.util.TextRange;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.UppercaseSentenceStartRule;

import javax.xml.soap.Text;
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
        LOGGER.info("Processing file '" + file + "'");
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

    private String transform (String text)
    {
        String textSplitter = ApplicationContext.getInstance().getOptions().getPlainTextSplitter();
        if (textSplitter != null && !"".equals(text))
        {
            String[] entries = textSplitter.split(",");
            if (entries.length > 1)
            {
                try
                {
                    String splitter = escapeString(entries[0]);
                    int splitIndex = Integer.valueOf(entries[1]);
                    String[] strs = text.split(splitter);
                    if (strs.length > splitIndex)
                    {
                        return strs[splitIndex];
                    }
                }
                catch (Exception e)
                {
                }
            }
        }

        return text;
    }

    private String escapeString (String text)
    {
        text = text.indexOf("|") > -1 ? text.replaceAll("\\|", "\\\\|") : text;
        text = text.indexOf(".") > -1 ? text.replaceAll("\\.", "\\\\.") : text;
        text = text.indexOf("*") > -1 ? text.replaceAll("\\*", "\\\\*") : text;
        text = text.indexOf("?") > -1 ? text.replaceAll("\\?", "\\\\?") : text;
        text = text.indexOf("^") > -1 ? text.replaceAll("\\^", "\\\\^") : text;
        text = text.indexOf(":") > -1 ? text.replaceAll("\\:", "\\\\:") : text;
        return text;
    }

    protected List<ErrorItem> inspectText (String text) throws IOException
    {
        return inspectText(text, new JLanguageTool(ApplicationContext.getInstance().getLanguage()), false);
    }

    protected List<ErrorItem> inspectText (String text, JLanguageTool languageTool, boolean transform) throws IOException
    {
        if (text != null && !"".equals(text))
        {
            String transformedText = transform ? transform(text) : text;
            List<RuleMatch> matches = languageTool.check(transformedText.toLowerCase());
            if (matches.size() > 0)
            {
                List<ErrorItem> potentialErrorItems = new ArrayList<ErrorItem>();
                for (RuleMatch match : matches)
                {
                    ErrorItem errorItem = buildErrorItem(match, text, transformedText);
                    if (errorItem != null)
                    {
                        potentialErrorItems.add(errorItem);
                    }
                }
                matches.clear();
                return potentialErrorItems;
            }
        }

        return null;
    }

    private ErrorItem buildErrorItem (final RuleMatch ruleMatch, final String rawText, final String transformedText)
    {
        if (ruleMatch != null)
        {
            int fromPos = ruleMatch.getFromPos();
            int toPos = ruleMatch.getToPos();
            String errorWord = transformedText.substring(fromPos, toPos);

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

                if (transformedText.equals(rawText))
                {
                    errorItem.setErrorStartPos(fromPos);
                    errorItem.setErrorEndPos(toPos);
                }
                else
                {
                    int prefixLen = rawText.indexOf(transformedText);
                    errorItem.setErrorStartPos(fromPos + prefixLen);
                    errorItem.setErrorEndPos(toPos + prefixLen);
                }

                errorItem.setErrorDescription(ruleMatch.getMessage());
                errorItem.setSuggestReplacements(StringUtils.join(ruleMatch.getSuggestedReplacements().toArray(), ","));
                errorItem.setErrorWord(errorWord);
                return errorItem;
            }
        }

        return null;
    }
}
