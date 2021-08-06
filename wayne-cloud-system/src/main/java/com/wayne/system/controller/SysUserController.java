package com.wayne.system.controller;

import com.github.pagehelper.PageInfo;
import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.plugin.logging.aop.annotation.Logging;
import com.wayne.common.plugin.logging.aop.enums.BusinessType;
import com.wayne.common.plugin.submit.annotation.RepeatSubmit;
import com.wayne.common.plugin.system.domain.SysBaseRole;
import com.wayne.common.plugin.system.domain.SysBaseUser;
import com.wayne.common.tools.secure.SecurityUtil;
import com.wayne.common.tools.sequence.SequenceUtil;
import com.wayne.common.tools.servlet.ServletUtil;
import com.wayne.common.tools.string.Convert;
import com.wayne.common.web.base.BaseController;
import com.wayne.common.web.constants.Enable;
import com.wayne.common.web.domain.request.PageDomain;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.domain.SysMenu;
import com.wayne.system.domain.SysUser;
import com.wayne.system.param.EditPassword;
import com.wayne.system.param.QueryUserParam;
import com.wayne.system.service.ISysLogService;
import com.wayne.system.service.ISysRoleService;
import com.wayne.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describe: 用户控制器
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
@RestController
@Api(tags = {"用户管理"})
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "user")
public class SysUserController extends BaseController {

    /**
     * Describe: 用户模块服务
     */
    @Resource
    private ISysUserService sysUserService;

    /**
     * Describe: 角色模块服务
     */
    @Resource
    private ISysRoleService sysRoleService;

    /**
     * Describe: 日志模块服务
     */
    @Resource
    private ISysLogService sysLogService;


    /**
     * Describe: 获取用户列表数据
     * Param ModelAndView
     * Return 用户列表数据
     */
    @GetMapping("data")
    @ApiOperation(value = "获取用户列表数据")
    @PreAuthorize("hasAnyAuthority('sys:user:data')")
    @Logging(title = "查询用户", describe = "查询用户", type = BusinessType.QUERY)
    public ResultTable data(PageDomain pageDomain, QueryUserParam param) {
        SysUser user = new SysUser();
        user.setDeptId(param.getDeptId());
        user.setUsername(param.getUsername());
        user.setRealName(param.getRealName());
        PageInfo<SysUser> pageInfo = sysUserService.page(user, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }


    /**
     * Describe: 用户新增接口
     * Param ModelAndView
     * Return 操作结果
     */
    @RepeatSubmit
    @PostMapping("save")
    @ApiOperation(value = "保存用户数据")
    @PreAuthorize("hasAnyAuthority('sys:user:add')")
    @Logging(title = "新增用户", describe = "新增用户", type = BusinessType.ADD)
    public Result save(@RequestBody SysUser sysUser) {
        sysUser.setLogin("0");
        sysUser.setEnable("1");
        sysUser.setStatus(SysUser.C_NORMAL);
        sysUser.setUserId(SequenceUtil.makeStringId());
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        sysUserService.saveUserRole(sysUser.getUserId(), Arrays.asList(sysUser.getRoleIds().split(",")));
        Boolean result = sysUserService.save(sysUser);
        return decide(result);
    }


    /**
     * Describe: 用户密码修改接口
     * Param editPassword
     * Return: Result
     */
    @PutMapping("editPassword")
    public Result editPassword(@RequestBody EditPassword editPassword) {
        String oldPassword = editPassword.getOldPassword();
        String newPassword = editPassword.getNewPassword();
        String confirmPassword = editPassword.getConfirmPassword();
        SysUser editUser = sysUserService.getById(getCurrentUserId());
        if (Strings.isBlank(confirmPassword)
                || Strings.isBlank(newPassword)
                || Strings.isBlank(oldPassword)) {
            return failure("输入不能为空");
        }
        if (!new BCryptPasswordEncoder().matches(oldPassword, editUser.getPassword())) {
            return failure("密码验证失败");
        }
        if (!newPassword.equals(confirmPassword)) {
            return failure("两次密码输入不一致");
        }
        editUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        boolean result = sysUserService.updateById(editUser);
        return decide(result, "修改成功", "修改失败");
    }

    /**
     * Describe: 用户修改接口
     * Param ModelAndView
     * Return 返回用户修改接口
     */
    @PutMapping("update")
    @ApiOperation(value = "修改用户数据")
    @Logging(title = "修改用户", describe = "修改用户", type = BusinessType.EDIT)
    public Result update(@RequestBody SysUser sysUser) {
        sysUserService.saveUserRole(sysUser.getUserId(), Arrays.asList(sysUser.getRoleIds().split(",")));
        boolean result = sysUserService.updateById(sysUser);
        return decide(result);
    }

    /**
     * Describe: 头像修改接口
     * Param: SysUser
     * Return: Result
     */
    @PutMapping("updateAvatar")
    @ApiOperation(value = "修改用户头像")
    @PreAuthorize("hasAnyAuthority('sys:user:edit')")
    @Logging(title = "修改头像", describe = "修改头像", type = BusinessType.EDIT)
    public Result updateAvatar(@RequestBody SysUser sysUser) {
        sysUser.setUserId(getCurrentUserId());
        boolean result = sysUserService.updateById(sysUser);
        return decide(result);
    }

    /**
     * Describe: 用户批量删除接口
     * Param: ids
     * Return: Result
     */
    @DeleteMapping("batchRemove/{ids}")
    @ApiOperation(value = "批量删除用户")
    @PreAuthorize("hasAnyAuthority('sys:user:remove')")
    @Logging(title = "删除用户", describe = "删除用户", type = BusinessType.REMOVE)
    public Result batchRemove(@PathVariable String ids) {
        boolean result = sysUserService.removeByIds(Arrays.asList(Convert.toStrArray(ids)));
        return decide(result);
    }

    /**
     * Describe: 用户删除接口
     * Param: id
     * Return: Result
     */
    @Transactional
    @DeleteMapping("remove/{id}")
    @ApiOperation(value = "删除用户数据")
    @PreAuthorize("hasAnyAuthority('sys:user:remove')")
    @Logging(title = "删除用户", describe = "删除用户", type = BusinessType.REMOVE)
    public Result remove(@PathVariable String id) {
        boolean result = sysUserService.removeById(id);
        return decide(result);
    }

    /**
     * Describe: 根据 username 获取菜单数据
     * Param SysRole
     * Return 执行结果
     */
    @GetMapping("menu")
    @ApiOperation(value = "获取用户菜单数据")
    public List<SysMenu> getUserMenu() {
        String userName = getCurrentUserName();
        List<SysMenu> menus = sysUserService.getUserMenu(userName);
        return sysUserService.toUserMenu(menus, "0");
    }

    /**
     * Describe: 根据 userId 开启用户
     * Param: SysUser
     * Return: 执行结果
     */
    @PutMapping("enable")
    @ApiOperation(value = "开启用户登录")
    public Result enable(@RequestBody SysUser sysUser) {
        sysUser.setEnable(Enable.ENABLE);
        boolean result = sysUserService.updateById(sysUser);
        return decide(result);
    }

    /**
     * Describe: 根据 userId 禁用用户
     * Param: SysUser
     * Return: 执行结果
     */
    @PutMapping("disable")
    @ApiOperation(value = "禁用用户登录")
    public Result disable(@RequestBody SysUser sysUser) {
        sysUser.setEnable(Enable.DISABLE);
        boolean result = sysUserService.updateById(sysUser);
        return decide(result);
    }


    /**
     * Describe: 用户修改接口
     * Param ModelAndView
     * Return 返回用户修改接口
     */
    @PutMapping("updateInfo")
    @ApiOperation(value = "修改用户数据")
    public Result updateInfo(@RequestBody SysUser sysUser) {
        boolean result = sysUserService.updateById(sysUser);
        return decide(result);
    }

    @GetMapping("queryByUserId")
    @ApiOperation(value = "根据用户主键查询用户信息", hidden = true)
    public SysBaseUser queryByUserId(String userId) {
        return sysUserService.getById(userId);
    }

    @GetMapping("queryByUserName")
    @ApiOperation(value = "根据用户名查询用户信息", hidden = true)
    public SysBaseUser queryByUserName(String username) {
        return sysUserService.selectByUsername(username);
    }

    @GetMapping("queryUserRole")
    @ApiOperation(value = "根据用户主键查询用户角色信息", hidden = true)
    public List<SysBaseRole> queryUserRole(String userId) {
        return sysUserService.getUserRole(userId).stream().collect(Collectors.toList());
    }

}
