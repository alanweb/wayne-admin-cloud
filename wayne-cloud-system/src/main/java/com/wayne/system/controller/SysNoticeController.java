package com.wayne.system.controller;

import com.github.pagehelper.PageInfo;
import com.wayne.common.tools.secure.SecurityUtil;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.tools.string.Convert;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.domain.SysNotice;
import com.wayne.system.domain.SysUser;
import com.wayne.system.service.ISysNoticeService;
import com.wayne.system.service.ISysUserService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;
/**
 * Describe: 消息控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"消息公告"})
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    private String prefix = "system/notice" ;

    @Resource
    private ISysNoticeService sysNoticeService;

    @Resource
    private ISysUserService sysUserService;

    @GetMapping("/main")
    @PreAuthorize("hasAnyAuthority('system:notice:main')")
    public ModelAndView main()
    {
        return jumpPage(prefix + "/main");
    }

    /**
     * 查询notice列表
     */
    @ResponseBody
    @GetMapping("/data")
    @PreAuthorize("hasAnyAuthority('system:notice:data')")
    public ResultTable list(@ModelAttribute SysNotice sysNotice, PageDomain pageDomain)
    {
        PageInfo<SysNotice> pageInfo = sysNoticeService.selectSysNoticePage(sysNotice,pageDomain);
        return pageTable(pageInfo.getList(),pageInfo.getTotal());
    }

    /**
     * 查询消息
     * */
    @ResponseBody
    @GetMapping("notice")
    public List<Map> notice(@RequestHeader HttpHeaders headers){
        String userId = headers.get("user_id").get(0);
        List<Map> result = new ArrayList<>();
        SysNotice publicParam = new SysNotice();
        publicParam.setType("public");
        SysNotice privateParam = new SysNotice();
        privateParam.setType("private");
        privateParam.setAccept(userId);
        SysNotice noticeParam = new SysNotice();
        noticeParam.setType("notice");

        Map<String,Object> publicArray = new HashMap<>();
        publicArray.put("id","1");
        publicArray.put("title","公告");
        publicArray.put("children",sysNoticeService.selectSysNoticeList(publicParam));

        Map<String,Object> privateArray = new HashMap<>();
        privateArray.put("id","2");
        privateArray.put("title","私信");
        privateArray.put("children",sysNoticeService.selectSysNoticeList(privateParam));

        Map<String,Object> noticeArray = new HashMap<>();
        noticeArray.put("id","3");
        noticeArray.put("title","通知");
        noticeArray.put("children",sysNoticeService.selectSysNoticeList(noticeParam));

        result.add(publicArray);
        result.add(privateArray);
        result.add(noticeArray);

        return result;
    }

    /**
     * 新增notice
     */
    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('system:notice:add')")
    public ModelAndView add(Model model)
    {
        model.addAttribute("users",sysUserService.list());
        return jumpPage(prefix + "/add");
    }

    /**
     * 新增保存notice
     */
    @ResponseBody
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('system:notice:add')")
    public Result save(@RequestBody SysNotice sysNotice)
    {
        sysNotice.setId(SequenceUtil.makeStringId());
        return decide(sysNoticeService.save(sysNotice));
    }

    /**
     * 修改notice
     */
    @GetMapping("/edit")
    @PreAuthorize("hasAnyAuthority('system:notice:edit')")
    public ModelAndView edit(String id, ModelMap mmap)
    {
        SysNotice sysNotice = sysNoticeService.getById(id);
        mmap.put("sysNotice", sysNotice);
        return jumpPage(prefix + "/edit");
    }

    /**
     * 修改保存notice
     */
    @ResponseBody
    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('system:notice:edit')")
    public Result update(@RequestBody SysNotice sysNotice)
    {
        return decide(sysNoticeService.updateById(sysNotice));
    }

    /**
     * 删除notice
     */
    @ResponseBody
    @DeleteMapping( "/batchRemove")
    @PreAuthorize("hasAnyAuthority('system:notice:remove')")
    public Result batchRemove(String ids)
    {
        return decide(sysNoticeService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAnyAuthority('system:notice:remove')")
    public Result remove(@PathVariable("id") String id)
    {
        return decide(sysNoticeService.removeById(id));
    }
}
