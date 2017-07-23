package com.nokia.oss.o2ml.metadata.fragment;

import lombok.Data;

import java.io.File;

/**
 * Created by x36zhao on 2017/7/21.
 */
@Data
public class BaseFragment
{
    protected String fileName;
    protected String adaptationId;
    protected String adaptationRelease;

    public String getId()
    {
        if (adaptationId != null && adaptationRelease != null)
        {
            return adaptationId + "-" + adaptationRelease;
        }

        int endPos = fileName.lastIndexOf(File.separator);
        return endPos > -1 ? fileName.substring(endPos) : fileName;
    }
}
