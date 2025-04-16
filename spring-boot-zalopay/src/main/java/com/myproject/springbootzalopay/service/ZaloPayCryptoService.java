package com.myproject.springbootzalopay.service;

import com.myproject.springbootzalopay.shared.constant.EncodingConstant;
import jakarta.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 5:30 PM Wed 4/16/2025
 */
@Service
public class ZaloPayCryptoService {

	private final Map<String, Mac> HMAC_ENCODER = new HashMap<>();

	private final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

	public ZaloPayCryptoService() throws NoSuchAlgorithmException {
	}

	@PostConstruct
	private void initialize() {
		try {
			Mac HMAC_SHA256 = Mac.getInstance(EncodingConstant.HMAC_SHA256);
			HMAC_ENCODER.put(EncodingConstant.HMAC_SHA256, HMAC_SHA256);

			Mac HMAC_SHA512 = Mac.getInstance(EncodingConstant.HMAC_SHA512);
			HMAC_ENCODER.put(EncodingConstant.HMAC_SHA512, HMAC_SHA512);

			Mac HMAC_SHA1 = Mac.getInstance(EncodingConstant.HMAC_SHA1);
			HMAC_ENCODER.put(EncodingConstant.HMAC_SHA1, HMAC_SHA1);

			Mac HMAC_MD5 = Mac.getInstance(EncodingConstant.HMAC_MD5);
			HMAC_ENCODER.put(EncodingConstant.HMAC_MD5, HMAC_MD5);
		} catch (NoSuchAlgorithmException e) {}
	}

	private Mac getHmacEncoder(String hmacAlgorithmName) {
		return HMAC_ENCODER.get(hmacAlgorithmName);
	}


}
