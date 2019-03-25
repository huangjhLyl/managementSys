package com.docker.adminuser.modules.sysUser.service.impl;

import com.docker.adminUser.modules.sysUser.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.docker.adminuser.modules.sysUser.mapper.SysUserMapper;
import com.docker.adminuser.modules.sysUser.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author huangjh
 * @since 2019-03-25
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
