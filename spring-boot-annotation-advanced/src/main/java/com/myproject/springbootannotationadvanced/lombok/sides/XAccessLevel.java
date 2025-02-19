package com.myproject.springbootannotationadvanced.lombok.sides;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nguyenle
 * @since 2:33 AM Thu 2/20/2025
 */
public enum XAccessLevel {

    PUBLIC(4, "public"),
    PROTECTED(3, "protected"),
    PACKAGE(2, "default"),
    PRIVATE(1, "private"),
    NONE(0, "")
    ;

    private static Map<Integer, XAccessLevel> map = new HashMap<Integer, XAccessLevel>();

    static {
        for (XAccessLevel level : XAccessLevel.values()) {
            map.put(level.priority, level);
        }
    }

    public static XAccessLevel getXAccessLevel(int priority) {
        return map.getOrDefault(priority, XAccessLevel.PUBLIC);
    }

    private int priority;
    private String functionAccessModifier;

    XAccessLevel(int priority, String functionAccessModifier) {
        this.priority = priority;
        this.functionAccessModifier = functionAccessModifier;
    }

    public int getPriority() {
        return priority;
    }

    public String getFunctionAccessModifier() {
        return functionAccessModifier;
    }

}
