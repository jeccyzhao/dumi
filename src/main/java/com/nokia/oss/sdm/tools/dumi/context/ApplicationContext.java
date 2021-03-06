package com.nokia.oss.sdm.tools.dumi.context;

import com.nokia.oss.sdm.tools.dumi.inspector.rule.FilterRule;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.FilterText;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.PlainTextFilterRule;
import com.nokia.oss.sdm.tools.dumi.inspector.rule.RegexPatternFilterRule;
import com.nokia.oss.sdm.tools.dumi.gui.DumiGui;
import com.nokia.oss.sdm.tools.dumi.gui.model.LogEntry;
import com.nokia.oss.sdm.tools.dumi.util.FileUtil;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.spelling.SpellingCheckRule;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Filter;

/**
 * Created by x36zhao on 2017/7/23.
 */
public class ApplicationContext
{
    public static ApplicationContext instance = new ApplicationContext();

    private static Options options;
    private Language lang = new BritishEnglish();
    private JLanguageTool langTool = new JLanguageTool(lang);
    private List<String> acceptedPhrases = new ArrayList<String>();
    private List<FilterRule> filterRules = new ArrayList<>();
    private ResourceBundle resource;

    private ApplicationContext ()
    {
        loadAssembly();
        loadRules();
        initLangTool(langTool);
    }

    private void loadRules ()
    {
        filterRules.add(new RegexPatternFilterRule());
        filterRules.add(new PlainTextFilterRule());
        for (FilterRule rule : filterRules)
        {
            rule.loadRule();
            rule.addRules(loadUserRules(rule.getClass() == RegexPatternFilterRule.class ?
                    Constants.USER_CONF_REGEX_PATTERN_FILE : Constants.USER_CONF_DICTIONARY_FILE));
        }
    }

    private List<FilterText> loadUserRules(String userRuleFile)
    {
        List<FilterText> filterTexts = new ArrayList<>();
        File ruleFile = new File(Constants.USER_CONF_FOLDER + "/" + userRuleFile);
        if (ruleFile.exists())
        {
            List<String> rules = FileUtil.getFileContent(ruleFile.getPath(), false);
            for (String rule : rules)
            {
                FilterText filterText = new FilterText();
                int sep = rule.lastIndexOf("#");
                if (sep > -1)
                {
                    filterText.setText(rule.substring(0, sep));
                    filterText.setRemark(rule.substring(sep + 1));
                }
                else
                {
                    filterText.setText(rule);
                }

                filterTexts.add(filterText);
            }
        }

        return filterTexts;
    }

    public static void Logger (final Logger logger, String message)
    {
        Logger(logger, message, null);
    }

    public static void Logger (final Logger logger, String message, Throwable e)
    {
        if (e != null)
        {
            logger.error(message, e);
        }
        else
        {
            logger.info(message);
        }

        if (isGuiModeRunning())
        {
            DumiGui.getController().logEvent(new LogEntry(e != null ? "ERROR" : "INFO", message));
        }
    }

    private static boolean isGuiModeRunning ()
    {
        return options != null && options.isGuiMode();
    }

    public void loadAssembly()
    {
        resource = ResourceBundle.getBundle("assembly");
    }

    public String getProperty (String propName)
    {
        return getProperty(propName, "");
    }

    public String getProperty (String propName, String defaultValue)
    {
        String propValue = resource.getString(propName);
        if (propValue != null)
        {
            return propValue;
        }

        return defaultValue;
    }

    public void addIgnoredWords (String file, boolean isBuiltIn)
    {
        this.acceptedPhrases.addAll(buildAcceptedPhrasesFromFile(file, isBuiltIn));
        addIgnoredWordsToLangTool(acceptedPhrases);
    }

    private void addIgnoredWordsToLangTool (List<String> acceptedPhrases)
    {
        addIgnoredWordsToLangTool(acceptedPhrases, langTool);
    }

    public void addIgnoredWordsToLangTool (List<String> acceptedPhrases, JLanguageTool languageTool)
    {
        if (acceptedPhrases != null && acceptedPhrases.size() > 0)
        {
            for (Rule rule : languageTool.getAllActiveRules())
            {
                if (rule instanceof SpellingCheckRule)
                {
                    ((SpellingCheckRule) rule).acceptPhrases(acceptedPhrases);
                    break;
                }
            }
        }
    }

    public void addIgnoredWordsToLangTool (JLanguageTool languageTool)
    {
        addIgnoredWordsToLangTool(this.acceptedPhrases, languageTool);
    }

    private List<String> buildAcceptedPhrasesFromFile (String file, boolean isBuiltIn)
    {
        List<String> ignoredWords = new ArrayList<String>();
        try
        {
            List<String> lines = FileUtil.getFileContent(file, isBuiltIn);
            for (String line : lines)
            {
                if (!ignoredWords.contains(line))
                {
                    ignoredWords.add(line);
                    ignoredWords.add(line.toLowerCase());
                }
            }
        }
        catch (Exception ex)
        {
            // do nothing
        }

        acceptedPhrases.addAll(ignoredWords);

        return ignoredWords;
    }

    public Options getOptions()
    {
        return options;
    }

    public void setOptions(Options options)
    {
        this.options = options;

        String userDictionary = this.options.getUserDictionary();
        if (userDictionary != null && !"".equals(userDictionary))
        {
            addIgnoredWords(userDictionary, false);
        }

        String regexPatternRuleFile = this.options.getRegexPatternRuleFile();
        if (regexPatternRuleFile != null && !"".equals(regexPatternRuleFile))
        {
            for (FilterRule filterRule : filterRules)
            {
                if (filterRule instanceof RegexPatternFilterRule)
                {
                    List<String> rules = FileUtil.getFileContent(regexPatternRuleFile);
                    for (String rule : rules)
                    {
                        filterRule.addRule(rule, null);
                    }
                }
            }
        }
    }

    public void initLangTool(JLanguageTool languageTool)
    {
        String disabeldRules = getProperty(Constants.propDisabedRules);
        if (disabeldRules != null && !"".equals(disabeldRules))
        {
            String[] ruleIds = disabeldRules.split(",");
            for (String ruleId : ruleIds)
            {
                languageTool.disableRule(ruleId);
            }
        }
    }

    public Language getLanguage()
    {
        return lang;
    }

    public JLanguageTool getLangTool ()
    {
        return langTool;
    }

    public static ApplicationContext getInstance()
    {
        return instance;
    }

    public List<FilterRule> getFilterRules()
    {
        return filterRules;
    }

    public List<String> getAcceptedPhrases()
    {
        return acceptedPhrases;
    }
}
