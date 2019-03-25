package com.hjh.adminActiviti.common.flowWork.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hjh.adminActiviti.common.flowWork.entity.ActModel;
import com.hjh.adminActiviti.common.flowWork.mapper.ActModelMapper;
import com.hjh.adminActiviti.common.flowWork.service.IActModelService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huangjh
 * @since 2019-03-06
 */
@Service
public class ActModelServiceImpl extends ServiceImpl<ActModelMapper, ActModel> implements IActModelService {

    @Autowired
    private ActModelMapper actModelMapper;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public List<ActModel> selectUserPage() {
        LinkedList<ActModel> objects = Lists.newLinkedList();

        List<Model> modelQuery = repositoryService.createModelQuery()
                .latestVersion()
                .orderByLastUpdateTime()
                .desc()
                .listPage(0,100);
        for (Model model1 : modelQuery) {
            ActModel model = new ActModel();
            model.setID(model1.getId());
            model.setKey(model1.getKey());
            model.setName(model1.getName());
            model.setVersion(model1.getVersion());
            objects.add(model);
        }

        return objects;
    }
}
