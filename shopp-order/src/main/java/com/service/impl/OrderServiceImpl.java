package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.serivce.OrderService;
import com.base.BaseApiService;
import com.base.ResponseBase;
import com.codingapi.tx.annotation.ITxTransaction;
import com.dao.OrderDao;


@RestController
public class OrderServiceImpl extends BaseApiService implements OrderService, ITxTransaction  {
	@Autowired
	private OrderDao orderDao;

	@Transactional
	@Override
	public ResponseBase updateOrderIdInfo(@RequestParam("isPay") Long isPay, @RequestParam("payId") String aliPayId,
			@RequestParam("orderNumber") String orderNumber) {
		int updateOrder = orderDao.updateOrder(isPay, aliPayId, orderNumber);
		if (updateOrder <= 0) {
			return setResultError("系统错误!");
		}
		return setResultSuccess();
	}

}
