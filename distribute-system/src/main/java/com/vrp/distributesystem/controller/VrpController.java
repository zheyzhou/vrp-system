package com.vrp.distributesystem.controller;

import com.vrp.distributesystem.service.VrpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/vrp")
public class VrpController {

    @Autowired
    private VrpService vrpService;

    @PostMapping("/start")
    public boolean start() {
        return vrpService.start();
    }
}
