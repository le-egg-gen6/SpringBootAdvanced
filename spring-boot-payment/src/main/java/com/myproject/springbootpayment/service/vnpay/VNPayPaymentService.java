package com.myproject.springbootpayment.service.vnpay;

import com.myproject.springbootpayment.abstracts.core.AbstractPaymentService;
import com.myproject.springbootpayment.abstracts.processor.CreateOrderProcessor;
import com.myproject.springbootpayment.abstracts.processor.QueryOrderStatusProcessor;
import com.myproject.springbootpayment.abstracts.processor.RefundOrderProcessor;
import com.myproject.springbootpayment.service.HttpRestClientService;
import com.myproject.springbootpayment.service.vnpay.dto.req.VNPayCreateOrderReq;
import com.myproject.springbootpayment.service.vnpay.dto.req.VNPayQueryOrderStatusReq;
import com.myproject.springbootpayment.service.vnpay.dto.req.VNPayRefundOrderReq;
import com.myproject.springbootpayment.service.vnpay.dto.resp.VNPayCreateOrderResp;
import com.myproject.springbootpayment.service.vnpay.dto.resp.VNPayQueryOrderStatusResp;
import com.myproject.springbootpayment.service.vnpay.dto.resp.VNPayRefundOrderResp;
import com.myproject.springbootpayment.shared.constant.StringConstant;
import com.myproject.springbootpayment.shared.constant.vnpay.VNPayCommand;
import com.myproject.springbootpayment.shared.constant.vnpay.VNPayParams;
import com.myproject.springbootpayment.shared.enums.CryptoAlgorithm;
import com.myproject.springbootpayment.utils.DateUtils;
import com.myproject.springbootpayment.utils.IPUtils;
import com.myproject.springbootpayment.utils.RequestUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 10:40 PM Tue 4/22/2025
 */
@Service
public class VNPayPaymentService extends AbstractPaymentService implements
        CreateOrderProcessor<VNPayCreateOrderResp, VNPayCreateOrderReq>,
        QueryOrderStatusProcessor<VNPayQueryOrderStatusResp, VNPayQueryOrderStatusReq>,
        RefundOrderProcessor<VNPayRefundOrderResp, VNPayRefundOrderReq> {

    @Value("${payment.vnpay.version}")
    private String version;

    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode;

    @Value("${payment.vnpay.pay-url}")
    private String payUrl;

    @Value("${payment.vnpay.api-url}")
    private String apiUrl;

    @Value("${payment.vnpay.expired-time-in-seconds}")
    private long expiredTimeInSeconds;

    @Autowired
    public VNPayPaymentService(
            VNPayCryptoService cryptoService,
            HttpRestClientService httpRestClientService
    ) {
        this.cryptoService = cryptoService;
        this.httpRestClientService = httpRestClientService;
    }

    @Override
    public VNPayCreateOrderResp createOrder(VNPayCreateOrderReq vnPayCreateOrderReq) {
        Map<String, String> query = new HashMap<>();
        query.put(VNPayParams.VERSION, version);
        query.put(VNPayParams.COMMAND, VNPayCommand.CREATE_ORDER);
        query.put(VNPayParams.TMN_CODE, tmnCode);
        query.put(VNPayParams.AMOUNT, String.valueOf(vnPayCreateOrderReq.getAmount()));
        query.put(VNPayParams.BANK_CODE, vnPayCreateOrderReq.getBankCode());
        query.put(VNPayParams.CREATED_DATE, DateUtils.parseVNPAYFormat(
                new Date(vnPayCreateOrderReq.getCreatedTime())
        ));
        query.put(VNPayParams.CURRENCY, vnPayCreateOrderReq.getCurrency());
        query.put(VNPayParams.IP_ADDRESS, vnPayCreateOrderReq.getIpAddress());
        query.put(VNPayParams.LOCALE, vnPayCreateOrderReq.getLanguage());
        query.put(VNPayParams.ORDER_INFO, vnPayCreateOrderReq.getOrderInfo());
        query.put(VNPayParams.ORDER_TYPE, vnPayCreateOrderReq.getOrderType());
        query.put(VNPayParams.RETURN_URL, StringConstant.EMPTY);
        query.put(VNPayParams.TRANSACTION_EXPIRE_DATE, DateUtils.parseVNPAYFormat(
                new Date(vnPayCreateOrderReq.getCreatedTime() + expiredTimeInSeconds)
        ));
        query.put(VNPayParams.APP_TRANSACTION_ID, vnPayCreateOrderReq.getAppTransactionId());

        StringBuilder hashData = new StringBuilder();
        List<String> sortedKeys = getSortedParamAsc(query);
        Iterator<String> iterator = sortedKeys.iterator();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            String fieldValue = query.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append(StringConstant.EQUALS)
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (iterator.hasNext()) {
                    hashData.append(StringConstant.AND);
                }
            }
        }
        query.put(
                VNPayParams.SECURE_HASH,
                cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA512, hashData.toString(), 1)
        );

        return new VNPayCreateOrderResp(RequestUtils.buildUriStr(payUrl, query));
    }

    @Override
    public VNPayQueryOrderStatusResp queryOrderStatus(VNPayQueryOrderStatusReq vnPayQueryOrderStatusReq) {
        Map<String, String> query = new HashMap<>();
        query.put(VNPayParams.VERSION, version);
        query.put(VNPayParams.COMMAND, VNPayCommand.QUERY_ORDER_STATUS);
        query.put(VNPayParams.TMN_CODE, tmnCode);
        query.put(VNPayParams.REQUEST_ID, vnPayQueryOrderStatusReq.getRequestId());
        query.put(VNPayParams.APP_TRANSACTION_ID, vnPayQueryOrderStatusReq.getAppTransactionId());
        query.put(VNPayParams.ORDER_INFO, vnPayQueryOrderStatusReq.getOrderInfo());
        query.put(VNPayParams.VNPAY_TRANSACTION_ID, vnPayQueryOrderStatusReq.getVnpayTransactionId());
        query.put(VNPayParams.APP_RECOGNIZED_TRANSACTION_DATE,
                DateUtils.parseVNPAYFormat(new Date(vnPayQueryOrderStatusReq.getCreatedTime()))
        );
        query.put(VNPayParams.CREATED_DATE, DateUtils.parseVNPAYFormat(
                new Date(DateUtils.getVietnamTimestamp())
        ));
        query.put(VNPayParams.IP_ADDRESS, IPUtils.getCurrentMachineIPAddress());

        StringBuilder hashData = new StringBuilder();
        hashData.append(query.get(VNPayParams.REQUEST_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.VERSION)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.COMMAND)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.TMN_CODE)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.APP_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.APP_RECOGNIZED_TRANSACTION_DATE)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.CREATED_DATE)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.IP_ADDRESS)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.ORDER_INFO));

        query.put(
                VNPayParams.SECURE_HASH,
                cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA512, hashData.toString(), 1)
        );
        return httpRestClientService.post(apiUrl, query, null, null, VNPayQueryOrderStatusResp.class);
    }

    @Override
    public VNPayRefundOrderResp refund(VNPayRefundOrderReq vnPayRefundOrderReq) {
        Map<String, String> query = new HashMap<>();
        query.put(VNPayParams.VERSION, version);
        query.put(VNPayParams.TMN_CODE, tmnCode);
        query.put(VNPayParams.COMMAND, VNPayCommand.REFUND_ORDER);
        query.put(VNPayParams.REQUEST_ID, vnPayRefundOrderReq.getRequestId());
        query.put(VNPayParams.TRANSACTION_TYPE, vnPayRefundOrderReq.getTransactionType());
        query.put(VNPayParams.APP_TRANSACTION_ID, vnPayRefundOrderReq.getAppTransactionId());
        query.put(VNPayParams.AMOUNT, String.valueOf(vnPayRefundOrderReq.getAmount()));
        query.put(VNPayParams.ORDER_INFO, vnPayRefundOrderReq.getOrderInfo());
        query.put(VNPayParams.VNPAY_TRANSACTION_ID, vnPayRefundOrderReq.getVnpayTransactionId());
        query.put(VNPayParams.APP_RECOGNIZED_TRANSACTION_DATE, DateUtils.parseVNPAYFormat(
                new Date(vnPayRefundOrderReq.getCreatedTime())
        ));
        query.put(VNPayParams.CREATED_BY, vnPayRefundOrderReq.getCreatedBy());
        query.put(VNPayParams.CREATED_DATE, DateUtils.parseVNPAYFormat(
                new Date(DateUtils.getVietnamTimestamp())
        ));
        query.put(VNPayParams.IP_ADDRESS, IPUtils.getCurrentMachineIPAddress());

        StringBuilder hashData = new StringBuilder();
        hashData.append(query.get(VNPayParams.REQUEST_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.VERSION)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.COMMAND)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.TMN_CODE)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.TRANSACTION_TYPE)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.APP_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.AMOUNT)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.VNPAY_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.APP_RECOGNIZED_TRANSACTION_DATE)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.CREATED_BY)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.CREATED_DATE)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.IP_ADDRESS)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(VNPayParams.ORDER_INFO));

        query.put(
                VNPayParams.SECURE_HASH,
                cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA512, hashData.toString(), 1)
        );

        return httpRestClientService.post(apiUrl, query, null, null, VNPayRefundOrderResp.class);
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
