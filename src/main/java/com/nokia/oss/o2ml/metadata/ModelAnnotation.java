package com.nokia.oss.o2ml.metadata;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Annotation definition, consists of list of elements definitions
 *
 * E.g.
 *  <annotations>
 <elements name="CAFClassIsCMObject" value="TRUE"/>
 <elements name="CAFComponentType" value="CAF_Other"/>
 <elements name="CAFInstanceResolver" value=""/>
 <elements name="CAFIsGhostObject" value="FALSE"/>
 <type href="com.nsn.nthlrfe.common#//CAFComponentInfo"/>
 </annotations>
 *
 * @author Jeccy.Zhao
 *
 */
public class ModelAnnotation
{
    /**
     * The LOGGER
     */
    protected static final Logger LOGGER = Logger.getLogger(ModelAnnotation.class);

    /**
     * Elements under annotation
     */
    private List<ModelAnnotationElement> elements = new ArrayList<ModelAnnotationElement>();

    /**
     * annotation type
     */
    private ModelAnnotationType type;

    /**
     *
     * @param object
     * @return
     */
    public boolean equals (ModelAnnotation object)
    {
        boolean result = true;

        if (object != null)
        {
            if (object.getElements().size() == elements.size())
            {
                for (int i = 0, size = elements.size(); i < size; i++)
                {
                    if (!object.getElements().get(i).equals(elements.get(i)))
                    {
                        result = false;
                    }
                }
            }
            else
            {
                LOGGER.error("Annotation element size not matched. (`" + elements.size() +
                        "` in base, `" + object.getElements().size() + "` in fighter)");

                result = false;
            }

            if (type != null && object.getType() != null)
            {
                if (!type.getHref().equals(object.getType().getHref()))
                {
                    result = false;
                }
            }
        }
        else
        {
            result = false;
        }

        return result;
    }

    /**
     * Appends the specified element into list
     *
     * @param element
     */
    public void appendElements (ModelAnnotationElement element)
    {
        if (elements == null)
        {
            elements = new ArrayList<ModelAnnotationElement>();
        }

        elements.add(element);
    }

    /**
     * Returns true if the provided element existed in list
     *
     * @param entity
     * @return
     */
    public boolean containsElement (ModelAnnotationElement entity)
    {
        if (elements != null && entity != null)
        {
            for (ModelAnnotationElement element : elements)
            {
                if (element.getName().equals(entity.getName()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public List<ModelAnnotationElement> getElements()
    {
        return elements;
    }

    public ModelAnnotationType getType()
    {
        return type;
    }

    public void setType(ModelAnnotationType type)
    {
        this.type = type;
    }

}
