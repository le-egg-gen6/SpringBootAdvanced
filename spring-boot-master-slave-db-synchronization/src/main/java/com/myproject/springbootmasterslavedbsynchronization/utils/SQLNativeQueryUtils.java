package com.myproject.springbootmasterslavedbsynchronization.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.myproject.springbootmasterslavedbsynchronization.shared.QueryType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 12:58 AM Tue 2/18/2025
 */
@UtilityClass
public class SQLNativeQueryUtils {

	public static String buildNativeQuery(String log) {
		JsonNode node = JsonUtils.toJsonNode(log);
		if (node == null) {
			return "";
		}
		String table = node.path("source").path("table").asText();

		String op = node.path("op").asText();

		JsonNode beforeNode = node.path("before");
		JsonNode afterNode = node.path("after");

		QueryType queryType = QueryType.getQueryType(op);

		switch (queryType) {
			case CREATE:
				return buildCreateQuery(table, afterNode);
			case UPDATE:
				return buildUpdateQuery(table, beforeNode, afterNode);
			case DELETE:
				return buildDeleteQuery(table, afterNode);
			default:
				return "";
		}
	}

	private static String buildCreateQuery(String table, JsonNode data) {
		List<String> columnList = new ArrayList<>();
		List<String> valueList = new ArrayList<>();

		Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			columnList.add(field.getKey());
			valueList.add(JsonUtils.formatValue(field.getValue()));
		}
		return String.format(
			"INSERT INTO %s (%s) VALUES (%s)",
			table,
			String.join(", ", columnList),
			String.join(", ", valueList)
		);
	}

	private static String buildUpdateQuery(String table, JsonNode before, JsonNode after) {
		List<String> setClauseList = new ArrayList<>();
		List<String> whereClauseList = new ArrayList<>();

		Iterator<Map.Entry<String, JsonNode>> fields = after.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			String column = field.getKey();
			String newValue = JsonUtils.formatValue(field.getValue());

			JsonNode oldValue = before.get(column);
			if (!field.getValue().equals(oldValue)) {
				setClauseList.add(String.format("%s = %s", column, newValue));
			}

			if (isPrimaryKey(column) || !field.getValue().equals(oldValue)) {
				whereClauseList.add(String.format("%s = %s", column, JsonUtils.formatValue(field.getValue())));
			}
		}

		return String.format(
			"UPDATE %s SET %s WHERE %s",
			table,
			String.join(", ", setClauseList),
			String.join(" AND ", whereClauseList)
		);
	}

	private String buildDeleteQuery(String table, JsonNode data) {
		List<String> whereClauseList = new ArrayList<>();

		Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			if (isPrimaryKey(field.getKey())) {
				whereClauseList.add(String.format("%s = %s",
					field.getKey(),
					JsonUtils.formatValue(field.getValue())
				));
			}
		}
		return String.format(
			"DELETE FROM %s WHERE %s",
			table,
			String.join(" AND ", whereClauseList)
		);
	}


	private static boolean isPrimaryKey(String column) {
		return column.toLowerCase().contains("id");
	}

}
