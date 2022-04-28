package com.vrp.distributesystem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vrp.distributesystem.entity.EachPath;
import com.vrp.distributesystem.mapper.EachPathMapper;
import org.springframework.stereotype.Service;

@Service
public class EachPathService extends ServiceImpl<EachPathMapper, EachPath> {

    public boolean saveOrUpdateEachPath(EachPath eachPath) {
        return saveOrUpdate(eachPath);
    }
}
