package com.nokia.oss.sdm.tools.dumi.report.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/23.
 */
@Data
public class ReportStatistics
{
    private int totalNum;
    private int failureNum;

    private List<TypoInspectionDataModel> data = new ArrayList<TypoInspectionDataModel>();
}
