package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.FilterRule;
import com.nokia.oss.sdm.tools.dumi.inspector.splitter.IdentifierSplitter;
import com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel;
import static com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel.ErrorItem;

import com.nokia.oss.sdm.tools.dumi.spellchecker.util.TextRange;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.languagetool.JLanguageTool;
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
    private static final Logger LOGGER = Logger.getLogger(AbstractSpellingInspector.class);

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
        return inspectText(text, new JLanguageTool(ApplicationContext.getInstance().getLanguage()), false, 0);
    }

    protected List<ErrorItem> inspectText (String rawText, final JLanguageTool languageTool, boolean transform, int fromPos) throws IOException
    {
        List<ErrorItem> errorItems = new ArrayList<>();
        if (rawText != null && !"".equals(rawText))
        {
            String transformedText = transform ? transform(rawText) : rawText;
            List<RuleMatch> matches = languageTool.check(transformedText.toLowerCase());
            if (matches.size() > 0)
            {
                for (RuleMatch match : matches)
                {
                    List<ErrorItem> items = buildErrorItems(match, rawText, transformedText, languageTool, fromPos);
                    if (items != null)
                    {
                        errorItems.addAll(items);
                    }
                }
            }
        }
        return errorItems;
    }

    private ErrorItem makeErrorItem (final RuleMatch ruleMatch, String errorWord, String rawText, String transformedText, int from)
    {
        for (FilterRule rule : ApplicationContext.getInstance().getFilterRules())
        {
            if (rule.isPhraseAccepted(errorWord))
            {
                return null;
            }
        }

        ErrorItem errorItem = null;
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

        int fromPos = ruleMatch.getFromPos();
        int toPos = ruleMatch.getToPos();

        if (trackError)
        {
            errorItem = new ErrorItem();
            int prefixLen = rawText.indexOf(transformedText);
            errorItem.setErrorStartPos(fromPos + prefixLen + from);
            errorItem.setErrorEndPos(toPos + prefixLen + from);
            errorItem.setErrorDescription(ruleMatch.getMessage());
            errorItem.setSuggestReplacements(StringUtils.join(ruleMatch.getSuggestedReplacements().toArray(), ","));
            errorItem.setErrorWord(errorWord);
        }

        return errorItem;
    }

    private List<ErrorItem> buildErrorItems (final RuleMatch ruleMatch, String rawText,
                                             String transformedText, final JLanguageTool languageTool, int from)
    {
        List<ErrorItem> errorItems = new ArrayList<>();
        if (ruleMatch != null)
        {
            int fromPos = ruleMatch.getFromPos();
            int toPos = ruleMatch.getToPos();
            String errorWord = transformedText.substring(fromPos, toPos);

            List<String> identifiers = IdentifierSplitter.getInstance().split(errorWord, TextRange.allOf(errorWord));
            if (identifiers.size() > 1)
            {
                for (String identifier : identifiers)
                {
                    try
                    {
                        errorItems.addAll(inspectText(identifier, languageTool, false,
                                fromPos + errorWord.indexOf(identifier) + rawText.indexOf(transformedText)));
                    }
                    catch (IOException e)
                    {
                        LOGGER.error("Failed to process word '" + identifier + "'", e);
                    }
                }
            }
            else
            {
                ErrorItem errorItem = makeErrorItem(ruleMatch, errorWord, rawText, transformedText, from);
                if (errorItem != null)
                {
                    errorItems.add(errorItem);
                }
            }
        }

        return errorItems;
    }
}
