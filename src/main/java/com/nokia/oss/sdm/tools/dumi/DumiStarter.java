package com.nokia.oss.sdm.tools.dumi;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Options;
import com.nokia.oss.sdm.tools.dumi.gui.DumiGui;
import com.nokia.oss.sdm.tools.dumi.inspector.InspectionProcessor;
import com.nokia.oss.sdm.tools.dumi.util.StringUtil;
import javafx.application.Application;

/**
 * Created by x36zhao on 2017/8/3.
 */
public class DumiStarter
{
    public static void printHelp ()
    {
        System.out.println("java -jar dumi.jar <scanningFolder> [ignoredWordFile.txt]");
    }

    public static void main (String[] args)
    {
        Options options = Options.from(args);
        if (options.isValid())
        {
            ApplicationContext.getInstance().setOptions(options);
            if (options.isGuiMode())
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run ()
                    {
                        Application.launch(DumiGui.class, args);
                    }
                }).start();
            }
            else
            {
                new InspectionProcessor().inspect(options.getScanFolder(), options.getUserDictionary());
            }
        }
        else
        {
            System.out.println("Illegal arguments: " + StringUtil.join(args, " "));
            System.exit(1);
        }
    }
}
