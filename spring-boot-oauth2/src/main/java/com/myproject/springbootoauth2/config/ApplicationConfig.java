package com.myproject.springbootoauth2.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author nguyenle
 * @since 4:41 PM Wed 3/12/2025
 */
@EnableConfigurationProperties({ApplicationConfig.class})
@ConfigurationProperties(prefix = "app")
@Configuration
@Getter
@Setter
public class ApplicationConfig {
	private final AuthConfig auth = new AuthConfig();
	private final OAuth2Config oauth2 = new OAuth2Config();


	@Setter
	@Getter
	public static class AuthConfig {
		private String tokenSecret;
		private Long tokenExpirationInMilliseconds;

	}

	@Setter
	@Getter
	public static class OAuth2Config {
		private List<String> authorizedRedirectUris = new ArrayList<>();

	}
}






