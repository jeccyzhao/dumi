package com.nokia.oss.sdm.tools.dumi.gui.model;

import com.nokia.oss.sdm.tools.dumi.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by x36zhao on 2017/8/3.
 */
@Data
public class LogEntry
{
    private StringProperty timestamp;
    private StringProperty level;
    private StringProperty text;

    public LogEntry(String level, String text)
    {
        this(DateUtil.parseTime(new Date(), "MM-dd HH:mm:ss.S"), level, text);
    }

    public LogEntry(String timestamp, String level, String text)
    {
        this.timestamp = new SimpleStringProperty(timestamp);
        this.level = new SimpleStringProperty(level);
        this.text = new SimpleStringProperty(text);
    }
}
