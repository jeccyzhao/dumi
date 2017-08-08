package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Constants;
import com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel;
import com.nokia.oss.sdm.tools.dumi.util.FileUtil;
import org.apache.log4j.Logger;
import org.languagetool.JLanguageTool;

import static com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by x36zhao on 2017/7/23.
 */
public class PlainTextSpellingInspector extends AbstractSpellingInspector
{
    private static final Logger LOGGER = Logger.getLogger(PlainTextSpellingInspector.class);

    public TypoInspectionDataModel doProcess (String file) throws Exception
    {
        int threadThreshold = ApplicationContext.getInstance().getOptions().getThreadThreshold();
        TypoInspectionDataModel dataModel = new TypoInspectionDataModel(file);
        dataModel.setPlainText(true);

        List<String> lines = FileUtil.getFileContent(file, false);
        long startTime = System.currentTimeMillis();
        int seed = lines.size() / threadThreshold;
        int linePerThread = seed > 0 ? seed : -1;
        int threadNum = linePerThread > 0 ? threadThreshold : 1;

        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        List<Future<List<Label>>> futures = new ArrayList<>();
        for (int i = 0; i < threadNum; i++)
        {
            int start = i * linePerThread;
            int end = (i < threadNum - 1) ? (i * linePerThread + linePerThread) : lines.size();

            PlainTextSpellingInspectTask task = new PlainTextSpellingInspectTask(this, lines, start, end);
            futures.add(executorService.submit(task));
            addTask(task);
        }

        for (Future<List<Label>> feedback : futures)
        {
            try
            {
                if (feedback != null)
                {
                    dataModel.getLabels().addAll(feedback.get());
                }
            }
            catch (Exception e)
            {
                LOGGER.error("Execution fails due to " + e.getMessage(), e);
            }
        }

        executorService.shutdown();

        dataModel.setCategory(FileUtil.getFileName(file));

        LOGGER.info("File processed successfully within " + (System.currentTimeMillis() - startTime) / 1000 + " s");
        return dataModel;
    }

    private Label checkLine (int lineNum, String line, JLanguageTool lanTool, boolean transform)
    {
        Label labelEntry = new Label(String.valueOf(lineNum), line);
        try
        {
            List<TypoInspectionDataModel.ErrorItem> errorItems = inspectText(line, lanTool, transform, 0);
            if (errorItems != null && errorItems.size() > 0)
            {
                labelEntry.setErrorItems(errorItems);
                labelEntry.setHasError(true);
            }
        }
        catch (Exception e)
        {
            LOGGER.warn("Failed to process L-" + lineNum + ": " + line, e);
        }

        return labelEntry;
    }

    public Label checkLine (int lineNum, String line)
    {
        return checkLine(lineNum, line, false);
    }

    public Label checkLine (int lineNum, String line, boolean transform)
    {
        JLanguageTool languageTool = new JLanguageTool(ApplicationContext.getInstance().getLanguage());
        ApplicationContext.getInstance().initLangTool(languageTool);
        ApplicationContext.getInstance().addIgnoredWordsToLangTool(languageTool);
        return checkLine(lineNum, line, languageTool, transform);
    }
}
