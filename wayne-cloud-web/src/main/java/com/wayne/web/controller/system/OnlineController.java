package com.wayne.web.controller.system;

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
@Api(tags = {"在线用户"})
@RequestMapping("online")
public class OnlineController extends BaseController {
    /**
     * Describe: 获取在线用户列表视图
     * Return: ModelAndView
     */
    @GetMapping("main")
    @PreAuthorize("hasAnyAuthority('sys:online:main')")
    public ModelAndView main() {
        return jumpPage("system/user/online");
    }

}
