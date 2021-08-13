package com.wayne.web.controller.system;

import com.wayne.common.tools.system.CpuInfo;
import com.wayne.common.tools.system.SystemUtil;
import com.wayne.common.web.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"服务监控"})
@RequestMapping("monitor")
public class MonitorController extends BaseController {

    @GetMapping("main")
    @PreAuthorize("hasAnyAuthority('sys:monitor:main')")
    public ModelAndView main(Model model){
        CpuInfo cpu = SystemUtil.getCpu();
        model.addAttribute("cpu", cpu);
        return jumpPage("system/monitor/main");
    }
}
