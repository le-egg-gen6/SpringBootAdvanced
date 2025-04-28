package com.myproject.springbootpayment.service.vnpay;

import com.myproject.springbootpayment.abstracts.core.AbstractCryptoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 10:39 PM Tue 4/22/2025
 */
@Service
public class VNPayCryptoService extends AbstractCryptoService {

    @Value("${payment.momopay.secret-key}")
    private String secretKey;

    @PostConstruct
    @Override
    public void initialize() {
        initMac(secretKey);
    }

}
