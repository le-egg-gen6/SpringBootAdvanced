package com.myproject.springbootvnpay.service;

import com.myproject.springbootvnpay.dto.request.InitPaymentRequest;
import com.myproject.springbootvnpay.dto.response.InitPaymentResponse;
import com.myproject.springbootvnpay.shared.constant.VNPAYCommandConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYCoreConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYParams;
import com.myproject.springbootvnpay.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author nguyenle
 * @since 11:26 AM Mon 3/10/2025
 */
@Service
@RequiredArgsConstructor
public class VNPayPaymentService {

    private final VNPayCryptoService cryptoService;

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
        params.put(VNPAYParams.AMOUNT, String.valueOf(amount));
        params.put(VNPAYParams.TXN_REF, txnRef);


    }

    private String buildReturnUrl(String txnRef) {
        return String.format(returnUrlFormat, txnRef);
    }

    private String buildPaymentDetails(InitPaymentRequest initPaymentRequest) {
        return String.format("Test %s", initPaymentRequest.getTxnRef());
    }

}
