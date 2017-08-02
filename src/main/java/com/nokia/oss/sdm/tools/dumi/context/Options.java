package com.nokia.oss.sdm.tools.dumi.context;

import com.nokia.oss.sdm.tools.dumi.annotation.OptionArgument;
import com.nokia.oss.sdm.tools.dumi.util.AnnotationUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.io.File;

/**
 * Created by x36zhao on 2017/8/2.
 */
@Data
public final class Options
{
    @OptionArgument(attribute = "-f")
    private String scanFolder;

    @OptionArgument(attribute = "-d")
    private String userDictionary;

    @OptionArgument(attribute = "-s")
    private String plainTextSplitter;

    public Options ()
    {
    }

    public boolean isValid ()
    {
        return scanFolder != null && new File(scanFolder).exists();
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
                        catch (IllegalAccessException e)
                        {
                        }
                        break;
                    }
                }
            }
        }

        return options;
    }
}
