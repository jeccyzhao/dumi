package com.nokia.oss.sdm.tools.dumi;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Constants;
import com.nokia.oss.sdm.tools.dumi.inspector.SpellingInspectorFactory;
import com.nokia.oss.sdm.tools.dumi.report.Environment;
import com.nokia.oss.sdm.tools.dumi.report.ReportBuilder;
import com.nokia.oss.sdm.tools.dumi.report.TypoInspectionDataModel;
import com.nokia.oss.sdm.tools.dumi.inspector.AbstractSpellingInspector;
import com.nokia.oss.sdm.tools.dumi.report.ReportStatistics;
import com.nokia.oss.sdm.tools.dumi.util.FileUtil;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import opennlp.tools.parser.Cons;

import java.io.IOException;
import java.util.*;

/**
 * Created by x36zhao on 2017/7/21.
 */
public class SpellChecker
{
    public static void printHelp ()
    {
        System.out.println("java -jar xxx.jar <scanningFolder> [ignoredWordFile]");
    }

    public static void main (String[] args) throws IOException
    {
        if (args.length < 1)
        {
            printHelp();
        }

        String scanFolder = args[0]; //"D:\\Workspace\\SVN\\adaptations\\com.nsn.hp.hphw";
        String customizedIgnoredWordsFile = null;
        if (args.length > 1)
        {
            customizedIgnoredWordsFile = args[1];
            ApplicationContext.getInstance().addIgnoredWords(customizedIgnoredWordsFile, false);
        }

        List<String> filesToProcess = FileUtil.listFiles(scanFolder, new String[] {".man"});

        Map<String, ReportStatistics> dataMap = null;
        if (filesToProcess.size() > 0)
        {
            dataMap = new HashMap<String, ReportStatistics>();
            for (String file : filesToProcess)
            {
                AbstractSpellingInspector inspector = SpellingInspectorFactory.createSpellingChecker(file);
                if (inspector != null)
                {
                    TypoInspectionDataModel rdm = inspector.process(file);
                    if (rdm != null)
                    {
                        if (!dataMap.containsKey(rdm.getCategory()))
                        {
                            dataMap.put(rdm.getCategory(), new ReportStatistics());
                        }

                        ReportStatistics statistics = dataMap.get(rdm.getCategory());
                        statistics.setTotalNum(statistics.getTotalNum() + 1);

                        if (rdm.isHasError())
                        {
                            statistics.setFailureNum(statistics.getFailureNum() + 1);
                        }

                        statistics.getData().add(rdm);
                    }
                }
                else
                {
                    // no built-in inspector found
                }
            }
        }

        ReportBuilder reportBuilder = new ReportBuilder();
        Map<String, Object> dataModel = new HashMap<String, Object>();

        Environment env = new Environment();
        env.setTitle(ApplicationContext.getInstance().getProperty(Constants.propToolTitle));
        env.setVersion(ApplicationContext.getInstance().getProperty(Constants.propToolVer));
        env.setScanningPath(scanFolder);

        dataModel.put("env", env);
        dataModel.put("dataMap", dataMap);

        reportBuilder.buildReport("report.tpl", dataModel);
    }
}
