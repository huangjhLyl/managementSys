package com.docker.feign.adminRole;

import com.docker.feign.adminRole.entity.SysRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="adminRole",fallback = adminRoleFeginClient.adminRoleFeginClientFallBack.class)
public interface adminRoleFeginClient {

	@PostMapping("/sysRole/sys-role/findListByUserId")
	public List<SysRole> findListByUserId(@RequestParam("userId") String userId);

	@Component
	static class adminRoleFeginClientFallBack implements adminRoleFeginClient{

        @Override
        public List<SysRole> findListByUserId(String userId) {
            return null;
        }
    }
}
