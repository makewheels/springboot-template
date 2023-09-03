package com.github.makewheels.springboottemplate.alipay;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.Client;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AlipayService {

    public String createOrder(String subject, String outTradeNo, BigDecimal totalAmount, String buyerId) {
        try {
            return Factory.Payment.Common()
                    .create(subject, outTradeNo, String.valueOf(totalAmount), buyerId)
                    .getTradeNo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AlipayTradeQueryResponse queryOrder(String outTradeNo) {
        try {
            Client client = Factory.Payment.Common();
            return client.query(outTradeNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
