package com.wayne.system.controller;

import com.github.pagehelper.PageInfo;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.system.domain.SysBaseDictData;
import com.wayne.common.plugin.system.domain.SysBaseDictType;
import com.wayne.common.plugin.system.service.SysContext;
import com.wayne.common.tools.database.SqlInjectionUtil;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.domain.SysDictDataData;
import com.wayne.system.service.ISysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Describe: 数据字典控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"字典类型"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "dictData")
public class SysDictDataController extends BaseController {

    private String MODULE_PATH = "system/dict/data/";

    @Resource
    private ISysDictDataService sysDictDataService;

    @Resource
    private SysContext iSysBaseAPI;

    /**
     * Describe: 数据字典列表数据
     * Param: sysDictType
     * Return: Result
     */
    @GetMapping("data")
    public ResultTable data(SysDictDataData sysDictData, PageDomain pageDomain) {
        PageInfo<SysDictDataData> pageInfo = sysDictDataService.page(sysDictData, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * Describe: 根据字典code获取数据字典列表数据
     * Param: typeCode
     * Return: Result
     */
    @GetMapping("selectByCode")
    public Result selectByCode(String typeCode) {
        List<SysDictDataData> list = sysDictDataService.selectByCode(typeCode);
        return success(list);
    }

    /**
     * Describe: 获取字典数据
     * Param: dictCode 字典code
     * Return: Result
     */
    @GetMapping(value = "/getDictItems/{dictCode}")
    public Result<List<SysBaseDictData>> getDictItems(@PathVariable String dictCode, @RequestParam(value = "sign", required = false) String sign, HttpServletRequest request) {
        Result<List<SysBaseDictData>> result = new Result<List<SysBaseDictData>>();
        List<SysBaseDictData> ls = null;
        try {
            if (dictCode.contains(",")) {
                String[] params = dictCode.split(",");

                if (params.length < 3) {
                    return Result.failure("字典Code格式不正确！");
                }
                final String[] sqlInjCheck = {params[0], params[1], params[2]};
                SqlInjectionUtil.filterContent(sqlInjCheck);
                if (params.length == 4) {
                    SqlInjectionUtil.specialFilterContent(params[3]);
                    ls = iSysBaseAPI.queryTableDictItemsByCodeAndFilter(params[0], params[1], params[2], params[3]);
                } else if (params.length == 3) {
                    ls = iSysBaseAPI.queryTableDictItemsByCode(params[0], params[1], params[2]);
                } else {
                    return Result.failure("字典Code格式不正确！");
                }
            } else {
                ls = iSysBaseAPI.selectDictByCode(dictCode);
            }
            result.setSuccess(true);
            result.setData(ls);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("操作失败！");
        }
        return result;
    }

    /**
     * Describe: 根据字典code加载字典text
     * Param: dictCode 字典code
     * Return: Result
     */
    @RequestMapping(value = "/loadDictItem/{dictCode}", method = RequestMethod.GET)
    public Result<List<SysBaseDictData>> loadDictItem(@PathVariable String dictCode, @RequestParam(name = "key") String keys, @RequestParam(value = "sign", required = false) String sign, HttpServletRequest request) {
        Result<List<SysBaseDictData>> result = new Result<>();
        try {
            if (dictCode.contains(",")) {
                String[] params = dictCode.split(",");
                if (params.length != 3) {
                    return Result.failure("字典Code格式不正确！");
                }
                String[] keyArray = keys.split(",");
                List<SysBaseDictData> texts = iSysBaseAPI.queryTableDictByKeys(params[0], params[1], params[2], keyArray);
                return Result.success(texts);
            } else {
                return Result.failure("字典Code格式不正确！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("操作失败！");
        }
    }

    /**
     * Describe: 新增字典类型接口
     * Param: sysDictType
     * Return: Result
     */
    @PostMapping("save")
    public Result save(@RequestBody SysDictDataData sysDictData) {
        sysDictData.setDataId(SequenceUtil.makeStringId());
        Boolean result = sysDictDataService.save(sysDictData);
        return decide(result);
    }

    /**
     * Describe: 数据字典类型修改视图
     * Param: sysDictData
     * Return: ModelAndView
     */
    @PutMapping("update")
    public Result update(@RequestBody SysDictDataData sysDictData) {
        boolean result = sysDictDataService.updateById(sysDictData);
        return decide(result);
    }

    /**
     * Describe: 数据字典删除
     * Param: id
     * Return: Result
     */
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") String id) {
        Boolean result = sysDictDataService.remove(id);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 启用字典
     * Param dictId
     * Return ResuTree
     */
    @PutMapping("enable")
    public Result enable(@RequestBody SysDictDataData sysDictData) {
        sysDictData.setEnable("0");
        boolean result = sysDictDataService.updateById(sysDictData);
        return decide(result);
    }

    /**
     * Describe: 根据 Id 禁用字典
     * Param dictId
     * Return ResuTree
     */
    @PutMapping("disable")
    public Result disable(@RequestBody SysDictDataData sysDictData) {
        sysDictData.setEnable("1");
        boolean result = sysDictDataService.updateById(sysDictData);
        return decide(result);
    }

    /**
     * Describe: 查询数据字典
     * Param: dictDataId
     * Return: SysBaseDictType
     */
    @GetMapping("/{dictDataId}")
    @ApiOperation(value = "获取数据字典信息", hidden = true)
    public SysBaseDictData queryByDictDataId(@PathVariable String dictDataId) {
        return sysDictDataService.getById(dictDataId);
    }
}
