package com.nokia.oss.sdm.tools.dumi.context;

import com.nokia.oss.sdm.tools.dumi.util.FileUtil;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.spelling.SpellingCheckRule;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by x36zhao on 2017/7/23.
 */
public class ApplicationContext
{
    public static ApplicationContext instance = new ApplicationContext();

    private Language lang = new BritishEnglish();
    private JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
    private List<String> acceptedPhrases = new ArrayList<String>();

    private ResourceBundle resource;

    private ApplicationContext ()
    {
        loadAssembly();
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

    public void loadAssembly()
    {
        resource = ResourceBundle.getBundle("assembly");

        addIgnoredWords(resource.getString(Constants.propIgnoredBuiltInWords), true);
    }

    public String getProperty (String propName)
    {
        return resource.getString(propName);
    }

    public void addIgnoredWords (String file, boolean isBuiltIn)
    {
        List<String> ignoredWords = buildAcceptedPhrasesFromFile(file, isBuiltIn);
        addIgnoredWordsToLangTool(ignoredWords);
    }

    public List<String> getAcceptedPhrases()
    {
        return acceptedPhrases;
    }

    private void addIgnoredWordsToLangTool (List<String> acceptedPhrases)
    {
        if (acceptedPhrases != null && acceptedPhrases.size() > 0)
        {
            for (Rule rule : langTool.getAllActiveRules())
            {
                if (rule instanceof SpellingCheckRule)
                {
                    ((SpellingCheckRule) rule).acceptPhrases(acceptedPhrases);
                    break;
                }
            }
        }
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
}
