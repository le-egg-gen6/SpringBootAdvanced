package com.myproject.springbootannotationadvanced.lombok.annotation;

import com.myproject.springbootannotationadvanced.lombok.sides.XAccessLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenle
 * @since 4:39 PM Wed 2/19/2025
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface XSetter {

    XAccessLevel value() default XAccessLevel.PUBLIC;

}
