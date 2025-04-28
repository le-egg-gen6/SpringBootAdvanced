package com.myproject.springbootpayment.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 3:04 PM Tue 4/22/2025
 */
@UtilityClass
public class CryptoUtils {

	public static SecretKey createSecretKey(String algorithm, String key) {
		return new SecretKeySpec(key.getBytes(), algorithm);
	}

	public static Mac getMac(String algorithm) throws NoSuchAlgorithmException {
		return Mac.getInstance(algorithm);
	}

	public static Mac initMac(String algorithm, String key) throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKey secretKey = createSecretKey(algorithm, key);
		Mac mac = getMac(algorithm);
		mac.init(secretKey);
		return mac;
	}

}
