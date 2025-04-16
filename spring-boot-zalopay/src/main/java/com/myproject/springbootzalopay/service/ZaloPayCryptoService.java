package com.myproject.springbootzalopay.service;

import com.myproject.springbootzalopay.shared.constant.EncodingConstant;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 5:30 PM Wed 4/16/2025
 */
@Service
public class ZaloPayCryptoService {

	private final Mac HMAC_SHA256 = Mac.getInstance(EncodingConstant.HMAC_SHA256);

	private final Mac HMAC_SHA512 = Mac.getInstance(EncodingConstant.HMAC_SHA512);

	private final Mac HMAC_SHA1 = Mac.getInstance(EncodingConstant.HMAC_SHA1);

	private final Mac HMAC_MD5 = Mac.getInstance(EncodingConstant.HMAC_MD5);

	private final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

	public ZaloPayCryptoService() throws NoSuchAlgorithmException {
	}


}
