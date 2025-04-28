package com.myproject.springbootpayment.service.momo;

import com.myproject.springbootpayment.abstracts.core.AbstractPaymentService;
import com.myproject.springbootpayment.abstracts.processor.CancelOrderProcessor;
import com.myproject.springbootpayment.abstracts.processor.ConfirmOrderProcessor;
import com.myproject.springbootpayment.abstracts.processor.CreateOrderProcessor;
import com.myproject.springbootpayment.abstracts.processor.QueryOrderStatusProcessor;
import com.myproject.springbootpayment.abstracts.processor.RefundOrderProcessor;
import com.myproject.springbootpayment.service.HttpRestClientService;
import com.myproject.springbootpayment.service.momo.dto.req.MomoConfirmCancelOrderReq;
import com.myproject.springbootpayment.service.momo.dto.req.MomoCreateOrderReq;
import com.myproject.springbootpayment.service.momo.dto.req.MomoQueryOrderStatusReq;
import com.myproject.springbootpayment.service.momo.dto.req.MomoRefundOrderReq;
import com.myproject.springbootpayment.service.momo.dto.resp.MomoConfirmCancelOrderResp;
import com.myproject.springbootpayment.service.momo.dto.resp.MomoCreateOrderResp;
import com.myproject.springbootpayment.service.momo.dto.resp.MomoQueryOrderStatusResp;
import com.myproject.springbootpayment.service.momo.dto.resp.MomoRefundOrderResp;
import com.myproject.springbootpayment.shared.constant.StringConstant;
import com.myproject.springbootpayment.shared.constant.momo.MomoCommand;
import com.myproject.springbootpayment.shared.constant.momo.MomoHashDataKey;
import com.myproject.springbootpayment.shared.constant.momo.MomoRequestType;
import com.myproject.springbootpayment.shared.enums.CryptoAlgorithm;
import com.myproject.springbootpayment.utils.EncodingUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 10:23 PM Tue 4/22/2025
 */
@Service
public class MomoPaymentService extends AbstractPaymentService implements
        CreateOrderProcessor<MomoCreateOrderResp, MomoCreateOrderReq>,
        ConfirmOrderProcessor<MomoConfirmCancelOrderResp, MomoConfirmCancelOrderReq>,
        CancelOrderProcessor<MomoConfirmCancelOrderResp, MomoConfirmCancelOrderReq>,
        QueryOrderStatusProcessor<MomoQueryOrderStatusResp, MomoQueryOrderStatusReq>,
        RefundOrderProcessor<MomoRefundOrderResp, MomoRefundOrderReq>
{

    @Value("${payment.momopay.partner-code}")
    private String partnerCode;

    @Value("${payment.momopay.partner-name}")
    private String partnerName;

    @Value("${payment.momopay.store-id}")
    private String storeId;

    @Value("${payment.momopay.base-endpoint}")
    private String baseUrl;

    @Autowired
    public MomoPaymentService(
            MomoCryptoService cryptoService,
            HttpRestClientService httpRestClientService
    ) {
        this.cryptoService = cryptoService;
        this.httpRestClientService = httpRestClientService;
    }

    @Override
    public MomoCreateOrderResp createOrder(MomoCreateOrderReq momoCreateOrderReq) {
        String url  =buildUrl(baseUrl, MomoCommand.CREATE_ORDER);

        momoCreateOrderReq.setPartnerCode(partnerCode);
        momoCreateOrderReq.setPartnerName(partnerName);
        momoCreateOrderReq.setStoreId(storeId);
        momoCreateOrderReq.setRequestType(MomoRequestType.CAPTURE_WALLET);

        String extraDataBeforeEncode = momoCreateOrderReq.getExtraData();
        momoCreateOrderReq.setExtraData(EncodingUtils.base64Encode(extraDataBeforeEncode));

        Map<String, String> hashData = new HashMap<>();
        hashData.put(MomoHashDataKey.ACCESS_KEY, cryptoService.getKey(1));
        hashData.put(MomoHashDataKey.AMOUNT, String.valueOf(momoCreateOrderReq.getAmount()));
        hashData.put(MomoHashDataKey.EXTRA_DATA, momoCreateOrderReq.getExtraData());
        hashData.put(MomoHashDataKey.IPN_URL, momoCreateOrderReq.getIpnUrl());
        hashData.put(MomoHashDataKey.ORDER_ID, momoCreateOrderReq.getOrderId());
        hashData.put(MomoHashDataKey.ORDER_INFO, momoCreateOrderReq.getOrderInfo());
        hashData.put(MomoHashDataKey.PARTNER_CODE, momoCreateOrderReq.getPartnerCode());
        hashData.put(MomoHashDataKey.REDIRECT_URL, momoCreateOrderReq.getRedirectUrl());
        hashData.put(MomoHashDataKey.REQUEST_ID, momoCreateOrderReq.getRequestId());
        hashData.put(MomoHashDataKey.REQUEST_TYPE, momoCreateOrderReq.getRequestType());

        momoCreateOrderReq.setSignature(buildSignature(hashData));
        return httpRestClientService.post(url, null, null, momoCreateOrderReq, MomoCreateOrderResp.class);
    }

    @Override
    public MomoConfirmCancelOrderResp confirmOrder(MomoConfirmCancelOrderReq momoConfirmCancelOrderReq) {
        String url = buildUrl(baseUrl, MomoCommand.CONFIRM_ORDER);

        momoConfirmCancelOrderReq.setPartnerCode(partnerCode);
		momoConfirmCancelOrderReq.setRequestType(MomoRequestType.CONFIRM_ORDER);

		Map<String, String> hashData = new HashMap<>();
		hashData.put(MomoHashDataKey.ACCESS_KEY, cryptoService.getKey(1));
		hashData.put(MomoHashDataKey.AMOUNT, String.valueOf(momoConfirmCancelOrderReq.getAmount()));
		hashData.put(MomoHashDataKey.DESCRIPTION, momoConfirmCancelOrderReq.getDescription());
		hashData.put(MomoHashDataKey.ORDER_ID, momoConfirmCancelOrderReq.getOrderId());
		hashData.put(MomoHashDataKey.PARTNER_CODE, momoConfirmCancelOrderReq.getPartnerCode());
		hashData.put(MomoHashDataKey.REQUEST_ID, momoConfirmCancelOrderReq.getRequestId());
		hashData.put(MomoHashDataKey.REQUEST_TYPE, momoConfirmCancelOrderReq.getRequestType());

		momoConfirmCancelOrderReq.setSignature(buildSignature(hashData));
		return httpRestClientService.post(url, null, null, momoConfirmCancelOrderReq, MomoConfirmCancelOrderResp.class);
    }

    @Override
    public MomoConfirmCancelOrderResp cancelOrder(MomoConfirmCancelOrderReq momoConfirmCancelOrderReq) {
	    String url = buildUrl(baseUrl, MomoCommand.CONFIRM_ORDER);

	    momoConfirmCancelOrderReq.setPartnerCode(partnerCode);
	    momoConfirmCancelOrderReq.setRequestType(MomoRequestType.CANCEL_ORDER);

	    Map<String, String> hashData = new HashMap<>();
	    hashData.put(MomoHashDataKey.ACCESS_KEY, cryptoService.getKey(1));
	    hashData.put(MomoHashDataKey.AMOUNT, String.valueOf(momoConfirmCancelOrderReq.getAmount()));
	    hashData.put(MomoHashDataKey.DESCRIPTION, momoConfirmCancelOrderReq.getDescription());
	    hashData.put(MomoHashDataKey.ORDER_ID, momoConfirmCancelOrderReq.getOrderId());
	    hashData.put(MomoHashDataKey.PARTNER_CODE, momoConfirmCancelOrderReq.getPartnerCode());
	    hashData.put(MomoHashDataKey.REQUEST_ID, momoConfirmCancelOrderReq.getRequestId());
	    hashData.put(MomoHashDataKey.REQUEST_TYPE, momoConfirmCancelOrderReq.getRequestType());

	    momoConfirmCancelOrderReq.setSignature(buildSignature(hashData));
	    return httpRestClientService.post(url, null, null, momoConfirmCancelOrderReq, MomoConfirmCancelOrderResp.class);
    }

    @Override
    public MomoQueryOrderStatusResp queryOrderStatus(MomoQueryOrderStatusReq momoQueryOrderStatusReq) {
        String url = buildUrl(baseUrl, MomoCommand.QUERY_ORDER_STATUS);

		momoQueryOrderStatusReq.setPartnerCode(partnerCode);

		Map<String, String> hashData = new HashMap<>();
		hashData.put(MomoHashDataKey.ACCESS_KEY, cryptoService.getKey(1));
		hashData.put(MomoHashDataKey.ORDER_ID, momoQueryOrderStatusReq.getOrderId());
		hashData.put(MomoHashDataKey.PARTNER_CODE, momoQueryOrderStatusReq.getPartnerCode());
		hashData.put(MomoHashDataKey.REQUEST_ID, momoQueryOrderStatusReq.getRequestId());

		momoQueryOrderStatusReq.setSignature(buildSignature(hashData));
		return httpRestClientService.post(url, null, null, momoQueryOrderStatusReq, MomoQueryOrderStatusResp.class);
    }

    @Override
    public MomoRefundOrderResp refund(MomoRefundOrderReq momoRefundOrderReq) {
		String url = buildUrl(baseUrl, MomoCommand.REFUND_ORDER);

		momoRefundOrderReq.setPartnerCode(partnerCode);

		Map<String, String> hashData = new HashMap<>();
		hashData.put(MomoHashDataKey.ACCESS_KEY, cryptoService.getKey(1));
		hashData.put(MomoHashDataKey.AMOUNT, String.valueOf(momoRefundOrderReq.getAmount()));
		hashData.put(MomoHashDataKey.DESCRIPTION, momoRefundOrderReq.getDescription());
		hashData.put(MomoHashDataKey.ORDER_ID, momoRefundOrderReq.getOrderId());
		hashData.put(MomoHashDataKey.PARTNER_CODE, momoRefundOrderReq.getPartnerCode());
		hashData.put(MomoHashDataKey.REQUEST_ID, momoRefundOrderReq.getRequestId());
		hashData.put(MomoHashDataKey.MOMO_TRANSACTION_ID, String.valueOf(momoRefundOrderReq.getTransId()));

		momoRefundOrderReq.setSignature(buildSignature(hashData));
		return httpRestClientService.post(url, null, null, momoRefundOrderReq, MomoRefundOrderResp.class);
    }

    private String buildSignature(Map<String, String> hashData) {
        List<String> sortedKeysAsc = getSortedParamAsc(hashData);
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = sortedKeysAsc.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = hashData.get(key);
            sb.append(key)
                .append(StringConstant.EQUALS)
                .append(value);
            if (iterator.hasNext()) {
                sb.append(StringConstant.AND);
            }
        }
        return cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA256, sb.toString(), 2);
    }

    private List<String> getSortedParamAsc(Map<String, String> params) {
        List<String> sortedParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sortedParams.add(entry.getKey());
        }
        Collections.sort(sortedParams);
        return sortedParams;
    }

    private List<String> getSortedParamDesc(Map<String, String> params) {
        List<String> sortedParams = getSortedParamAsc(params);
        Collections.reverse(sortedParams);
        return sortedParams;
    }
}
