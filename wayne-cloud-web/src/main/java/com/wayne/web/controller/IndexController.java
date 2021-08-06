package com.wayne.web.controller;

import com.wayne.common.plugin.logging.aop.annotation.Logging;
import com.wayne.common.plugin.logging.aop.enums.BusinessType;
import com.wayne.common.web.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Api(tags = {"项目入口"})
public class IndexController extends BaseController {
    @GetMapping({"/", "/index"})
    @Logging(title = "主页", describe = "返回 Index 主页视图", type = BusinessType.ADD)
    public ModelAndView index() {
        String avatar = getCurrentUser().getAvatar();
        Map<String, String> params = new HashMap<>();
        params.put("avatar", avatar);
        return jumpPage("index", params);
    }

    /**
     * Describe: 获取主页视图
     * Param: ModelAndView
     * Return: 主页视图
     */
    @GetMapping("console")
    public ModelAndView home(@RequestHeader HttpHeaders headers, Model model) {
        return jumpPage("console/console");
    }

    /**
     * Describe:无权限页面
     * Return:返回403页面
     */
    @GetMapping("error/403")
    public ModelAndView noPermission() {
        return jumpPage("error/403");
    }

    /**
     * Describe:找不带页面
     * Return:返回404页面
     */
    @GetMapping("error/404")
    public ModelAndView notFound() {
        return jumpPage("error/404");
    }

    /**
     * Describe:异常处理页
     * Return:返回500界面
     */
    @GetMapping("error/500")
    public ModelAndView onException() {
        return jumpPage("error/500");
    }
}
