package com.nokia.oss.sdm.tools.dumi.inspector;

import com.nokia.oss.o2ml.metadata.fragment.BaseFragment;
import com.nokia.oss.o2ml.metadata.fragment.parser.AbstractXmlParser;
import com.nokia.oss.o2ml.metadata.fragment.parser.FragmentParserFactory;
import com.nokia.oss.sdm.tools.dumi.annotation.TypoInspection;
import com.nokia.oss.sdm.tools.dumi.report.TypoInspectionDataModel;
import com.nokia.oss.sdm.tools.dumi.util.AnnotationUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.nokia.oss.sdm.tools.dumi.report.TypoInspectionDataModel.Label;
import static com.nokia.oss.sdm.tools.dumi.report.TypoInspectionDataModel.ErrorItem;

/**
 * Created by x36zhao on 2017/7/21.
 */
public class FragmentSpellingInspector extends AbstractSpellingInspector
{
    protected static final Logger LOGGER = Logger.getLogger(FragmentSpellingInspector.class);

    public TypoInspectionDataModel doProcess (String file) throws Exception
    {
        AbstractXmlParser parser = FragmentParserFactory.createFragmentParser(file);
        if (parser != null)
        {
            TypoInspectionDataModel dataModel = new TypoInspectionDataModel(file);
            BaseFragment fragment = parser.parse(file);
            dataModel.setLabels(checkFragment(fragment));
            dataModel.setCategory(fragment.getId());
            return dataModel;
        }

        return null;
    }

    private List<Label> checkFragment (Object fragment)
    {
        List<Label> labels = new ArrayList<Label>();
        List<Field> annotatedFields = AnnotationUtil.getAnnotatedFields(TypoInspection.class, fragment.getClass());
        for (Field field : annotatedFields)
        {
            try
            {
                field.setAccessible(true);
                Object fieldValue = field.get(fragment);
                if (fieldValue != null)
                {
                    Label labelEntry = new Label(field.getName(), fieldValue.toString());
                    List<ErrorItem> errorItems = inspectText(fieldValue.toString());
                    if (errorItems != null && errorItems.size() > 0)
                    {
                        labelEntry.setErrorItems(errorItems);
                    }

                    labels.add(labelEntry);
                }
            }
            catch (Exception e)
            {
                LOGGER.warn("Failed to processing filed " + field.getName(), e);
            }
        }

        return labels;
    }
}
