package com.docker.adminrole.modules.sysRole.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.docker.adminrole.modules.roleUserMapp.entity.SysUserRole;
import com.docker.adminrole.modules.roleUserMapp.service.ISysUserRoleService;
import com.docker.adminrole.modules.sysRole.entity.SysRole;
import com.docker.adminrole.modules.sysRole.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author huangjh
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/sysRole/sys-role")
@Slf4j
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    /**
     * 根据用户信息查询角色
     *
     */
    @PostMapping("findListByUserId")
    public List<SysRole> findListByUserId(@RequestParam("userId") String userId){
        List<SysRole> list = new ArrayList<SysRole>();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",userId);
        //查询中间表
        Collection<SysUserRole> sysUserRoles = sysUserRoleService.listByMap(map);

        if(!sysUserRoles.isEmpty()){
            //取得这个用户下所有的角色id
            List<String> roleIds = sysUserRoles.stream()
                    .map(SysUserRole::getRoleId)
                    .collect(Collectors.toList());

            //查询所有的角色信息
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",roleIds);
            List<SysRole> roleList = sysRoleService.list(queryWrapper);

            //将结果返回输出
            list = roleList.stream().map(e -> {
                SysRole role = new SysRole();
                BeanUtils.copyProperties(e, role);
                return role;
            }).collect(Collectors.toList());

            log.info("用户角色：{}",list);
        }

        return list;
    }

}
