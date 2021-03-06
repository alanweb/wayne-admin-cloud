package com.wayne.web.controller.system;

import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.web.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Describe: 接口文档控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"接口文档"})
@RequestMapping("doc")
public class DocController extends BaseController {

    private String MODULE_PATH = "system/doc/";

    @GetMapping("main")
    @PreAuthorize("hasAnyAuthority('sys:doc:main')")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }
}
