package com.nokia.oss.sdm.tools.dumi.context;

import com.nokia.oss.sdm.tools.dumi.annotation.OptionArgument;
import com.nokia.oss.sdm.tools.dumi.util.AnnotationUtil;
import lombok.Data;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.io.File;

/**
 * Created by x36zhao on 2017/8/2.
 */
@Data
public final class Options
{
    private static int DEFAULT_THREAD_THRESHOLD = 2;
    private static final Logger LOGGER = Logger.getLogger(Options.class);

    @OptionArgument(attribute = "-f")
    private String scanFolder;

    @OptionArgument(attribute = "-d")
    private String userDictionary;

    @OptionArgument(attribute = "-r")
    private String regexPatternRuleFile;

    @OptionArgument(attribute = "-s")
    private String plainTextSplitter;

    @OptionArgument(attribute = "-t")
    private int threadThreshold;

    @OptionArgument(attribute = "-console")
    private String consoleMode;

    public Options ()
    {
    }

    public boolean isGuiMode ()
    {
        return consoleMode == null;
    }

    public int getThreadThreshold()
    {
        if (threadThreshold < 1)
        {
            String configuredThreshold = ApplicationContext.getInstance().getProperty(Constants.propThreadThreshold);
            if (configuredThreshold != null)
            {
                try
                {
                    threadThreshold = Integer.valueOf(configuredThreshold);
                }
                catch (Exception ex)
                {
                    threadThreshold = DEFAULT_THREAD_THRESHOLD;
                }
            }
        }

        return threadThreshold;
    }

    public boolean isValid ()
    {
        if (!isGuiMode())
        {
            return scanFolder != null && new File(scanFolder).exists();
        }

        return true;
    }

    public String getPlainTextSplitter()
    {
        if (plainTextSplitter == null || "".equals(plainTextSplitter))
        {
            plainTextSplitter = ApplicationContext.getInstance().getProperty(Constants.propTextSplit);
        }

        return plainTextSplitter;
    }

    public static Options from (String[] args)
    {
        Options options = new Options();
        if (args != null && args.length > 0 && args.length % 2 == 0)
        {
            List<Field> fields = AnnotationUtil.getAnnotatedFields(OptionArgument.class, Options.class);
            for (int i = 0; i < args.length; i++)
            {
                if (i + 1 >= args.length)
                {
                    break;
                }

                String argName = args[i];
                String argValue = args[i + 1];
                for (Field field : fields)
                {
                    OptionArgument optArg = field.getAnnotation(OptionArgument.class);
                    String attribute = optArg.attribute();
                    if (args[i].equals(attribute))
                    {
                        field.setAccessible(true);
                        try
                        {
                            field.set(options, argValue);
                            ++i;
                        }
                        catch (Exception e)
                        {
                            LOGGER.warn("Failed to set field (" + field + ") when creating option", e);
                        }

                        break;
                    }
                }
            }
        }

        return options;
    }
}
