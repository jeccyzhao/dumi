package com.nokia.oss.o2ml.metadata.fragment.parser;

import com.nokia.oss.o2ml.metadata.fragment.BaseFragment;
import com.nokia.oss.o2ml.metadata.fragment.AlarmManPageFragment;
import com.nokia.oss.o2ml.metadata.fragment.CommonFragment;
import com.nokia.oss.sdm.tools.dumi.context.ApplicationContext;
import com.nokia.oss.sdm.tools.dumi.context.Constants;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import java.io.File;

/**
 * Created by x36zhao on 2017/7/21.
 */
public class AlarmManFragmentParser extends AbstractXmlParser
{
    /**
     * The LOGGER
     */
    protected static final Logger LOGGER = Logger.getLogger(AlarmManFragmentParser.class);

    /**
     * The sole constructor
     *
     * @throws Exception
     */
    public AlarmManFragmentParser ()
    {
        super();
    }

    /**
     * @return
     * @throws Exception
     */
    protected BaseFragment doParse () throws Exception
    {
        boolean isIgnored = false;
        Node rootNode = xmlDoc.getFirstChild();
        String ref = evaluateNodeAttrValueByPath(rootNode, "//Adaptation", "href");
        BaseFragment commonFragment = processRef(ref);
        if (commonFragment != null)
        {
            String ignoredIds = ApplicationContext.getInstance().getProperty(Constants.propIgnoredAdaptId);
            if (ignoredIds != null && !"".equals(ignoredIds))
            {
                for (String id : ignoredIds.split(","))
                {
                    if (id.equals(commonFragment.getAdaptationId()))
                    {
                        LOGGER.info("Processing file '" + xmlFile.getPath() + "' is discarded as '" + id + "' is in ignored id list");
                        isIgnored = true;
                        break;
                    }
                }
            }
        }

        if (!isIgnored)
        {
            AlarmManPageFragment manPageFragment = new AlarmManPageFragment();
            manPageFragment.setFileName(xmlFile.getPath());

            manPageFragment.setAlarmText(evaluateAttributeValue(rootNode, "alarmText"));
            manPageFragment.setAlarmType(evaluateAttributeValue(rootNode, "alarmType"));
            manPageFragment.setCancelling(evaluateAttributeValue(rootNode, "cancelling"));
            manPageFragment.setInstructions(evaluateAttributeValue(rootNode, "instructions"));
            manPageFragment.setMeaning(evaluateAttributeValue(rootNode, "meaning"));
            manPageFragment.setPatchLevel(evaluateAttributeValue(rootNode, "patchLevel"));
            manPageFragment.setSpecificProblem(evaluateAttributeValue(rootNode, "specificProblem"));
            manPageFragment.setPerceivedSeverityInfo(evaluateAttributeValue(rootNode, "perceivedSeverityInfo"));
            manPageFragment.setAdaptationId(commonFragment.getAdaptationId());
            manPageFragment.setAdaptationRelease(commonFragment.getAdaptationRelease());

            return manPageFragment;
        }

        return null;
    }

    private BaseFragment processRef (String ref) throws Exception
    {
        String prefix = "../";
        String suffix = ".common";
        if (ref != null && ref.startsWith(prefix))
        {
            int targetPos = ref.indexOf(suffix);
            if (targetPos > -1)
            {
                String commonFileName = ref.substring(prefix.length(), targetPos + suffix.length());
                String targetDir = xmlFile.getPath();
                for (int i = 0; i < 2; i++)
                {
                    File file = new File(targetDir);
                    if (file.exists())
                    {
                        targetDir = file.getParent();
                    }
                }

                String commonFilePath = targetDir + File.separator + commonFileName;
                CommonFragmentParser commonFragmentParser = new CommonFragmentParser();
                BaseFragment commonFragment = commonFragmentParser.parse(commonFilePath);
                if (commonFragment != null)
                {
                    return commonFragment;
                }
            }
        }

        return null;
    }
}
