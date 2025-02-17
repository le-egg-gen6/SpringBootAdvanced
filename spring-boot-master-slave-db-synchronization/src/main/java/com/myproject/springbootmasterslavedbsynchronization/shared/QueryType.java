package com.myproject.springbootmasterslavedbsynchronization.shared;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nguyenle
 * @since 12:51 AM Tue 2/18/2025
 */
public enum QueryType {

    CREATE("c", "CREATE"),
    UPDATE("u", "UPDATE"),
    DELETE("d", "DELETE")
    ;

    private String op;

    private String value;

    QueryType(String op, String value) {
        this.op = op;
        this.value = value;
    }

    static Map<String, QueryType> mapOpToQueryType = new HashMap<>();

    static {
        for (QueryType type : QueryType.values()) {
            mapOpToQueryType.put(type.op, type);
        }
    }

    public static QueryType getQueryType(String op) {
        return mapOpToQueryType.get(op);
    }

}
