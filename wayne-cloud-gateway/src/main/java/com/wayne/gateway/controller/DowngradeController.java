package com.wayne.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DowngradeController {
    @RequestMapping(value = "/downgrade",produces = "text/html;charset=UTF-8")
    public String downgrade(){
        return "<html><body><div style='width:800px;margin:auto;font-size:24px;text-align:center;'>服务正忙，请稍后重试！</div></body></html>";
    }
}
