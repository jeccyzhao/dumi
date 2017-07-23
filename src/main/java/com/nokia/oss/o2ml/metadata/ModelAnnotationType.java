package com.nokia.oss.o2ml.metadata;

/**
 * Annotation type definition
 *
 * E.g.
 *  <annotations>
 *    ...
 <type href="com.nsn.nthlrfe.common#//CAFComponentInfo"/>
 </annotations>
 *
 *
 * Created by x36zhao on 2017/7/21.
 */
public class ModelAnnotationType
{
    /**
     * annotation type root class
     */
    private String href;

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }
}
