package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel;
import com.nokia.oss.sdm.tools.dumi.util.FileUtil;
import org.apache.log4j.Logger;

import static com.nokia.oss.sdm.tools.dumi.report.model.TypoInspectionDataModel.Label;

import java.util.List;

/**
 * Created by x36zhao on 2017/7/23.
 */
public class PlainTextSpellingInspector extends AbstractSpellingInspector
{
    protected static final Logger LOGGER = Logger.getLogger(PlainTextSpellingInspector.class);

    public TypoInspectionDataModel doProcess (String file) throws Exception
    {
        TypoInspectionDataModel dataModel = new TypoInspectionDataModel(file);
        List<String> lines = FileUtil.getFileContent(file, false);
        int lineNum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < lines.size(); i++)
        {
            System.out.println("Processing line-" + lineNum);
            Label label = checkLine(i, lines.get(i));
            dataModel.addLabel(label);
            lineNum++;
        }

        System.out.println(System.nanoTime() - start + " ns");

        dataModel.setCategory(FileUtil.getFileName(file));
        return dataModel;
    }

    private Label checkLine (int lineNum, String line)
    {
        Label labelEntry = new Label(String.valueOf(lineNum), line);
        try
        {
            List<TypoInspectionDataModel.ErrorItem> errorItems = inspectText(line);
            if (errorItems != null && errorItems.size() > 0)
            {
                labelEntry.setErrorItems(errorItems);
            }
        }
        catch (Exception e)
        {
            LOGGER.warn("Failed to process L" + lineNum + ": " + line, e);
        }

        return labelEntry;
    }
}
