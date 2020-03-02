package com.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.constants.Constants;

//统一返回参数封装
@Component
public class BaseApiService {
	
	@Autowired
	protected BaseRedisService baseRedisService;

	//通用封装
	public ResponseBase setResult(Integer rtnCode, String msg, Object data) {
		return new ResponseBase(rtnCode, msg, data);
	}
	//返回成功，可以传date。
	public ResponseBase setResultSuccess(Object data) {
		return new ResponseBase(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE, data);
	}
	//返回成功，可以传msg。
	public ResponseBase setResultSuccess(String msg) {
		return new ResponseBase(Constants.HTTP_RES_CODE_200,msg, null);
	}
	//返回成功，不传参数。
	public ResponseBase setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE, null);
	}
	
	//返回失败，可以传msg。
	public ResponseBase setResultError(String msg) {
		return new ResponseBase(Constants.HTTP_RES_CODE_500,msg, null);
	}
	//返回失败，可以传msg。
	public ResponseBase setResultError(Integer rtnCode,String msg) {
		return new ResponseBase(rtnCode,msg, null);
	}
	
	
}
