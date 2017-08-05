package com.nokia.oss.sdm.tools.dumi.report;

import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Constants;
import com.nokia.oss.sdm.tools.dumi.inspector.AbstractSpellingInspector;
import com.nokia.oss.sdm.tools.dumi.util.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by x36zhao on 2017/7/22.
 */
public class ReportBuilder
{
    private static final Logger LOGGER = Logger.getLogger(AbstractSpellingInspector.class);

    public String buildReport (String templateFile,  Map<String, Object> dataModel)
    {
        ApplicationContext.Logger(LOGGER, "Building report..", null);
        FileWriter out = null;
        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setDefaultEncoding("UTF-8");
        config.setClassForTemplateLoading(ReportBuilder.class, "/res/template");
        try
        {
            String htmlFile = Constants.REPORT_FILE_NAME + "_" + DateUtil.parseTime(new Date())+ ".html";
            Template template = config.getTemplate(templateFile);
            File outputFile = new File(htmlFile);
            if (!outputFile.exists())
            {
                outputFile.createNewFile();
            }
            out = new FileWriter(outputFile);
            template.process(dataModel, out);

            ApplicationContext.Logger(LOGGER,  "Report file is created as '" + htmlFile + "'", null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (TemplateException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
