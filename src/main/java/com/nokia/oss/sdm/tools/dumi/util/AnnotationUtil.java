package com.nokia.oss.sdm.tools.dumi.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/22.
 */
public class AnnotationUtil
{
    public static List<Field> getAnnotatedFields (Class annotationClaz, Class targetClaz)
    {
        List<Field> annotatedFields = new ArrayList<Field>();
        Field[] declaredFields = targetClaz.getDeclaredFields();
        for (Field field : declaredFields)
        {
            if (field.isAnnotationPresent(annotationClaz))
            {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }
}
