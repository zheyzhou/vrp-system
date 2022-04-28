package com.vrp.distributesystem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vrp.distributesystem.entity.TotalCost;
import com.vrp.distributesystem.mapper.TotalCostMapper;
import org.springframework.stereotype.Service;

@Service
public class TotalCostService extends ServiceImpl<TotalCostMapper, TotalCost> {

    public boolean saveOrUpdateTotalCost(TotalCost totalCost) {
        return saveOrUpdate(totalCost);
    }
}
