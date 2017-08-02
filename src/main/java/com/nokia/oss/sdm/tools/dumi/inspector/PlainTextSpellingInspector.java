package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
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
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        List<Future<List<Label>>> executionFeedbacks = new ArrayList<>();
        for (int i = 0; i < threadNum; i++)
        {
            int start = i * linePerThread;
            int end = (i < threadNum - 1) ? (i * linePerThread + linePerThread) : lines.size();
            executionFeedbacks.add(exec.submit(new PlainTextSpellingInspectTask(this, lines, start, end)));
        }

        for (Future<List<Label>> feedback : executionFeedbacks)
        {
            try
            {
                dataModel.getLabels().addAll(feedback.get());
            }
            catch (Exception e)
            {
            }
        }

        exec.shutdown();

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
        ApplicationContext.getInstance().addIgnoredWordsToLangTool(languageTool);
        return checkLine(lineNum, line, languageTool, transform);
    }

    public class InspectTask extends RecursiveTask<List<Label>>
    {
        private final PlainTextSpellingInspector inspector;
        private final List<String> lines;
        private JLanguageTool lanTool = new JLanguageTool(ApplicationContext.getInstance().getLanguage());
        private int threshold;
        private int start;
        private int end;

        public InspectTask(PlainTextSpellingInspector inspector, List<String> lines, int threshold)
        {
            this(inspector, lines, threshold, 0, lines.size());
        }

        public InspectTask(PlainTextSpellingInspector inspector, List<String> lines, int threshold, int start, int end)
        {
            this.inspector = inspector;
            this.lines = lines;
            this.start = start;
            this.end = end;
            this.threshold = threshold;

            System.out.println("Forking from " + start + " to " + end);
        }

        /**
         * The main computation performed by this task.
         *
         * @return the result of the computation
         */
        @Override
        protected List<Label> compute ()
        {
            List<Label> labels = new ArrayList<>();
            boolean fork = (end - start) > threshold;
            if (fork)
            {

                int middle = (start + end) / 2;
                InspectTask lTask = new InspectTask(inspector, lines, threshold, start, middle);
                InspectTask rTask = new InspectTask(inspector, lines, threshold, middle + 1, end);

                invokeAll(lTask, rTask);
                //lTask.fork();
                //rTask.fork();

                labels.addAll(lTask.join());
                labels.addAll(rTask.join());
            }
            else
            {
                for (int i = start; i < end; i++)
                {
                    String line = lines.get(i);
                    if (line != null && !"".equals(line))
                    {
                        System.out.println("Processing line-" + i); //": " + lines.get(i));
                        labels.add(inspector.checkLine(i, line, lanTool, true));
                    }
                }
            }

            return labels;
        }
    }
}
