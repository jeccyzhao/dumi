package com.nokia.oss.sdm.tools.dumi.spellchecker.util;

import javax.xml.soap.Text;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class TextRange
{
    private final int myStartOffset;
    private final int myEndOffset;

    public TextRange (int startOffset, int endOffset)
    {
        myStartOffset = startOffset;
        myEndOffset = endOffset;
    }

    public static TextRange from(int offset, int length)
    {
        return create(offset, offset + length);
    }

    public static TextRange create(int startOffset, int endOffset)
    {
        return new TextRange(startOffset, endOffset);
    }

    public static TextRange create (TextRange segment)
    {
        return create(segment.getStartOffset(), segment.getEndOffset());
    }

    public final int getStartOffset ()
    {
        return myStartOffset;
    }

    public final int getEndOffset ()
    {
        return myEndOffset;
    }

    public final int getLength ()
    {
        return myEndOffset - myStartOffset;
    }

    public boolean equals (Object obj)
    {
        if (!(obj instanceof TextRange)) return false;
        TextRange range = (TextRange) obj;
        return myStartOffset == range.myStartOffset && myEndOffset == range.myEndOffset;
    }

    public static TextRange allOf(String s)
    {
        return new TextRange(0, s.length());
    }

    public int hashCode ()
    {
        return myStartOffset + myEndOffset;
    }

    public boolean containsRange (int startOffset, int endOffset)
    {
        return myStartOffset <= startOffset && myEndOffset >= endOffset;
    }

    public boolean containsOffset (int offset)
    {
        return myStartOffset <= offset && offset <= myEndOffset;
    }

    public String substring(String str)
    {
        return str.substring(myStartOffset, myEndOffset);
    }
}