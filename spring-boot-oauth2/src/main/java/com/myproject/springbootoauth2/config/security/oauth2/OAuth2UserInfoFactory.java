package com.myproject.springbootoauth2.config.security.oauth2;

import com.myproject.springbootoauth2.shared.AuthProvider;
import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 11:58 AM Wed 3/12/2025
 */
@UtilityClass
public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider provider, Map<String, Object> attributes) {
		switch (provider) {
			case FACEBOOK:
				return new FacebookOAuth2UserInfo(attributes);
			case GOOGLE:
				return new GoogleOAuth2UserInfo(attributes);
			case GITHUB:
				return new GithubOAuth2UserInfo(attributes);
			default:
				return null;
		}
	}

}
