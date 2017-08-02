package com.nokia.oss.sdm.tools.dumi.annotation;

import java.lang.annotation.*;

/**
 * Created by x36zhao on 2017/8/2.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OptionArgument
{
    String attribute() default "";
}
