package com.myproject.springbootoauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author nguyenle
 * @since 4:36 PM Wed 3/12/2025
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("#{'${spring.security.cors.allowedOrigins}'.split(',')}")
	private String[] corsAllowedOrigins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(corsAllowedOrigins)
			.allowCredentials(true)
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.maxAge(3600);
	}
}
