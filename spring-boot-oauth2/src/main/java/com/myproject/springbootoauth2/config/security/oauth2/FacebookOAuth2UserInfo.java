package com.myproject.springbootoauth2.config.security.oauth2;

import com.myproject.springbootoauth2.shared.FacebookOAuth2AttributeKey;
import com.myproject.springbootoauth2.utils.OAuth2AttributeUtils;
import java.util.Map;

/**
 * @author nguyenle
 * @since 11:46 AM Wed 3/12/2025
 */
public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

	public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return OAuth2AttributeUtils.getAttribute(attributes, FacebookOAuth2AttributeKey.ID);
	}

	@Override
	public String getName() {
		return OAuth2AttributeUtils.getAttribute(attributes, FacebookOAuth2AttributeKey.NAME);
	}

	@Override
	public String getEmail() {
		return OAuth2AttributeUtils.getAttribute(attributes, FacebookOAuth2AttributeKey.EMAIL);
	}

	@Override
	public String getImageUrl() {
		return OAuth2AttributeUtils.getAttribute(attributes, FacebookOAuth2AttributeKey.IMAGE_URL);
	}
}
