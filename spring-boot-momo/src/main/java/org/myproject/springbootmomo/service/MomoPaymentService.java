package org.myproject.springbootmomo.service;

import lombok.RequiredArgsConstructor;
import org.myproject.springbootmomo.shared.constant.StringConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 1:06 AM Tue 4/22/2025
 */
@Service
@RequiredArgsConstructor
public class MomoPaymentService {

    @Value("${payment.momo.partner-code}")
    private String partnerCode;

    @Value("${payment.momo.base-endpoint}")
    private String baseEndpoint;

    private final HttpRestClientService httpRestClientService;

    private final MomoCryptoService momoCryptoService;

    public String buildMomoPaymentAPI(String command) {
        return String.format("%s%s%s", baseEndpoint, StringConstant.SLASH, command);
    }



}
