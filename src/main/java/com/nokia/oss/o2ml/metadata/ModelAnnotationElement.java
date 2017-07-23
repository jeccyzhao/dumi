package com.nokia.oss.o2ml.metadata;

import lombok.Data;
import org.apache.log4j.Logger;

/**
 * Annotation elements definition
 *
 * E.g.
 * <annotations>
 *    <elements name="CAFClassIsCMObject" value="TRUE"/>
 *    ...
 * </annotations>
 *
 * Created by x36zhao on 2017/7/21.
 */
@Data
public class ModelAnnotationElement
{
    /**
     * The LOGGER
     */
    protected static final Logger LOGGER = Logger.getLogger(ModelAnnotationElement.class);

    /**
     * Name of element
     */
    private String name;

    /**
     * Value of element
     */
    private String value;

    /**
     * Default constructor
     */
    public ModelAnnotationElement ()
    {
    }

    public boolean equals (ModelAnnotationElement object)
    {
        boolean result = true;

        if (object != null)
        {
            if (!(object.getName().equals(name) && object.getValue().equals(value)))
            {
                LOGGER.error("Annotation element updated: " + name +
                        ":" + value + "-> " + object.getName() + ":" + object.getValue());

                result = false;
            }
        }

        return result;
    }

    /**
     * The sole constructor
     *
     * @param name
     * @param value
     */
    public ModelAnnotationElement (String name, String value)
    {
        this.name = name;
        this.value = value;
    }
}