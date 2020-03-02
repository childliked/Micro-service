package com.api.serivce;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.ResponseBase;

@RequestMapping("/order")
public interface OrderService {

	@RequestMapping("/updateOrderIdInfo")
	ResponseBase updateOrderIdInfo(@RequestParam("isPay") Long isPay, @RequestParam("payId") String aliPayId,
			@RequestParam("orderNumber") String orderNumber);
}
