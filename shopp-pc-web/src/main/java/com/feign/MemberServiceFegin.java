package com.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.api.serivce.MemberService;

@Component
@FeignClient("member")
public interface MemberServiceFegin extends MemberService {

}
