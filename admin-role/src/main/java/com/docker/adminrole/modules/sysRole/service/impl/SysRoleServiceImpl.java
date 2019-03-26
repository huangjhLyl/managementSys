package com.docker.adminrole.modules.sysRole.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.docker.adminrole.modules.sysRole.entity.SysRole;
import com.docker.adminrole.modules.sysRole.mapper.SysRoleMapper;
import com.docker.adminrole.modules.sysRole.service.ISysRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author huangjh
 * @since 2019-03-25
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

}
