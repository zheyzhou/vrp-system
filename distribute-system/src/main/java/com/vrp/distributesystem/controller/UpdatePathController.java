package com.vrp.distributesystem.controller;

import com.vrp.distributesystem.service.UpdatePathService;
import com.vrp.distributesystem.service.VrpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/update")
public class UpdatePathController {

    @Autowired
    private UpdatePathService updatePathService;

    @PostMapping("/start")
    public boolean start() {
        return updatePathService.updatePath();
    }
}
