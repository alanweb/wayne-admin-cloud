package com.wayne.system.controller;

import com.wayne.common.constant.ControllerConstant;
import com.wayne.common.web.domain.response.Result;
import com.wayne.common.web.domain.response.module.ResultTable;
import com.wayne.system.param.ClientDetailsParam;
import com.wayne.system.service.IClientDetailsService;
import com.wayne.system.vo.ClientDetailsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create date 2020/9/24.
 *
 * @author evan
 */
@Api(value = "客户端管理Controller", tags = "客户端管理")
@RestController
@RequestMapping(ControllerConstant.API_SYSTEM_PREFIX + "client")
public class ClientDetailsController {

    private final IClientDetailsService clientDetailsService;

    public ClientDetailsController(IClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @ApiOperation("创建客户端配置")
    @PostMapping("/detail")
    public Result addClientDetails(@RequestBody ClientDetailsParam clientDetailsParam) {
        this.clientDetailsService.addClientDetails(clientDetailsParam);
        return Result.success();
    }

    @ApiOperation("更新客户端配置")
    @PutMapping("/detail")
    public Result updateClientDetails(@RequestBody ClientDetailsParam clientDetailsParam) {
        this.clientDetailsService.updateClientDetails(clientDetailsParam);
        return Result.success();
    }

    @ApiOperation("删除客户端配置")
    @DeleteMapping("/detail/{clientId}")
    public Result removeClientDetails(
            @PathVariable("clientId")String clientId) {
        this.clientDetailsService.removeClientDetails(clientId);
        return Result.success();
    }

    @ApiOperation("列出所有客户端管理配置")
    @GetMapping("/detail/list")
    public ResultTable listClientDetails(
            @Param("clientId") String clientId,
            @Param("webServerRedirectUri") String webServerRedirectUri) {
        List<ClientDetailsVO> clientDetailsVOS =
                this.clientDetailsService.listClientDetails(clientId, webServerRedirectUri);
        return ResultTable.pageTable(clientDetailsVOS.size(), clientDetailsVOS);
    }

    /**
     * 根据客户端id获取客户端管理信息
     *
     * @param clientId client id
     * @return clientDetails
     */
    @ApiOperation("根据客户端id获取客户端管理信息")
    @GetMapping("/detail")
    public Result getClientDetails(@RequestParam("clientId") String clientId) {
        ClientDetailsVO details = this.clientDetailsService.getClientDetails(clientId);
      return Result.success(details);
    }
}
