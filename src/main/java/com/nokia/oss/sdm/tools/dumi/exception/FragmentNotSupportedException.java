package com.nokia.oss.sdm.tools.dumi.exception;

/**
 * Created by x36zhao on 2017/7/22.
 */
public class FragmentNotSupportedException extends Exception
{
    public FragmentNotSupportedException ()
    {
    }

    public FragmentNotSupportedException (String message)
    {
        super(message);
    }

    public FragmentNotSupportedException (String message, Throwable cause)
    {
        super(message, cause);
    }
}
