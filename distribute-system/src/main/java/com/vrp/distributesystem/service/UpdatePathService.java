package com.vrp.distributesystem.service;

import com.vrp.distributesystem.service.vrp.Update;
import org.springframework.stereotype.Service;

@Service
public class UpdatePathService {

    public boolean updatePath() {
        Update update = new Update();
        return update.startUpdate();
    }
}
