package com.myproject.springbootpayment.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * @author nguyenle
 * @since 12:38 AM Thu 4/24/2025
 */
@UtilityClass
public class IPUtils {

    private static final String[] ipServices = {
            "https://api.ipify.org",
            "https://checkip.amazonaws.com",
            "https://icanhazip.com"
    };

    private static final String[] headers = {
            "X-Forwarded-For",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED"
    };

    private static String cachedOldIp = null;

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static String getCurrentMachineIPAddress() {
        for (String service : ipServices) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(service))
                        .timeout(Duration.ofSeconds(5))
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    String ip = response.body();
                    if (ip != null && !ip.isEmpty()) {
                        cachedOldIp = ip.trim();
                        break;
                    }
                }
            } catch (Exception ex) {
                continue;
            }
        }
        return cachedOldIp;
    }

    public String getIPFromHttpRequest(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = null;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !ip.equalsIgnoreCase("unknown")) {
                int commaIndex = ip.indexOf(',');
                if (commaIndex != -1) {
                    ip = ip.substring(0, commaIndex).trim();
                }
                break;
            }
        }

        if (ip == null || ip.isEmpty() || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
