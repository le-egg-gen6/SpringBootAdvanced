package com.myproject.springbootzalopay.service;

import com.myproject.springbootzalopay.dto.BankDTO;
import com.myproject.springbootzalopay.dto.ListBackDTO;
import com.myproject.springbootzalopay.dto.req.CreateOrderReq;
import com.myproject.springbootzalopay.dto.req.RefundOrderReq;
import com.myproject.springbootzalopay.dto.resp.CreateOrderResp;
import com.myproject.springbootzalopay.dto.resp.QueryOrderResp;
import com.myproject.springbootzalopay.dto.resp.QueryRefundResp;
import com.myproject.springbootzalopay.dto.resp.RefundOrderResp;
import com.myproject.springbootzalopay.shared.constants.CryptoConstant;
import com.myproject.springbootzalopay.shared.constants.StringConstant;
import com.myproject.springbootzalopay.shared.constants.ZaloPayCommandConstant;
import com.myproject.springbootzalopay.shared.constants.ZaloPayParams;
import com.myproject.springbootzalopay.shared.enums.ZaloPayPaymentProvider;
import com.myproject.springbootzalopay.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 1:59 PM Fri 4/18/2025
 */
@Service
@RequiredArgsConstructor
public class ZaloPayService {

	@Value("${payment.zalopay.appid}")
	private int appid;

	@Value("${payment.zalopay.base-endpoint}")
	private String baseEndpoint;

	@Value("${payment.zalopay.list-merchant-bank}")
	private String listMerchantBank;

	@Value("${payment.zalopay.expired-time-in-seconds}")
	private long expiredTimeInSeconds;

	private final HttpRestClientService httpRestClientService;

	private final ZaloPayCryptoService zaloPayCryptoService;

	public Map<ZaloPayPaymentProvider, List<BankDTO>> getListAvailableBank() {
		String reqtime = String.valueOf(System.currentTimeMillis());
		String data = appid + StringConstant.VERTICAL_BAR + reqtime;

		Map<String, String> query = new HashMap<>();
		query.put("appid", String.valueOf(appid));
		query.put("reqtime", reqtime);
		query.put("data", zaloPayCryptoService.HMACHexStringEncode(CryptoConstant.HMAC_SHA256, data, 1));

		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		ListBackDTO listBackDTO = httpRestClientService.post(listMerchantBank, query, header, null, ListBackDTO.class);

		Map<ZaloPayPaymentProvider, List<BankDTO>> map = new HashMap<>();
		if (listBackDTO != null && listBackDTO.getBanks() != null) {
			for (Entry<Integer, List<BankDTO>> entry : listBackDTO.getBanks().entrySet()) {
				ZaloPayPaymentProvider paymentProvider = ZaloPayPaymentProvider.fromValue(entry.getKey());
				if (paymentProvider != null) {
					List<BankDTO> bankDTOList = entry.getValue();
					map.put(paymentProvider, bankDTOList);
				}
			}
		}
		return map;
	}

	private String buildZaloPayAPI(String command) {
		return String.format("%s%s%s", baseEndpoint, StringConstant.SLASH, command);
	}

	public CreateOrderResp createOrder(CreateOrderReq createOrderReq) {
		String queryUrl = buildZaloPayAPI(ZaloPayCommandConstant.ORDER);

		Map<String, String> query = new HashMap<>();
		query.put(ZaloPayParams.APP_ID, String.valueOf(appid));
		query.put(ZaloPayParams.USER_INFO, createOrderReq.getUserId());
		String appTransactionId = String.format("%s%s%s",
			DateUtils.parseZaloPayFormat(new Date()),
			StringConstant.DASH,
			createOrderReq.getTransactionId()
		);
		query.put(ZaloPayParams.TRANSACTION_ID, appTransactionId);
		query.put(ZaloPayParams.TIME_CREATE_ORDER, String.valueOf(System.currentTimeMillis()));
		query.put(ZaloPayParams.TIME_EXPIRE_TRANSACTION, String.valueOf(expiredTimeInSeconds));
		query.put(ZaloPayParams.AMOUNT, String.valueOf(createOrderReq.getAmount()));
		query.put(ZaloPayParams.ITEMS_DETAILS, createOrderReq.getItem().toString());
		query.put(ZaloPayParams.DESCRIPTION, StringConstant.EMPTY);
		query.put(ZaloPayParams.EMBEDDED_DATA, StringConstant.EMPTY_OBJECT);
		query.put(ZaloPayParams.BANK_CODE, createOrderReq.getBankCode());
		query.put(ZaloPayParams.CALLBACK_URL, "your-callback-handler");
		query.put(ZaloPayParams.DEVICE_INFO, createOrderReq.getDeviceInfo());
		query.put(ZaloPayParams.TITLE, createOrderReq.getTitle());
		query.put(ZaloPayParams.CURRENCY, createOrderReq.getCurrency());
		query.put(ZaloPayParams.PHONE_NUMBER, createOrderReq.getPhone());
		query.put(ZaloPayParams.EMAIL, createOrderReq.getEmail());
		query.put(ZaloPayParams.ADDRESS, createOrderReq.getAddress());

		StringBuilder hashData = new StringBuilder();
		hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.USER_INFO)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.AMOUNT)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.TIME_CREATE_ORDER)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.EMBEDDED_DATA)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.ITEMS_DETAILS));

		query.put(
			ZaloPayParams.MAC_HASH,
			zaloPayCryptoService.HMACHexStringEncode(CryptoConstant.HMAC_SHA256, hashData.toString(), 1)
		);

		return httpRestClientService.post(queryUrl, query, null, null, CreateOrderResp.class);
	}

	public QueryOrderResp queryOrder(String transactionId) {
		String queryUrl = buildZaloPayAPI(ZaloPayCommandConstant.GET_ORDER_STATUS);

		Map<String, String> query = new HashMap<>();
		query.put(ZaloPayParams.APP_ID, String.valueOf(appid));
		query.put(ZaloPayParams.TRANSACTION_ID, transactionId);

		StringBuilder hashData = new StringBuilder();
		hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(zaloPayCryptoService.getKey1());

		query.put(
			ZaloPayParams.MAC_HASH,
			zaloPayCryptoService.HMACHexStringEncode(CryptoConstant.HMAC_SHA256, hashData.toString(), 1)
		);

		return httpRestClientService.post(queryUrl, query, null, null, QueryOrderResp.class);
	}

	public RefundOrderResp refundOrder(RefundOrderReq refundOrderReq) {
		String queryUrl = buildZaloPayAPI(ZaloPayCommandConstant.REFUND);

		Map<String, String> query = new HashMap<>();
		query.put(ZaloPayParams.REFUND_ID, refundOrderReq.getRefundId());
		query.put(ZaloPayParams.APP_ID, String.valueOf(appid));
		query.put(ZaloPayParams.REFUND_TRANSACTION_ID, refundOrderReq.getZaloPayTransactionId());
		query.put(ZaloPayParams.AMOUNT, String.valueOf(refundOrderReq.getAmount()));
		if (refundOrderReq.getRefundFeeAmount() > 0) {
			query.put(ZaloPayParams.REFUND_FEE, String.valueOf(refundOrderReq.getRefundFeeAmount()));
		}
		query.put(ZaloPayParams.REFUND_TIME, String.valueOf(System.currentTimeMillis()));
		query.put(ZaloPayParams.DESCRIPTION, StringConstant.EMPTY);

		StringBuilder hashData = new StringBuilder();
		hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.REFUND_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.AMOUNT)).append(StringConstant.VERTICAL_BAR);
		if (refundOrderReq.getRefundFeeAmount() > 0) {
			hashData.append(query.get(ZaloPayParams.REFUND_FEE)).append(StringConstant.VERTICAL_BAR);
		}
		hashData.append(query.get(ZaloPayParams.DESCRIPTION)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.REFUND_TIME));

		query.put(
			ZaloPayParams.MAC_HASH,
			zaloPayCryptoService.HMACHexStringEncode(CryptoConstant.HMAC_SHA256, hashData.toString(), 1)
		);

		return httpRestClientService.post(queryUrl, query, null, null, RefundOrderResp.class);
	}

	public QueryRefundResp queryRefund(String refundId) {
		String queryUrl = buildZaloPayAPI(ZaloPayCommandConstant.GET_REFUND_STATUS);

		Map<String, String> query = new HashMap<>();
		query.put(ZaloPayParams.APP_ID, String.valueOf(appid));
		query.put(ZaloPayParams.REFUND_ID, refundId);
		query.put(ZaloPayParams.REFUND_TIME, String.valueOf(System.currentTimeMillis()));

		StringBuilder hashData = new StringBuilder();
		hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.REFUND_ID)).append(StringConstant.VERTICAL_BAR);
		hashData.append(query.get(ZaloPayParams.REFUND_TIME));

		query.put(
			ZaloPayParams.MAC_HASH,
			zaloPayCryptoService.HMACHexStringEncode(CryptoConstant.HMAC_SHA256, hashData.toString(), 1)
		);
		return httpRestClientService.post(queryUrl, query, null, null, QueryRefundResp.class);
	}

}
