package com.docker.adminuser.modules.sysUser.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.docker.adminuser.modules.sysUser.service.ISysUserService;
import com.docker.commonUtil.Layui;
import com.docker.feign.adminRole.entity.SysRole;
import com.docker.feign.adminRole.entity.SysUserOutPut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.docker.adminUser.modules.sysUser.entity.SysUser;

import com.docker.feign.adminRole.adminRoleFeginClient;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.LayerUI;
import javax.validation.Valid;
import java.util.List;

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
@Slf4j
public class SysUserController {

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private adminRoleFeginClient adminRoleFeginClient;
    /**
     *查询个人信息
     * 1.查询用户信息
     * 2.查询用户角色
     * 3.数据构造
     * 存在问题，传入参数的绑定校验没有完成
     * @param user
     * @param bindingResult
     * @return
     */
    @PostMapping("getPersonInfo")
    public Layui getPersonInfo(@Valid SysUserOutPut user, BindingResult bindingResult){
        SysUserOutPut userOutPut = new SysUserOutPut();

        //参数校验
        if(bindingResult.hasErrors()){
            return Layui.data(0,bindingResult.getFieldErrors());
        }

        //1.查询用户信息
        queryUserInfo(user.getLoginName(), userOutPut);

        //2.查询用户角色
        List<SysRole> listRole= adminRoleFeginClient.findListByUserId(userOutPut.getId());

        //3.数据构造
        userOutPut.setUserRoles(listRole);

        return Layui.data(0,userOutPut);
    }

    /**
     * 查询所有的用户信息
     * @return
     */
    @PostMapping("listPersonInfo")
    public Layui listPersonInfo(SysUserOutPut user, HttpServletRequest request){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        List<SysUser> list = userService.list(queryWrapper);
        return Layui.data(list.size(),list);
    }
    /**
     * 查询用户信息
     * @param loginName
     * @param userOutPut
     */

    private void queryUserInfo(@RequestParam String loginName, SysUserOutPut userOutPut) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",loginName);
        SysUser user = userService.getOne(queryWrapper);
        log.info("用户信息：{}",user);

        if(user !=null){
            BeanUtils.copyProperties(user,userOutPut);
        }
    }
}
