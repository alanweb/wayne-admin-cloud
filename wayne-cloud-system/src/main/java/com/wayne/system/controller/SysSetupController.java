package com.wayne.system.controller;

import cn.hutool.core.map.MapUtil;
import com.wayne.common.constant.ConfigurationConstant;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.listener.event.SetupEvent;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.domain.response.Result;
import com.wayne.system.domain.SysConfig;
import com.wayne.system.domain.SysSetup;
import com.wayne.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
/**
 * Describe: 设 置 控 制 器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"系统设置"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "setup")
public class SysSetupController extends BaseController implements ApplicationEventPublisherAware {



    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private ISysConfigService sysConfigService;

    @Transactional
    @PutMapping("save")
    @PreAuthorize("hasAnyAuthority('sys:setup:add')")
    public Result save(@RequestBody SysSetup sysSetup) {

        String from = sysSetup.getMailFrom();
        String user = sysSetup.getMailUser();
        String pass = sysSetup.getMailPass();
        String port = sysSetup.getMailPort();
        String host = sysSetup.getMailHost();

        String uploadKind = sysSetup.getUploadKind();
        String uploadPath = sysSetup.getUploadPath();

        updateSetup("邮箱来源", ConfigurationConstant.MAIN_FROM, from);
        updateSetup("邮箱用户", ConfigurationConstant.MAIN_USER, user);
        updateSetup("邮箱密码", ConfigurationConstant.MAIN_PASS, pass);
        updateSetup("邮箱端口", ConfigurationConstant.MAIN_PORT, port);
        updateSetup("邮箱主机", ConfigurationConstant.MAIN_HOST, host);

        updateSetup("上传方式", ConfigurationConstant.UPLOAD_KIND, uploadKind);
        updateSetup("上传路径", ConfigurationConstant.UPLOAD_PATH, uploadPath);

        HashMap<String, String> map = MapUtil.newHashMap(5);
        map.put(ConfigurationConstant.MAIN_FROM, from);
        map.put(ConfigurationConstant.MAIN_USER, user);
        map.put(ConfigurationConstant.MAIN_PASS, pass);
        map.put(ConfigurationConstant.MAIN_PORT, port);
        map.put(ConfigurationConstant.MAIN_HOST, host);
        SetupEvent setupEvent = new SetupEvent(this, map);
        applicationEventPublisher.publishEvent(setupEvent);
        return success("保存成功");
    }

    private void updateSetup(String name, String code, String value) {
        SysConfig config = sysConfigService.getByCode(code);
        if (config != null) {
            config.setConfigValue(value);
            sysConfigService.updateById(config);
        } else {
            config = new SysConfig();
            config.setConfigId(SequenceUtil.makeStringId());
            config.setConfigName(name);
            config.setConfigCode(code);
            config.setConfigType("system");
            config.setConfigValue(value);
            sysConfigService.save(config);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
