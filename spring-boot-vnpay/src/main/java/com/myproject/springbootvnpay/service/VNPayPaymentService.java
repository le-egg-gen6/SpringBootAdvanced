package com.myproject.springbootvnpay.service;

import com.myproject.springbootvnpay.dto.request.InitPaymentRequest;
import com.myproject.springbootvnpay.dto.response.InitPaymentResponse;
import com.myproject.springbootvnpay.shared.constant.StringConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYCommandConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYCoreConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYCurrencyConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYGoodsTypeConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYLocalConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYParams;
import com.myproject.springbootvnpay.utils.DateUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author nguyenle
 * @since 11:26 AM Mon 3/10/2025
 */
@Service
@RequiredArgsConstructor
public class VNPayPaymentService {

	private final VNPayCryptoService cryptoService;

	@Value("${payment.vnpay.tmn-code}")
	private String tmnCode;

	@Value("${payment.vnpay.init-payment-url}")
	private String initPaymentPrefixUrl;

	@Value("${payment.vnpay.return-url}")
	private String returnUrlFormat;

	@Value("${payment.vnpay.timeout}")
	private Integer paymentTimeout;

	public InitPaymentResponse initPayment(InitPaymentRequest initPaymentRequest) {
		long amount = initPaymentRequest.getAmount() * VNPAYCoreConstant.DEFAULT_MULTIPLIER;
		String txnRef = initPaymentRequest.getTxnRef();
		Long userId = initPaymentRequest.getUserId();
		String ipAddress = initPaymentRequest.getIpAddress();
		String requestId = initPaymentRequest.getRequestId();

		Date createdDate = DateUtils.getVNTime();
		Date expiredDate = DateUtils.getVNTimeAfter(TimeUnit.MINUTES, paymentTimeout);

		String createdDateStr = DateUtils.parseVNPAYFormat(createdDate);
		String expiredDateStr = DateUtils.parseVNPAYFormat(expiredDate);

		String orderInfo = buildPaymentDetails(initPaymentRequest);
		String returnUrl = buildReturnUrl(txnRef);

		Map<String, String> params = new HashMap<>();
		params.put(VNPAYParams.VERSION, VNPAYCoreConstant.SERVICE_VERSION);
		params.put(VNPAYParams.COMMAND, VNPAYCommandConstant.PAY);

		params.put(VNPAYParams.TMN_CODE, tmnCode);
		params.put(VNPAYParams.AMOUNT, String.valueOf(amount));
		params.put(VNPAYParams.CURRENCY, VNPAYCurrencyConstant.VND);

		params.put(VNPAYParams.TXN_REF, txnRef);
		params.put(VNPAYParams.RETURN_URL, returnUrl);

		params.put(VNPAYParams.CREATED_DATE, createdDateStr);
		params.put(VNPAYParams.EXPIRE_DATE, expiredDateStr);

		params.put(VNPAYParams.IP_ADDRESS, ipAddress);
		params.put(VNPAYParams.LOCALE, VNPAYLocalConstant.VIETNAM);

		params.put(VNPAYParams.ORDER_INFO, orderInfo);
		params.put(VNPAYParams.ORDER_TYPE, VNPAYGoodsTypeConstant.BILL_PAYMENT);

		String initPaymentUrl = buildInitPaymentUrl(params);
		return InitPaymentResponse.builder().vnpUrl(initPaymentUrl).build();
	}

	private String buildInitPaymentUrl(Map<String, String> params) {
		StringBuilder hashPayload = new StringBuilder();
		StringBuilder query = new StringBuilder();
		List<String> queryField = new ArrayList<>(params.keySet());
		Collections.sort(queryField);

		Iterator<String> iterator = queryField.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = params.get(key);
			if (StringUtils.hasText(value)) {
				hashPayload.append(key);
				hashPayload.append(StringConstant.EQUALS);
				hashPayload.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

				query.append(URLEncoder.encode(key, StandardCharsets.US_ASCII));
				query.append(StringConstant.EQUALS);
				query.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

				if (iterator.hasNext()) {
					hashPayload.append(StringConstant.AND);
					query.append(StringConstant.AND);
				}
			}
		}
		String secureHash = cryptoService.sign(hashPayload.toString());
		query.append(StringConstant.AND);
		query.append(VNPAYParams.SECURE_HASH);
		query.append(StringConstant.EQUALS);
		query.append(secureHash);
		return String.format("%s?%s", initPaymentPrefixUrl, query);
	}

	private String buildReturnUrl(String txnRef) {
		return String.format(returnUrlFormat, txnRef);
	}

	private String buildPaymentDetails(InitPaymentRequest initPaymentRequest) {
		return String.format("Test %s", initPaymentRequest.getTxnRef());
	}

}
