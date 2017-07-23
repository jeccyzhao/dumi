package com.nokia.oss.sdm.tools.dumi.report;

import com.nokia.oss.sdm.tools.dumi.util.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
    public String buildReport (String templateFile,  Map<String, Object> dataModel)
    {
        FileWriter out = null;
        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setDefaultEncoding("UTF-8");
        config.setClassForTemplateLoading(ReportBuilder.class, "/template");
        try
        {
            Template template = config.getTemplate("report.tpl");
            File outputFile = new File("dumiReport_" + DateUtil.parseTime(new Date())+ ".html");
            if (!outputFile.exists())
            {
                outputFile.createNewFile();
            }
            out = new FileWriter(outputFile);
            template.process(dataModel, out);
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
