package com.myproject.springbootpayment.service.momo;

import com.myproject.springbootpayment.abstracts.core.AbstractCryptoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 10:20 PM Tue 4/22/2025
 */
@Service
public class MomoCryptoService extends AbstractCryptoService {

    @Value("${payment.momopay.access-key}")
    private String accessKey;

    @Value("${payment.momopay.secret-key}")
    private String secretKey;

    @PostConstruct
    @Override
    public void initialize() {
        initMac(accessKey, secretKey);
    }

}
