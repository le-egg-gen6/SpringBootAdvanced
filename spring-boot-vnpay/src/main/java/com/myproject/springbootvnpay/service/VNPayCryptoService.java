package com.myproject.springbootvnpay.service;

import com.myproject.springbootvnpay.shared.constant.CryptoConstant;
import com.myproject.springbootvnpay.utils.EncodingUtils;
import jakarta.annotation.PostConstruct;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 11:23 AM Mon 3/10/2025
 */
@Service
public class VNPayCryptoService {

	@Value("${payment.vnpay.secret-key}")
	private String secretKey;

	private final Mac mac = Mac.getInstance(CryptoConstant.HMAC_SHA512);

	public VNPayCryptoService() throws NoSuchAlgorithmException {
	}

	@PostConstruct
	private void initialize() throws InvalidKeyException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), CryptoConstant.HMAC_SHA512);
		mac.init(secretKeySpec);
	}

	public String sign(String data) {
		try {
			return EncodingUtils.toHexString(mac.doFinal(data.getBytes()));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
