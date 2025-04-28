package com.myproject.springbootpayment.service.zalopay;

import com.myproject.springbootpayment.abstracts.core.AbstractPaymentService;
import com.myproject.springbootpayment.abstracts.processor.CreateOrderProcessor;
import com.myproject.springbootpayment.abstracts.processor.QueryOrderStatusProcessor;
import com.myproject.springbootpayment.abstracts.processor.QueryRefundStatusProcessor;
import com.myproject.springbootpayment.abstracts.processor.RefundOrderProcessor;
import com.myproject.springbootpayment.service.HttpRestClientService;
import com.myproject.springbootpayment.service.zalopay.dto.BankDTO;
import com.myproject.springbootpayment.service.zalopay.dto.ListBackDTO;
import com.myproject.springbootpayment.service.zalopay.dto.req.ZaloPayCreateOrderReq;
import com.myproject.springbootpayment.service.zalopay.dto.req.ZaloPayQueryOrderStatusReq;
import com.myproject.springbootpayment.service.zalopay.dto.req.ZaloPayQueryRefundStatusReq;
import com.myproject.springbootpayment.service.zalopay.dto.req.ZaloPayRefundOrderReq;
import com.myproject.springbootpayment.service.zalopay.dto.resp.ZaloPayCreateOrderResp;
import com.myproject.springbootpayment.service.zalopay.dto.resp.ZaloPayQueryOrderStatusResp;
import com.myproject.springbootpayment.service.zalopay.dto.resp.ZaloPayQueryRefundStatusResp;
import com.myproject.springbootpayment.service.zalopay.dto.resp.ZaloPayRefundOrderResp;
import com.myproject.springbootpayment.shared.constant.StringConstant;
import com.myproject.springbootpayment.shared.constant.zalopay.ZaloPayCommand;
import com.myproject.springbootpayment.shared.constant.zalopay.ZaloPayParams;
import com.myproject.springbootpayment.shared.constant.zalopay.ZaloPayPaymentProvider;
import com.myproject.springbootpayment.shared.enums.CryptoAlgorithm;
import com.myproject.springbootpayment.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nguyenle
 * @since 5:21 PM Tue 4/22/2025
 */
@Service
public class ZaloPayPaymentService extends AbstractPaymentService implements
        CreateOrderProcessor<ZaloPayCreateOrderResp, ZaloPayCreateOrderReq>,
        QueryOrderStatusProcessor<ZaloPayQueryOrderStatusResp, ZaloPayQueryOrderStatusReq>,
        RefundOrderProcessor<ZaloPayRefundOrderResp, ZaloPayRefundOrderReq>,
        QueryRefundStatusProcessor<ZaloPayQueryRefundStatusResp, ZaloPayQueryRefundStatusReq>
{

    @Value("${payment.zalopay.appid}")
    private String appid;

    @Value("${payment.zalopay.base-endpoint}")
    private String baseUrl;

    @Value("${payment.zalopay.list-merchant-bank}")
    private String listMerchantBank;

    @Value("${payment.zalopay.expired-time-in-seconds}")
    private long expiredTimeInSeconds;

    @Autowired
    public ZaloPayPaymentService(ZaloPayCryptoService cryptoService, HttpRestClientService httpRestClientService) {
        this.cryptoService = cryptoService;
        this.httpRestClientService = httpRestClientService;
    }

    public Map<ZaloPayPaymentProvider, List<BankDTO>> getListAvailableBank() {
        String reqtime = String.valueOf(DateUtils.getUTCTimestamp());
        String data = appid + StringConstant.VERTICAL_BAR + reqtime;

        Map<String, String> query = new HashMap<>();
        query.put("appid", String.valueOf(appid));
        query.put("reqtime", reqtime);
        query.put("data", cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA256, data, 1));

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        ListBackDTO listBackDTO = httpRestClientService.post(listMerchantBank, query, header, null, ListBackDTO.class);

        Map<ZaloPayPaymentProvider, List<BankDTO>> map = new HashMap<>();
        if (listBackDTO != null && listBackDTO.getBanks() != null) {
            for (Map.Entry<Integer, List<BankDTO>> entry : listBackDTO.getBanks().entrySet()) {
                ZaloPayPaymentProvider paymentProvider = ZaloPayPaymentProvider.fromValue(entry.getKey());
                if (paymentProvider != null) {
                    List<BankDTO> bankDTOList = entry.getValue();
                    map.put(paymentProvider, bankDTOList);
                }
            }
        }
        return map;
    }

    @Override
    public ZaloPayCreateOrderResp createOrder(ZaloPayCreateOrderReq zaloPayCreateOrderReq) {
        String url = buildUrl(baseUrl, ZaloPayCommand.CREATE_ORDER);
        Map<String, String> query = new HashMap<>();
        query.put(ZaloPayParams.APP_ID, appid);
        query.put(ZaloPayParams.USER_INFO, zaloPayCreateOrderReq.getUserId());
        String appTransactionId = String.format("%s%s%s",
                DateUtils.parseZaloPayFormat(new Date()),
                StringConstant.DASH,
                zaloPayCreateOrderReq.getTransactionId()
        );
        query.put(ZaloPayParams.APP_TRANSACTION_ID, appTransactionId);
        query.put(ZaloPayParams.TIME_CREATE_ORDER, String.valueOf(DateUtils.getUTCTimestamp()));
        query.put(ZaloPayParams.TIME_EXPIRE_TRANSACTION, String.valueOf(expiredTimeInSeconds));
        query.put(ZaloPayParams.AMOUNT, String.valueOf(zaloPayCreateOrderReq.getAmount()));
        query.put(ZaloPayParams.ITEMS_DETAILS, zaloPayCreateOrderReq.getItem().toString());
        query.put(ZaloPayParams.DESCRIPTION, StringConstant.EMPTY);
        query.put(ZaloPayParams.EMBEDDED_DATA, StringConstant.EMPTY_OBJECT);
        query.put(ZaloPayParams.BANK_CODE, zaloPayCreateOrderReq.getBankCode());
        query.put(ZaloPayParams.CALLBACK_URL, "your-callback-handler");
        query.put(ZaloPayParams.DEVICE_INFO, zaloPayCreateOrderReq.getDeviceInfo());
        query.put(ZaloPayParams.TITLE, zaloPayCreateOrderReq.getTitle());
        query.put(ZaloPayParams.CURRENCY, zaloPayCreateOrderReq.getCurrency());
        query.put(ZaloPayParams.PHONE_NUMBER, zaloPayCreateOrderReq.getPhone());
        query.put(ZaloPayParams.EMAIL, zaloPayCreateOrderReq.getEmail());
        query.put(ZaloPayParams.ADDRESS, zaloPayCreateOrderReq.getAddress());

        StringBuilder hashData = new StringBuilder();
        hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.APP_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.USER_INFO)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.AMOUNT)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.TIME_CREATE_ORDER)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.EMBEDDED_DATA)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.ITEMS_DETAILS));

        query.put(
                ZaloPayParams.MAC_HASH,
                cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA256, hashData.toString(), 1)
        );

        return httpRestClientService.post(url, query, null, null, ZaloPayCreateOrderResp.class);
    }

    @Override
    public ZaloPayQueryOrderStatusResp queryOrderStatus(ZaloPayQueryOrderStatusReq zaloPayQueryOrderStatusReq) {
        String url = buildUrl(baseUrl, ZaloPayCommand.GET_ORDER_STATUS);

        Map<String, String> query = new HashMap<>();
        query.put(ZaloPayParams.APP_ID, appid);
        query.put(ZaloPayParams.APP_TRANSACTION_ID, zaloPayQueryOrderStatusReq.getAppTransactionId());

        StringBuilder hashData = new StringBuilder();
        hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.APP_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(cryptoService.getKey(1));

        query.put(
                ZaloPayParams.MAC_HASH,
                cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA256, hashData.toString(), 1)
        );
        return httpRestClientService.post(url, query, null, null, ZaloPayQueryOrderStatusResp.class);
    }

    @Override
    public ZaloPayRefundOrderResp refund(ZaloPayRefundOrderReq zaloPayRefundOrderReq) {
        String url = buildUrl(baseUrl, ZaloPayCommand.REFUND_ORDER);

        Map<String, String> query = new HashMap<>();
        query.put(ZaloPayParams.APP_REQUEST_REFUND_ID, zaloPayRefundOrderReq.getRefundId());
        query.put(ZaloPayParams.APP_ID, appid);
        query.put(ZaloPayParams.ZALOPAY_TRANSACTION_ID, zaloPayRefundOrderReq.getZaloPayTransactionId());
        query.put(ZaloPayParams.AMOUNT, String.valueOf(zaloPayRefundOrderReq.getAmount()));
        if (zaloPayRefundOrderReq.getRefundFeeAmount() > 0) {
            query.put(ZaloPayParams.REFUND_FEE, String.valueOf(zaloPayRefundOrderReq.getRefundFeeAmount()));
        }
        query.put(ZaloPayParams.REFUND_TIME, String.valueOf(DateUtils.getUTCTimestamp()));
        query.put(ZaloPayParams.DESCRIPTION, StringConstant.EMPTY);

        StringBuilder hashData = new StringBuilder();
        hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.ZALOPAY_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.AMOUNT)).append(StringConstant.VERTICAL_BAR);
        if (zaloPayRefundOrderReq.getRefundFeeAmount() > 0) {
            hashData.append(query.get(ZaloPayParams.REFUND_FEE)).append(StringConstant.VERTICAL_BAR);
        }
        hashData.append(query.get(ZaloPayParams.DESCRIPTION)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.REFUND_TIME));

        query.put(
                ZaloPayParams.MAC_HASH,
                cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA256, hashData.toString(), 1)
        );

        return httpRestClientService.post(url, query, null, null, ZaloPayRefundOrderResp.class);
    }

    @Override
    public ZaloPayQueryRefundStatusResp queryRefundStatus(ZaloPayQueryRefundStatusReq zaloPayQueryRefundStatusReq) {
        String url = buildUrl(baseUrl, ZaloPayCommand.GET_REFUND_STATUS);

        Map<String, String> query = new HashMap<>();
        query.put(ZaloPayParams.APP_ID, appid);
        query.put(ZaloPayParams.APP_REQUEST_REFUND_ID, zaloPayQueryRefundStatusReq.getRefundTransactionId());
        query.put(ZaloPayParams.REFUND_TIME, String.valueOf(DateUtils.getUTCTimestamp()));

        StringBuilder hashData = new StringBuilder();
        hashData.append(query.get(ZaloPayParams.APP_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.ZALOPAY_TRANSACTION_ID)).append(StringConstant.VERTICAL_BAR);
        hashData.append(query.get(ZaloPayParams.REFUND_TIME));

        query.put(
                ZaloPayParams.MAC_HASH,
                cryptoService.HMACHexStringEncode(CryptoAlgorithm.HMAC_SHA256, hashData.toString(), 1)
        );
        return httpRestClientService.post(url, query, null, null, ZaloPayQueryRefundStatusResp.class);
    }
}
