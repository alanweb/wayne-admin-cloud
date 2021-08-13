package com.wayne.web.controller.system;

import com.wayne.common.web.base.BaseController;
import com.wayne.web.service.SystemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"资源文件"})
@RequestMapping("file")
public class FileController extends BaseController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/file/";

    /**
     * Describe: 获取资源文件列表视图
     * Return 资源文件列表视图
     */
    @GetMapping("main")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 获取资源文件新增视图
     * Return 资源文件新增视图
     */
    @GetMapping("add")
    public ModelAndView add() {
        return jumpPage(MODULE_PATH + "add");
    }

}
