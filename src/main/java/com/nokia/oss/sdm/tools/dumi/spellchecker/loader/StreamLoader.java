package com.nokia.oss.sdm.tools.dumi.spellchecker.loader;

import com.nokia.oss.sdm.tools.dumi.spellchecker.dict.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class StreamLoader implements Loader
{
    private final InputStream stream;
    private final String name;
    private List<String> lines;

    public StreamLoader(InputStream stream, String name)
    {
        this.stream = stream;
        this.name = name;
        this.lines = new ArrayList<>();
    }

    @Override
    public List<String> load ()
    {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                lines.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public String getName ()
    {
        return name;
    }
}
