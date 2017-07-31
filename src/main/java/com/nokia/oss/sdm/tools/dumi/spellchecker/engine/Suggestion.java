package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

import org.jetbrains.annotations.NotNull;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class Suggestion implements Comparable<Suggestion>
{
    private final String word;
    private final int metrics;

    public Suggestion(String word, int metrics)
    {
        this.word = word;
        this.metrics = metrics;
    }

    public String getWord()
    {
        return word;
    }

    public int getMetrics()
    {
        return metrics;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Suggestion result = (Suggestion) o;
        if (metrics != result.metrics) return false;
        if (word != null ? !word.equals(result.word) : result.word != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + metrics;
        return result;
    }

    @Override
    public int compareTo (Suggestion o)
    {
        if (!(o instanceof Suggestion)) throw new IllegalArgumentException();
        Suggestion r = (Suggestion)o;
        return new Integer(getMetrics()).compareTo(r.getMetrics());
    }

    @Override
    public String toString()
    {
        return word + " : " + metrics;
    }
}
