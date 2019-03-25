package com.hjh.adminActiviti.common.flowWork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.adminActiviti.common.flowWork.entity.ActModel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huangjh
 * @since 2019-03-06
 */
public interface IActModelService extends IService<ActModel> {

    List<ActModel> selectUserPage();
}
