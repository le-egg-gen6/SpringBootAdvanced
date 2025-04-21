package com.myproject.springbootzalopay.service;

import com.myproject.springbootzalopay.shared.constants.CryptoConstant;
import com.myproject.springbootzalopay.utils.EncodingUtils;
import jakarta.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 5:30 PM Wed 4/16/2025
 */
@Service
public class ZaloPayCryptoService {

	@Getter
	@Value("${payment.zalopay.key1}")
	private String key1;

	@Getter
	@Value("${payment.zalopay.key2}")
	private String key2;

	private final Map<String, Mac> HMAC_ENCODER_1 = new HashMap<>();
	private final Map<String, Mac> HMAC_ENCODER_2 = new HashMap<>();

	private final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

	public ZaloPayCryptoService() throws NoSuchAlgorithmException {
	}

	@PostConstruct
	private void initialize() {
		Mac HMAC_SHA256_1 = initializeHMAC(CryptoConstant.HMAC_SHA256, key1);
		HMAC_ENCODER_1.put(CryptoConstant.HMAC_SHA256, HMAC_SHA256_1);

		Mac HMAC_SHA512_1 = initializeHMAC(CryptoConstant.HMAC_SHA512, key1);
		HMAC_ENCODER_1.put(CryptoConstant.HMAC_SHA512, HMAC_SHA512_1);

		Mac HMAC_SHA1_1 = initializeHMAC(CryptoConstant.HMAC_SHA1, key1);
		HMAC_ENCODER_1.put(CryptoConstant.HMAC_SHA1, HMAC_SHA1_1);

		Mac HMAC_MD5_1 = initializeHMAC(CryptoConstant.HMAC_MD5, key1);
		HMAC_ENCODER_1.put(CryptoConstant.HMAC_MD5, HMAC_MD5_1);

		Mac HMAC_SHA256_2 = initializeHMAC(CryptoConstant.HMAC_SHA256, key2);
		HMAC_ENCODER_2.put(CryptoConstant.HMAC_SHA256, HMAC_SHA256_2);

		Mac HMAC_SHA512_2 = initializeHMAC(CryptoConstant.HMAC_SHA512, key2);
		HMAC_ENCODER_2.put(CryptoConstant.HMAC_SHA512, HMAC_SHA512_2);

		Mac HMAC_SHA1_2 = initializeHMAC(CryptoConstant.HMAC_SHA1, key2);
		HMAC_ENCODER_2.put(CryptoConstant.HMAC_SHA1, HMAC_SHA1_2);

		Mac HMAC_MD5_2 = initializeHMAC(CryptoConstant.HMAC_MD5, key2);
		HMAC_ENCODER_2.put(CryptoConstant.HMAC_MD5, HMAC_MD5_2);
	}

	private Mac initializeHMAC(final String algorithm, String key) {
		try {
			Mac HMACInstance = Mac.getInstance(algorithm);
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(UTF8_CHARSET), algorithm);
			HMACInstance.init(signingKey);
			return HMACInstance;
		} catch (NoSuchAlgorithmException | InvalidKeyException ex) {
			return null;
		}
	}

	private Mac getHmacEncoder(String hmacAlgorithmName, int useCase) {
		return switch (useCase) {
			case 1 -> HMAC_ENCODER_1.get(hmacAlgorithmName);
			case 2 -> HMAC_ENCODER_2.get(hmacAlgorithmName);
			default -> null;
		};
	}

	public String HMACBase64Encode(final String algorithm, String data, int useCase) {
		byte[] hmacEncodeByte = HMACEncode(algorithm, data, useCase);
		if (hmacEncodeByte == null) {
			return null;
		}
		return Base64.getEncoder().encodeToString(hmacEncodeByte);
	}

	public String HMACHexStringEncode(final String algorithm, String data, int useCase) {
		byte[] hmacEncodeByte = HMACEncode(algorithm, data, useCase);
		if (hmacEncodeByte == null) {
			return null;
		}
		return EncodingUtils.byteArrayToHexString(hmacEncodeByte);
	}

	public byte[] HMACEncode(final String algorithm, String data, int useCase) {
		Mac hmac = getHmacEncoder(algorithm, useCase);
		return HMACEncode(hmac, data);
	}

	private byte[] HMACEncode(Mac macGenerator, String data) {
		if (macGenerator == null) {
			return null;
		}
		byte[] dataByte = null;
		dataByte = data.getBytes(UTF8_CHARSET);
		return macGenerator.doFinal(dataByte);
	}
}
