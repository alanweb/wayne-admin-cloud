package com.wayne.web.controller.system;

import com.wayne.common.plugin.system.domain.SysBaseNotice;
import com.wayne.common.web.base.BaseController;
import com.wayne.web.service.SystemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Api(tags = {"消息公告"})
@RequestMapping("notice")
public class NoticeController extends BaseController {

    /**
     * Describe: 基础路径
     */
    private static String MODULE_PATH = "system/notice/";

    @Autowired
    private SystemService systemService;

    /**
     * Describe: 获取消息公告列表视图
     * Return 消息公告列表视图
     */
    @GetMapping("main")
    public ModelAndView main() {
        return jumpPage(MODULE_PATH + "main");
    }


    /**
     * 新增notice
     */
    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('system:notice:add')")
    public ModelAndView add(Model model) {
        // model.addAttribute("users", sysUserService.list());
        return jumpPage(MODULE_PATH + "add");
    }

    /**
     * 修改notice
     */
    @GetMapping("/edit")
    @PreAuthorize("hasAnyAuthority('system:notice:edit')")
    public ModelAndView edit(String id, ModelMap map) {
        SysBaseNotice sysNotice = systemService.getNoticeById(id);
        map.put("sysNotice", sysNotice);
        return jumpPage(MODULE_PATH + "edit");
    }

}
