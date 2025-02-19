package com.myproject.springbootannotationadvanced.lombok.annotation;

import com.myproject.springbootannotationadvanced.lombok.sides.XAccessLevel;

import java.lang.annotation.*;

/**
 * @author nguyenle
 * @since 4:39 PM Wed 2/19/2025
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface XGetter {

    XAccessLevel value() default XAccessLevel.PUBLIC;

    boolean lazy() default false;

}
