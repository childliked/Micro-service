package com.adapter;

import com.alibaba.fastjson.JSONObject;

public interface MessageAdapter {

	public void sendMessage(JSONObject body);
}
