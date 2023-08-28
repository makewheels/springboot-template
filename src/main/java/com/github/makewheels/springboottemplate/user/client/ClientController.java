package com.github.makewheels.springboottemplate.user.client;

import cn.hutool.json.JSONObject;
import com.github.makewheels.springboottemplate.system.response.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController()
@RequestMapping("client")
public class ClientController {
    @Resource
    private ClientService clientService;

    @CrossOrigin
    @GetMapping("requestClientId")
    public Result<JSONObject> requestClientId() {
        return clientService.requestClientId();
    }
}
