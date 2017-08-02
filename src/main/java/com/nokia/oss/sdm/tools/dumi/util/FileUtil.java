package com.nokia.oss.sdm.tools.dumi.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/22.
 */
public class FileUtil
{
    public static List<String> getFileContent (String file)
    {
        return getFileContent(file, true);
    }

    public static List<String> getFileContent (String file, boolean isInSource)
    {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;
        InputStream inputStream = null;
        try
        {
            if (isInSource)
            {
                inputStream = FileUtil.class.getResourceAsStream("/" + file);
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    lines.add(line);
                }
            }
            else
            {
                //BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(file)));
                //reader = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);
                LineIterator it = FileUtils.lineIterator(new File(file), "UTF-8");
                try
                {
                    while (it.hasNext())
                    {
                        String line = it.nextLine();
                        lines.add(line);
                    }
                }
                finally
                {
                    LineIterator.closeQuietly(it);
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            }
        }

        return lines;
    }

    public static String getFileName (String file)
    {
        File fos = new File(file);
        if (fos.exists())
        {
            return fos.getName();
        }

        return null;
    }

    public static List<String> listFiles (String filePath, final String[] fileSuffixes)
    {
        final List<String> matchedFiles = new ArrayList<String>();
        File fos = new File(filePath);
        if (fos.exists() && fos.isDirectory())
        {
            File[] childFiles = fos.listFiles();
            for (File childFile : childFiles)
            {
                matchedFiles.addAll(listFiles(childFile.getAbsolutePath(), fileSuffixes));
            }
        }
        else
        {
            if (fileSuffixes != null && fileSuffixes.length > 0)
            {
                for (String suffix : fileSuffixes)
                {
                    if (filePath.endsWith(suffix))
                    {
                        matchedFiles.add(filePath);
                        break;
                    }
                }
            }
            else
            {
                matchedFiles.add(filePath);
            }
        }

        return matchedFiles;
    }
}
