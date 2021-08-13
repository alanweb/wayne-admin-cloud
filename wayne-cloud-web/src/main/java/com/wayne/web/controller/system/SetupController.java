package com.wayne.web.controller.system;

import com.wayne.common.constant.ConfigurationConstant;
import com.wayne.common.plugin.system.domain.SysBaseConfig;
import com.wayne.common.plugin.system.domain.SysBaseSetup;
import com.wayne.common.web.base.BaseController;
import com.wayne.web.service.SystemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"组织部门"})
@RequestMapping("setup")
public class SetupController extends BaseController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/setup/";

    @Autowired
    private SystemService systemService;

    @GetMapping("main")
    @PreAuthorize("hasAnyAuthority('sys:setup:main')")
    public ModelAndView main(Model model) {

        SysBaseSetup sysSetup = new SysBaseSetup();
        SysBaseConfig mailFromConfig = systemService.getByCode(ConfigurationConstant.MAIN_FROM);
        SysBaseConfig mailUserConfig = systemService.getByCode(ConfigurationConstant.MAIN_USER);
        SysBaseConfig mailPassConfig = systemService.getByCode(ConfigurationConstant.MAIN_PASS);
        SysBaseConfig mailHostConfig = systemService.getByCode(ConfigurationConstant.MAIN_HOST);
        SysBaseConfig mailPortConfig = systemService.getByCode(ConfigurationConstant.MAIN_PORT);
        sysSetup.setMailFrom(mailFromConfig == null ? "" : mailFromConfig.getConfigValue());
        sysSetup.setMailUser(mailUserConfig == null ? "" : mailUserConfig.getConfigValue());
        sysSetup.setMailPass(mailPassConfig == null ? "" : mailPassConfig.getConfigValue());
        sysSetup.setMailHost(mailHostConfig == null ? "" : mailHostConfig.getConfigValue());
        sysSetup.setMailPort(mailPortConfig == null ? "" : mailPortConfig.getConfigValue());

        SysBaseConfig uploadKindConfig = systemService.getByCode(ConfigurationConstant.UPLOAD_KIND);
        SysBaseConfig uploadPathConfig = systemService.getByCode(ConfigurationConstant.UPLOAD_PATH);
        sysSetup.setUploadKind(uploadKindConfig == null ? "" : uploadKindConfig.getConfigValue());
        sysSetup.setUploadPath(uploadPathConfig == null ? "" : uploadPathConfig.getConfigValue());

        model.addAttribute("setup", sysSetup);
        return jumpPage(MODULE_PATH + "main");
    }
}
