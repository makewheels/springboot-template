package com.github.makewheels.springboottemplate.alipay;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.Client;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AlipayService {
    static {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";
        config.appId = "2018021402198213";
        config.merchantPrivateKey = "<-- 请填写您的应用私钥，例如：MIIEvQIBADANB ... ... -->";
        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        // config.alipayPublicKey = "<-- 请填写您的支付宝公钥，例如：MIIBIjANBg... -->";
//        config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
        Factory.setOptions(config);
    }


    public String createOrder(String subject, String outTradeNo, BigDecimal totalAmount) {
        try {
            return Factory.Payment.Page()
                    .pay(subject, outTradeNo, String.valueOf(totalAmount), null).getBody();
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
