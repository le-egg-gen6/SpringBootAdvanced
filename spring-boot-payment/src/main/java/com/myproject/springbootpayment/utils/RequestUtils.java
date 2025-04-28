package com.myproject.springbootpayment.utils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author nguyenle
 * @since 5:14 PM Wed 4/23/2025
 */
@UtilityClass
public class RequestUtils {

	public static HttpHeaders buildHeader(Map<String, String> headers) {
		HttpHeaders header = new HttpHeaders();
		for (Entry<String, String> entry : headers.entrySet()) {
			String key = URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII);
			String value = URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII);
			header.add(key, value);
		}
		return header;
	}

	public static URI buildUri(String baseUrl, Map<String, String> queryParams) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
		if (queryParams != null) {
			for (Entry<String, String> entry : queryParams.entrySet()) {
				String key = URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII);
				String value = URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII);
				builder.queryParam(key, value);
			}
		}
		return builder.build().toUri();
	}

	public static String buildUriStr(String baseUrl, Map<String, String> queryParams) {
		return buildUri(baseUrl, queryParams).toString();
	}

}
