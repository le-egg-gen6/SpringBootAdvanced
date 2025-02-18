package com.myproject.springbootmasterslavedbsynchronization.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 12:26 AM Tue 2/18/2025
 */
@UtilityClass
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            LogUtils.info("Error while parsing object to json");
            LogUtils.error(e);
            return "";
        }
    }

    public static JsonNode toJsonNode(String json) {
	    try {
		    return mapper.readTree(json);
	    } catch (JsonProcessingException e) {
		    LogUtils.info("Error while parsing json to json node");
            LogUtils.error(e);
            return null;
	    }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            LogUtils.info("Error while parsing json to object");
            LogUtils.error(e);
            return null;
        }
    }

    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile(
        "\\d{4}-\\d{2}-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,6})?([+-]\\d{2}:?\\d{2}|Z)?");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern TIME_PATTERN = Pattern.compile(
        "\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,6})?");
    private static final Pattern UUID_PATTERN = Pattern.compile(
        "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    private static final Pattern JSONB_PATTERN = Pattern.compile("\\{.*\\}|\\[.*\\]");

    public static String formatValue(JsonNode value) {
        if (value == null || value.isNull()) {
            return "NULL";
        }

        String textValue = value.asText().trim();

        if (textValue.isEmpty()) {
            return "''";
        }

        try {
            // Handle PostgreSQL specific types first
            if (UUID_PATTERN.matcher(textValue).matches()) {
                return formatUUID(textValue);
            } else if (value.isObject() || JSONB_PATTERN.matcher(textValue).matches()) {
                return formatJSONB(value);
            } else if (value.isArray()) {
                return formatArray(value);
            } else if (value.isNumber()) {
                return formatNumber(value);
            } else if (value.isBoolean()) {
                return formatBoolean(value.asBoolean());
            } else if (TIMESTAMP_PATTERN.matcher(textValue).matches()) {
                return formatTimestamp(textValue);
            } else if (DATE_PATTERN.matcher(textValue).matches()) {
                return formatDate(textValue);
            } else if (TIME_PATTERN.matcher(textValue).matches()) {
                return formatTime(textValue);
            } else {
                return formatString(textValue);
            }
        } catch (Exception e) {
            LogUtils.info("Exception while formatting value");
            return formatString(textValue);
        }
    }

    private static String formatNumber(JsonNode value) {
        try {
            if (value.isFloat() || value.isDouble()) {
                BigDecimal decimal = new BigDecimal(value.asText());
                if (decimal.compareTo(BigDecimal.ZERO) == 0) {
                    return "0";
                }
                // PostgreSQL can handle scientific notation, but we'll convert to plain string
                return decimal.stripTrailingZeros().toPlainString();
            }

            // For integers and longs
            if (value.isIntegralNumber()) {
                return value.asText();
            }

            // For numeric/decimal types
            BigDecimal decimal = new BigDecimal(value.asText());
            return decimal.stripTrailingZeros().toPlainString();
        } catch (NumberFormatException e) {
            LogUtils.info("Exception while formatting number value");
            return formatString(value.asText());
        }
    }

    private static String formatBoolean(boolean value) {
        // PostgreSQL accepts TRUE/FALSE keywords
        return value ? "TRUE" : "FALSE";
    }

    private static String formatArray(JsonNode array) {
        if (array.isEmpty()) {
            return "'{}'"; // PostgreSQL empty array syntax
        }

        StringBuilder result = new StringBuilder("ARRAY[");
        for (int i = 0; i < array.size(); i++) {
            if (i > 0) {
                result.append(",");
            }
            result.append(formatValue(array.get(i)));
        }
        result.append("]");
        return result.toString();
    }

    private static String formatTimestamp(String value) {
        try {
            // Try parsing as ISO timestamp
            Instant instant = Instant.parse(value);
            return "TIMESTAMP WITH TIME ZONE '" +
                DateTimeFormatter.ISO_INSTANT.format(instant) + "'";
        } catch (DateTimeParseException e) {
            LogUtils.info("Exception while timestamp value");
            try {
                // Try parsing as local datetime
                LocalDateTime dateTime = LocalDateTime.parse(value,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS]"));
                return "TIMESTAMP '" + dateTime.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")) + "'";
            } catch (DateTimeParseException e2) {
                LogUtils.info("Exception while datetime value");
                return formatString(value);
            }
        }
    }

    private static String formatDate(String value) {
        try {
            LocalDate date = LocalDate.parse(value);
            return "DATE '" + date.format(DateTimeFormatter.ISO_DATE) + "'";
        } catch (DateTimeParseException e) {
            LogUtils.info("Exception while datetime value");
            return formatString(value);
        }
    }

    private static String formatTime(String value) {
        return "TIME '" + value + "'";
    }

    private static String formatUUID(String value) {
        return "'" + value + "'::uuid";
    }

    private static String formatJSONB(JsonNode value) {
        // For JSONB type, escape single quotes and cast to JSONB
        return "'" + value.toString().replace("'", "''") + "'::jsonb";
    }

    private static String formatString(String value) {
        if (value == null) {
            return "NULL";
        }

        // PostgreSQL specific escaping
        String escaped = value
            .replace("'", "''")     // Escape single quotes
            .replace("\\", "\\\\"); // Escape backslashes

        // Handle special characters using PostgreSQL escape string syntax (E'...')
        if (value.contains("\r") || value.contains("\n") ||
            value.contains("\t") || value.contains("\b") ||
            value.contains("\f")) {

            escaped = escaped
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");

            return "E'" + escaped + "'";
        }

        return "'" + escaped + "'";
    }

}
