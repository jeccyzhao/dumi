package com.nokia.oss.sdm.tools.dumi.report.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/22.
 */
@Data
public class TypoInspectionDataModel
{
    private String category;
    private String filePath;
    private String fileName;
    private boolean hasError;
    private List<Label> labels;

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
    public static class Label implements Comparator<Label>
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

        public boolean isHasError()
        {
            if (errorItems != null && errorItems.size() > 0)
            {
                return true;
            }

            return false;
        }

        public String getRawText()
        {
            if (errorItems.size() >  0)
            {
                for (int i = errorItems.size() - 1; i >= 0; i--)
                {
                    ErrorItem errorItem = errorItems.get(i);

                    String errorWord = rawText.substring(errorItem.getErrorStartPos(), errorItem.getErrorEndPos());
                    rawText = rawText.substring(0, errorItem.getErrorStartPos()) +
                            ("<b class='w-caution'>" + errorWord + "</b>") + rawText.substring(errorItem.getErrorEndPos());
                }
            }

            return rawText;
        }

        @Override
        public int compare (Label o1, Label o2)
        {
            return o1.getLabel().compareTo(o2.getLabel());
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
