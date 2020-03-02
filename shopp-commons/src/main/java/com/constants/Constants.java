package com.constants;

//baseApiService中的常量
public interface Constants {
	// 响应请求成功
		String HTTP_RES_CODE_200_VALUE = "success";
		// 系统错误
		String HTTP_RES_CODE_500_VALUE = "fial";
		// 响应请求成功code
		Integer HTTP_RES_CODE_200 = 200;
		// 系统错误
		Integer HTTP_RES_CODE_500 = 500;
		// 发送邮件
		String MSG_EMAIL ="email";
		//membertoken常量
		String TOKEN_MEMBER = "chenchuanjie";
		//paytoken常量
		String TOKEN_PAY = "chendashu";
		//移动端登陆tokenredis有效期
		Long REDIS_TIME = 60*60*24*90l;
		//移动端登陆cookie有效期
		int COOKIE_TOKEN_MEMBER_TIME = 60 * 60 * 24 * 90;
		//支付端订单tokenredis有效期
		Long PAY_REDIS_TIME = 60*15l;
		// cookie 会员 totoken 名称
		String COOKIE_MEMBER_TOKEN = "cookie_member_token";
		// 未关联QQ账号
		Integer HTTP_RES_CODE_201 = 201;
		String PAY_FAIL = "fail";
		String PAY_SUCCESS = "success";
}
