package com.nokia.oss.sdm.tools.dumi.report.model;

import lombok.Data;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Created by x36zhao on 2017/7/22.
 */
@Data
public class TypoInspectionDataModel
{
    private static Logger LOGGER = Logger.getLogger(TypoInspectionDataModel.class);

    private String category;
    private String filePath;
    private String fileName;
    private boolean isPlainText = false;
    private boolean hasError;
    private List<Label> labels = new ArrayList<>();

    public TypoInspectionDataModel (String filePath)
    {
        this.filePath = filePath;

        File file = new File(filePath);
        if (file.exists())
        {
            fileName = file.getName();
        }
    }

    public void addLabel (Label label)
    {
        if (labels == null)
        {
            labels = new ArrayList<Label>();
        }

        labels.add(label);
    }

    public boolean isHasError()
    {
        if (labels != null && labels.size() > 0)
        {
            for (Label label : labels)
            {
                if (label.isHasError())
                {
                    return true;
                }
            }
        }

        return false;
    }


    @Data
    public static class Label implements Comparable<Label>
    {
        private String label;
        private String rawText;
        private boolean hasError = false;
        private List<ErrorItem> errorItems = new ArrayList<ErrorItem>();

        public Label ()
        {
        }

        public Label (String label, String rawText)
        {
            this.label = label;
            this.rawText = rawText;
        }

        public boolean isHasError ()
        {
            if (errorItems != null && errorItems.size() > 0)
            {
                return true;
            }

            return false;
        }

        public String getRawText ()
        {
            String tempText = rawText;
            try
            {
                if (errorItems.size() > 0)
                {
                    for (int i = errorItems.size() - 1; i >= 0; i--)
                    {
                        ErrorItem errorItem = errorItems.get(i);
                        String errorWord = rawText.substring(errorItem.getErrorStartPos(), errorItem.getErrorEndPos());
                        tempText = tempText.substring(0, errorItem.getErrorStartPos()) +
                                ("<b class='w-caution'>" + errorWord + "</b>") + tempText.substring(errorItem.getErrorEndPos());
                    }
                }
            }
            catch (Exception e)
            {
                LOGGER.error("Failed to get raw text (" + rawText + "), errorItems (" +errorItems + ")");
            }

            return tempText;
        }

        @Override
        public int compareTo (Label o)
        {
            try
            {
                return Integer.valueOf(this.getLabel()) - Integer.valueOf(o.getLabel());
            }
            catch (NumberFormatException ex)
            {
                return this.getLabel().compareTo(o.getLabel());
            }
        }
    }

    @Data
    public static class ErrorItem
    {
        private String errorWord;
        private int errorStartPos;
        private int errorEndPos;
        private String errorDescription;
        private String suggestReplacements;
    }
}
