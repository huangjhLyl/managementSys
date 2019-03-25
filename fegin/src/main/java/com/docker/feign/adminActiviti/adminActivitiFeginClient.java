package com.docker.feign.adminActiviti;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="adminActiviti")
public interface adminActivitiFeginClient {

	@RequestMapping("/num1")
	public int num1();
}
