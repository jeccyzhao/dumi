package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel.Label;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class PlainTextSpellingInspectTask implements Callable<List<Label>>
{
    protected static final Logger LOGGER = Logger.getLogger(PlainTextSpellingInspectTask.class);

    private final PlainTextSpellingInspector inspector;
    private final List<String> lines;
    private int start;
    private int end;

    public PlainTextSpellingInspectTask (PlainTextSpellingInspector inspector, List<String> lines, int start, int end)
    {
        this.inspector = inspector;
        this.lines = lines;
        this.start = start;
        this.end = end;

        System.out.println("Processing lines from " + start + " to " + end);
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public List<Label> call () throws Exception
    {
        List<Label> labels = new ArrayList<>();
        for (int i = start; i < end; i++)
        {
            String text = lines.get(i);
            if (text != null && !"".equals(text))
            {
                LOGGER.info("Processing line" + i + " - '" + text.substring(0, text.length() > 100 ? 100 : text.length() - 1) + "...'");
                labels.add(inspector.checkLine(i, text));
            }
        }

        return labels;
    }
}
