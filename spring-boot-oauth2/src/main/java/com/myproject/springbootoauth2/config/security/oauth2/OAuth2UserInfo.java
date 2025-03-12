package com.myproject.springbootoauth2.config.security.oauth2;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 11:40 AM Wed 3/12/2025
 */
@Getter
@Setter
public abstract class OAuth2UserInfo {

	protected Map<String, Object> attributes;

	public OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public abstract String getId();

	public abstract String getName();

	public abstract String getEmail();

	public abstract String getImageUrl();
}
