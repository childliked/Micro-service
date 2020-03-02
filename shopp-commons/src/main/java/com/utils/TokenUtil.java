package com.utils;

import java.util.UUID;

import com.constants.Constants;

public class TokenUtil {

	public final static String getMemberToken() {

		return Constants.TOKEN_MEMBER + "-" + UUID.randomUUID();
	}
	
	public final static String getPayToken() {
		
		return Constants.TOKEN_PAY + "-" + UUID.randomUUID();
	}
}
