package com.nokia.oss.sdm.tools.dumi.inspector.rule;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

/**
 * Created by x36zhao on 2017/8/5.
 */
@Data
public class FilterText
{
    private String text;
    private String remark;
    private boolean isBuiltIn;

    private StringProperty textProperty;
    private StringProperty remarkProperty;

    public FilterText()
    {
    }

    public FilterText(String text)
    {
        this(text, null);
    }

    public FilterText(String text, String remark)
    {
        this.text = text;
        this.remark = remark;
    }

    public StringProperty getTextProperty()
    {
        if (textProperty == null)
        {
            textProperty = new SimpleStringProperty(text);
        }

        return textProperty;
    }

    public StringProperty getRemarkProperty()
    {
        if (remarkProperty == null)
        {
            remarkProperty = new SimpleStringProperty(remark);
        }

        return remarkProperty;
    }
}
