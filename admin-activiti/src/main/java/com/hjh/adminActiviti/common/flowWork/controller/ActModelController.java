package com.hjh.adminActiviti.common.flowWork.controller;



import com.docker.commonUtil.ImageUtil;
import com.docker.commonUtil.Layui;
import com.docker.commonUtil.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjh.adminActiviti.common.flowWork.service.IActModelService;
import com.hjh.adminActiviti.common.flowWork.entity.ActModel;
import com.hjh.adminActiviti.common.util.XmlUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Model;
import org.json.simple.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * <p>
 *  流程模型
 * </p>
 *
 * @author huangjh
 * @since 2019-03-06
 */
@Controller
@RequestMapping("/activiti/act-model")
public class ActModelController {

    private final static Logger logger = LoggerFactory.getLogger("ActModelController");

    @Autowired
    private IActModelService actModelService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private XmlUtil xmlUtil;



    @RequestMapping("list")
    public @ResponseBody
    Layui listModel(int page, int limit){
        List<ActModel> actModels = actModelService.selectUserPage();
        //用json来传值
        return Layui.data(actModels.size(),actModels);
    }

    @RequestMapping("listEmpity")
    public String getList(){
        return "flowWork/listActModel";
    }

    @RequestMapping("formEmpity")
    public String getForm(){
        return "flowWork/formActModel";
    }

    /**
     * 读取资源，通过部署ID
     * @param processDefinitionId  流程定义ID
     * @param processInstanceId 流程实例ID
     * @param resourceType 资源类型(xml|image)
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "resource/read")
    public void resourceRead(String procDefId, String proInsId, String resType, HttpServletResponse response) throws Exception {
        InputStream resourceAsStream = xmlUtil.resourceRead(procDefId,proInsId,resType);

        if("image".equals(resType)){
            BufferedImage image = ImageIO.read(resourceAsStream);
            OutputStream out = response.getOutputStream();

            ImageUtil.setBackgroundColor(image, out);
        }else{
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        }
        }

    /**
     * 预览xml
     * @param modelId
     * @param response
     */
    @RequestMapping(value = "readXml")
    public void editor(String modelId,HttpServletResponse response) {
        String XML="";
        try {
            Model modelData = repositoryService.getModel(modelId);
            String metaInfo = modelData.getMetaInfo();

            logger.info("描述信息："+metaInfo);
            JSONObject jsonObject = StringUtils.stringToJson(metaInfo);
            String  description = "";
            Object  ob =jsonObject.get("description");
            if(ob instanceof String){
                description = (String)ob;
            }
//            String  description = modelData.getMetaInfo().substring(modelData.getMetaInfo().lastIndexOf(":")+2,modelData.getMetaInfo().lastIndexOf("}")-1);
            modelData.setMetaInfo(description);
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree((new String(modelEditorSource,"UTF-8")).getBytes("UTF-8"));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] exportBytes = xmlConverter.convertToXML(bpmnModel,"UTF-8");

            response.getOutputStream().write(exportBytes, 0, exportBytes.length);


        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
