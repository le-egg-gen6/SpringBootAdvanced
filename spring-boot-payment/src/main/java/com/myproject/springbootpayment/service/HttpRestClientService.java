package com.myproject.springbootpayment.service;

import com.myproject.springbootpayment.utils.RequestUtils;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author nguyenle
 * @since 3:28 PM Tue 4/22/2025
 */
@Service
public class HttpRestClientService {

	private final RestTemplate restTemplate;

	private final HttpHeaders defaultHeader;

	public HttpRestClientService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder
			.setConnectTimeout(Duration.ofMinutes(5))
			.setReadTimeout(Duration.ofMinutes(1))
			.build();
		this.defaultHeader = new HttpHeaders();
		this.defaultHeader.setContentType(MediaType.APPLICATION_JSON);
	}

	public <T> T request(
		String baseUrl,
		String endpoint,
		HttpMethod method,
		Map<String, String> params,
		Map<String, String> headers,
		Object body,
		Class<T> responseType
	) {
		return request(baseUrl + endpoint, method, params, headers, body, responseType);
	}

	public <T> T request(
		String baseUrl,
		HttpMethod method,
		Map<String, String> params,
		Map<String, String> headers,
		Object body,
		Class<T> responseType
	) {
		try {
			URI uri = RequestUtils.buildUri(baseUrl, params);
			HttpHeaders header = RequestUtils.buildHeader(headers);

			HttpEntity<?> httpEntity = new HttpEntity<>(body, header);

			ResponseEntity<T> response = restTemplate.exchange(
				uri,
				method,
				httpEntity,
				responseType
			);
			return response.getBody();
		} catch (Exception ex) {
			return null;
		}
	}

	public <T> T get(
		String baseUrl,
		String endpoint,
		Map<String, String> queryParams,
		Map<String, String> headers,
		Class<T> responseType
	) {
		return request(
			baseUrl,
			endpoint,
			HttpMethod.GET,
			queryParams,
			headers,
			null,
			responseType
		);
	}

	public <T> T get(
		String baseUrl,
		Map<String, String> queryParams,
		Map<String, String> headers,
		Class<T> responseType
	) {
		return request(
			baseUrl,
			HttpMethod.GET,
			queryParams,
			headers,
			null,
			responseType
		);
	}

	public <T> T post(
		String baseUrl,
		String endpoint,
		Map<String, String> queryParams,
		Map<String, String> headers,
		Object body,
		Class<T> responseType
	) {
		return request(
			baseUrl,
			endpoint,
			HttpMethod.POST,
			queryParams,
			headers,
			body,
			responseType
		);
	}

	public <T> T post(
		String baseUrl,
		Map<String, String> queryParams,
		Map<String, String> headers,
		Object body,
		Class<T> responseType
	) {
		return request(
			baseUrl,
			HttpMethod.POST,
			queryParams,
			headers,
			body,
			responseType
		);
	}

}

