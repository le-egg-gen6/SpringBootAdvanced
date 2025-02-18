package com.myproject.springbootcdcdebezium.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 12:36 AM Mon 2/17/2025
 */
@UtilityClass
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LogUtils.info("Error while parsing json");
            LogUtils.error(e);
            return "";
        }
    }

    public static String compressLog(String idPayload, String dataPayload) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(idPayload);
        sb.append(",");
        sb.append(dataPayload);
        sb.append("}");
        return sb.toString();
    }

    public static String extractUsableDataFromCDCLog(String cdcLog) {
	    try {
		    JsonNode node = objectMapper.readTree(cdcLog);
            JsonNode payload = node.get("payload");
            String payloadStr = payload.toString();
            return payloadStr.substring(1, payloadStr.length() - 1);
	    } catch (JsonProcessingException e) {
		    LogUtils.info("Error while parsing json");
            LogUtils.error(e);
            return "";
	    }
    }

}
