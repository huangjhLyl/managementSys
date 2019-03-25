package com.hjh.adminActiviti.common.flowWork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjh.adminActiviti.common.flowWork.entity.ActModel;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huangjh
 * @since 2019-03-06
 */
public interface ActModelMapper extends BaseMapper<ActModel> {
    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * 注意!!: 如果入参是有多个,需要加注解指定参数名才能在xml中取值
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param state 状态
     * @return 分页对象
     */
    List<ActModel> selectModel();
}
