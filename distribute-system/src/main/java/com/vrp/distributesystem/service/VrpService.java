package com.vrp.distributesystem.service;

import com.vrp.distributesystem.service.vrp.Start;
import org.springframework.stereotype.Service;

@Service
public class VrpService {

    public boolean start() {
        Start start = new Start();
        return start.start();
    }
}
