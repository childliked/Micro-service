package com.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.api.serivce.OrderService;

@Component
@FeignClient("order")
public interface OrderServiceFegin extends OrderService {

}
