package com.nokia.oss.sdm.tools.dumi.annotation;

import java.lang.annotation.*;

/**
 * Created by x36zhao on 2017/7/21.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TypoInspection
{
    String name() default "";
}
