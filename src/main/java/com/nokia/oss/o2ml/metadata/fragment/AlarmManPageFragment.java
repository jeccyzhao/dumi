package com.nokia.oss.o2ml.metadata.fragment;

import com.nokia.oss.sdm.tools.dumi.annotation.TypoInspection;
import lombok.Data;

/**
 * Created by x36zhao on 2017/7/21.
 */
@Data
public class AlarmManPageFragment extends BaseFragment
{
    private String patchLevel;
    private String specificProblem;

    @TypoInspection
    private String alarmText;
    private String alarmType;

    @TypoInspection
    private String meaning;

    @TypoInspection
    private String instructions;

    @TypoInspection
    private String cancelling;

    private String perceivedSeverityInfo;
}
