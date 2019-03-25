package com.docker.adminuser.modules.sysUser.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.docker.adminuser.modules.sysUser.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.docker.adminUser.modules.sysUser.entity.SysUser;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author huangjh
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/sysUser/sys-user")
public class SysUserController {

    @Autowired
    private ISysUserService userService;
    /**
     * 查询个人信息
     * 1.查询用户信息
     * 2.查询归属公司
     * 3.查询用户角色
     * 4.数据构造
     */
    /**
     *查询个人信息
     * 1.查询用户信息
     * 2.查询归属公司
     * 3.查询用户角色
     * 4.数据构造
     * @param loginName 登录名
     * @return
     */
    @PostMapping("getPersonInfo")
    public SysUser getPersonInfo(@RequestBody String loginName){
        //查询用户信息
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",loginName);
        SysUser user = userService.getOne(queryWrapper);
        return user;
    }
}
