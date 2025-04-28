package com.myproject.springbootpayment.controller;

import com.myproject.springbootpayment.service.momo.MomoPaymentService;
import com.myproject.springbootpayment.service.vnpay.VNPayPaymentService;
import com.myproject.springbootpayment.service.zalopay.ZaloPayPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nguyenle
 * @since 9:41 AM Mon 4/28/2025
 */
@RestController("/ipn-receiver")
@RequiredArgsConstructor
public class InstancePaymentNotificationReceiver {

	private final MomoPaymentService momoPaymentService;

	private final VNPayPaymentService vnpayPaymentService;

	private final ZaloPayPaymentService zalopayPaymentService;


}
