package com.nokia.oss.sdm.tools.dumi.report.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by x36zhao on 2017/7/23.
 */
@Data
public class Environment
{
    private String title;
    private String version;
    private String scanningPath;
    private Date timestamp;
}
