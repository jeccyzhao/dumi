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

    protected List<ErrorItem> inspectText (String text) throws IOException
    {
        return inspectText(text, new JLanguageTool(ApplicationContext.getInstance().getLanguage()));
    }

    protected List<ErrorItem> inspectText (String text, JLanguageTool languageTool) throws IOException
    {
        if (text != null && !"".equals(text))
        {
            List<RuleMatch> matches = languageTool.check(text.toLowerCase());
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
                matches.clear();
                return potentialErrorItems;
            }

            //JazzySpellChecker spellChecker = new JazzySpellChecker();
            //spellChecker.check(text);
            //System.out.println(spellChecker.check(text));

            /*
            System.out.println(text);
            List<String> words = new ArrayList<>();
            PlainTextSplitter.getInstance().split(text, TextRange.allOf(text), words);
            for (String word : words)
            {
                List<String> suggestions = SpellCheckerManager.getInstance().getSuggestions(word);
                if (suggestions != null && suggestions.get(0) != null)
                {
                    System.out.println(word + " --> " + suggestions);
                }
            }
            */
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
