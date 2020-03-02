package com.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.api.service.PayService;

@Component
@FeignClient("pay")
public interface PayServiceFegin extends PayService {

}
