package com.nokia.oss.sdm.tools.dumi.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class Strings
{
    public static boolean isCapitalized (String word)
    {
        if (word.length() == 0) return false;

        boolean lowCase = true;
        for (int i = 1; i < word.length() && lowCase; i++)
        {
            lowCase = Character.isLowerCase(word.charAt(i));
        }
        return Character.isUpperCase(word.charAt(0)) && lowCase;
    }


    public static boolean isCapitalized (String text, TextRange range)
    {
        if (range.getLength() == 0) return false;
        CharacterIterator it = new StringCharacterIterator(text, range.getStartOffset() + 1, range.getEndOffset(), range.getStartOffset() + 1);
        boolean lowCase = true;
        for (char c = it.first(); c != CharacterIterator.DONE; c = it.next())
        {
            lowCase = Character.isLowerCase(c);
        }

        return Character.isUpperCase(text.charAt(range.getStartOffset())) && lowCase;
    }

    public static boolean isUpperCased (String text, TextRange range)
    {
        if (range.getLength() == 0) return false;
        CharacterIterator it = new StringCharacterIterator(text, range.getStartOffset(), range.getEndOffset(), range.getStartOffset());

        for (char c = it.first(); c != CharacterIterator.DONE; c = it.next())
        {
            if (!Character.isUpperCase(c))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean isUpperCase (String word)
    {
        boolean upperCase = true;
        for (int i = 0; i < word.length() && upperCase; i++)
        {
            upperCase = Character.isUpperCase(word.charAt(i));
        }

        return upperCase;
    }

    public static boolean isMixedCase (String word)
    {
        if (word.length() < 2) return false;

        String tail = word.substring(1);
        String lowerCase = tail.toLowerCase();
        return !tail.equals(lowerCase) && !isUpperCase(word);
    }

    public static void upperCase (List<String> words)
    {
        for (int i = 0; i < words.size(); i++)
        {
            words.set(i, words.get(i).toUpperCase());
        }
    }

    public static void capitalize(List<String> words)
    {
        for (int i = 0; i < words.size(); i++)
        {
            words.set(i, StringUtil.capitalize(words.get(i)));
        }
    }
}
