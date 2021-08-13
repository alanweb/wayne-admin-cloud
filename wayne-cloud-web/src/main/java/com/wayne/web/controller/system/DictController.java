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
@Api(tags = {"字典管理"})
@RequestMapping("dict")
public class DictController extends BaseController {

    private String MODULE_TYPE_PATH = "system/dict/";
    private String MODULE_DATA_PATH = "system/dict/data/";

    @Autowired
    private SystemService systemService;

    /**
     * Describe: 数据字典列表视图
     * Return: ModelAndView
     */
    @GetMapping("/type/main")
    @PreAuthorize("hasAnyAuthority('sys:dictType:main')")
    public ModelAndView main() {
        return jumpPage(MODULE_TYPE_PATH + "main");
    }

    /**
     * Describe: 数据字典类型新增视图
     * Return: ModelAndView
     */
    @GetMapping("/type/add")
    @PreAuthorize("hasAnyAuthority('sys:dictType:add')")
    public ModelAndView add() {
        return jumpPage(MODULE_TYPE_PATH + "add");
    }

    /**
     * Describe: 数据字典类型修改视图
     * Param: dictTypeId
     * Return: ModelAndView
     */
    @GetMapping("/type/edit")
    @PreAuthorize("hasAnyAuthority('sys:dictType:edit')")
    public ModelAndView editType(Model model, String dictTypeId) {
        model.addAttribute("sysDictType", systemService.getDictTypeById(dictTypeId));
        return jumpPage(MODULE_TYPE_PATH + "edit");
    }

    /**
     * Describe: 数据字典列表视图
     * Param: typeCode
     * Return: ModelAndView
     */
    @GetMapping("/data/main")
    public ModelAndView main(Model model, String typeCode) {
        model.addAttribute("typeCode", typeCode);
        return jumpPage(MODULE_DATA_PATH + "main");
    }

    /**
     * Describe: 数据字典类型新增视图
     * Param: typeCode
     * Return: ModelAndView
     */
    @GetMapping("/data/add")
    public ModelAndView add(Model model, String typeCode) {
        model.addAttribute("typeCode", typeCode);
        return jumpPage(MODULE_DATA_PATH + "add");
    }

    /**
     * Describe: 数据字典类型修改视图
     * Param: dataId
     * Return: ModelAndView
     */
    @GetMapping("/data/edit")
    public ModelAndView editData(Model model, String dataId) {
        model.addAttribute("sysDictData", systemService.getDictDataById(dataId));
        return jumpPage(MODULE_DATA_PATH + "edit");
    }
}
