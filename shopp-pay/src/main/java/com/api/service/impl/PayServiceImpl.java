package com.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.api.service.PayService;
import com.base.BaseApiService;
import com.base.ResponseBase;
import com.constants.Constants;
import com.dao.PaymentInfoDao;
import com.entity.PaymentInfo;
import com.utils.TokenUtil;

@RestController
public class PayServiceImpl extends BaseApiService implements PayService {

	@Autowired
	private PaymentInfoDao paymentInfodao;

	// 获取支付有效期token
	@Override
	public ResponseBase createToken(@RequestBody PaymentInfo PaymentInfo) {

		// 1.存在数据库中
		Integer savePaymentType = paymentInfodao.savePaymentType(PaymentInfo);
		if (savePaymentType <= 0) {
			return setResultError("订单提交失败....");
		}
		// 2.获取token
		String payToken = TokenUtil.getPayToken();
		// 3.token存在redis中
		baseRedisService.setString(payToken, PaymentInfo.getId() + "", Constants.PAY_REDIS_TIME);
		// 4.返回token
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("payToken", payToken);
		return setResultSuccess(jsonObject);
	}

	// 获取token返回参数给支付页面
	@Override
	public ResponseBase findPayToken(@RequestParam("payToken") String payToken) {

		// 1.验证参数
		if (StringUtils.isEmpty(payToken)) {
			return setResultError("tokne不能为空");
		}
		// 2.查看token是否过期
		String payId = (String) baseRedisService.getString(payToken);
		if (StringUtils.isEmpty(payId)) {
			return setResultError("支付请求已经超时...");
		}
		// 3.token换取订单信息
		long strPayId = Long.parseLong(payId);
		PaymentInfo paymentInfo = paymentInfodao.getPaymentInfo(strPayId);
		if (paymentInfo == null) {
			return setResultError("未找到支付信息...");
		}
		// 4.对接支付代码,将信息变成form表单提交格式
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
				AlipayConfig.sign_type);

		// 设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = paymentInfo.getOrderId();
		// 付款金额，必填 企业金额
		String total_amount = paymentInfo.getPrice() + "";
		// 订单名称，必填
		String subject = "曹文琦充值会员";
		// 商品描述，可空
		// String body = new
		// String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");

		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
				+ "\"," + "\"subject\":\"" + subject + "\","
				// + "\"body\":\""+ body +"\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		// 若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		// alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no
		// +"\","
		// + "\"total_amount\":\""+ total_amount +"\","
		// + "\"subject\":\""+ subject +"\","
		// + "\"body\":\""+ body +"\","
		// + "\"timeout_express\":\"10m\","
		// + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		// 请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

		// 5.请求返回给客户端
		try {
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("payHtml", result);
			return setResultSuccess(jSONObject);
		} catch (AlipayApiException e) {
			return setResultError("支付异常...");
		}
	}

}
