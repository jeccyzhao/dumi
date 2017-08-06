package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Constants;
import com.nokia.oss.sdm.tools.dumi.report.model.Environment;
import com.nokia.oss.sdm.tools.dumi.report.ReportBuilder;
import com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel;
import com.nokia.oss.sdm.tools.dumi.report.model.ReportStatistics;
import com.nokia.oss.sdm.tools.dumi.util.FileUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by x36zhao on 2017/7/21.
 */
public class InspectionProcessor
{
    private static final String REPORT_TEMPLATE_FILE = "report.tpl";
    private static final String[] acceptedFileExts = new String[] {".man", ".log"};
    private static final Logger LOGGER = Logger.getLogger(InspectionProcessor.class);

    private List<AbstractSpellingInspector> inspectors = new ArrayList<>();
    private boolean isCancelled = false;
    private Map<String, ReportStatistics> data;

    public void stop ()
    {
        for (AbstractSpellingInspector inspector : inspectors)
        {
            inspector.stopTasks();
        }

        isCancelled = true;
    }

    public void inspect (String folder, String userDictionaryFile)
    {
        if (userDictionaryFile != null && !"".equals(userDictionaryFile))
        {
            ApplicationContext.getInstance().addIgnoredWords(userDictionaryFile, false);
        }

        List<String> filesToInspect = FileUtil.listFiles(folder, acceptedFileExts);
        if (filesToInspect.size() > 0)
        {
            data = new HashMap<String, ReportStatistics>();
            for (String file : filesToInspect)
            {
                if (!isCancelled)
                {
                    inspectFile(file);
                }
                else
                {
                    ApplicationContext.Logger(LOGGER, "Inspection is cancelled");
                    break;
                }
            }

            createReport(folder);
        }
        else
        {
            LOGGER.warn("No matched files to inspect");
        }
    }

    private void createReport (String scanFolder)
    {
        new ReportBuilder().buildReport(REPORT_TEMPLATE_FILE, constructReportDataModel(scanFolder));
    }

    private Map<String, Object> constructReportDataModel (String scanFolder)
    {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("env", constructEnvironment(scanFolder));
        dataModel.put("dataMap", data);
        return dataModel;
    }

    private Environment constructEnvironment (String scanFolder)
    {
        Environment env = new Environment();
        env.setTitle(ApplicationContext.getInstance().getProperty(Constants.propToolTitle));
        env.setVersion(ApplicationContext.getInstance().getProperty(Constants.propToolVer));
        env.setScanningPath(scanFolder);
        return env;
    }

    private void inspectFile (String file)
    {
        AbstractSpellingInspector inspector = SpellingInspectorFactory.createSpellingChecker(file);
        if (inspector != null)
        {
            inspectors.add(inspector);
            TypoInspectionDataModel rdm = inspector.process(file);
            if (rdm != null)
            {
                if (!data.containsKey(rdm.getCategory()))
                {
                    data.put(rdm.getCategory(), new ReportStatistics());
                }

                ReportStatistics statistics = data.get(rdm.getCategory());
                statistics.setTotalNum(statistics.getTotalNum() + 1);
                if (rdm.isHasError())
                {
                    statistics.setFailureNum(statistics.getFailureNum() + 1);
                }

                statistics.getData().add(rdm);

                data.put(rdm.getCategory(), statistics);
            }
        }
    }
}
