package com.myproject.springbootoauth2.config.security.jwt;

import com.myproject.springbootoauth2.config.ApplicationConfig;
import com.myproject.springbootoauth2.config.security.userdetail.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 5:06 PM Wed 3/12/2025
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final ApplicationConfig applicationConfig;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		secretKey = Keys.hmacShaKeyFor(applicationConfig.getAuth().getTokenSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(Authentication authentication) {
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + applicationConfig.getAuth().getTokenExpirationInMilliseconds());

		return Jwts.builder()
			.subject(Long.toString(userDetails.getUser().getId()))
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(secretKey)
			.compact();

	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
