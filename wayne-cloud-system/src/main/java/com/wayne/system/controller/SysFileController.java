package com.wayne.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wayne.common.config.proprety.TemplateProperty;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.domain.SysFile;
import com.wayne.system.service.ISysFileService;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

/**
 * Describe: 文件控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"资源文件"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "file")
public class SysFileController extends BaseController {

    /**
     * 配置文件
     */
    @Resource
    private TemplateProperty uploadProperty;

    /**
     * 移 除 服 务
     */
    @Autowired
    private Map<String, ISysFileService> fileServiceMap;

    /**
     * 根据配置文件选择实现类
     * @return
     */
    private ISysFileService getFileService() {
        ISysFileService fileService = null;
        if (uploadProperty.isFtpUse()) {
            fileService = this.fileServiceMap.get("SysFileFTPServiceImpl");
        } else {
            fileService = this.fileServiceMap.get("SysFileServiceImpl");
        }
        return fileService;
    }

    /**
     * Describe: 文件资源数据
     * Param: PageDomain
     * Return: 文件资源列表
     */
    @GetMapping("data")
    @PreAuthorize("hasAnyAuthority('sys:file:data')")
    public ResultTable data(PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        PageInfo<SysFile> pageInfo = new PageInfo<>(getFileService().data());
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 文件上传接口
     * Param: SysUser
     * Return: Result
     */
    @PostMapping("upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        String result = getFileService().upload(file);
        if (Strings.isNotBlank(result)) {
            return Result.success(0, "上传成功", result);
        } else {
            return Result.failure("上传失败");
        }
    }

    /**
     * Describe: 文件获取接口
     * Param: id
     * Return: 文件流
     */
    @GetMapping("download/{id}")
    public void download(@PathVariable("id") String id) {
        getFileService().download(id);
    }

    /**
     * Describe: 文件删除接口
     * Param: id
     * Return: 文件流
     */
    @DeleteMapping("remove/{id}")
    @PreAuthorize("hasAnyAuthority('sys:file:remove')")
    public Result remove(@PathVariable("id") String id) {
        boolean result = getFileService().remove(id);
        return Result.decide(result, "删除成功", "删除失败");
    }

    /**
     * Describe: 文件删除接口
     * Param: id
     * Return: 文件流
     */
    @Transactional
    @DeleteMapping("batchRemove/{ids}")
    @PreAuthorize("hasAnyAuthority('sys:file:remove')")
    public Result batchRemove(@PathVariable("ids") String ids) {
        for (String id : ids.split(",")) {
            getFileService().remove(id);
        }
        return Result.success("删除成功");
    }
}
