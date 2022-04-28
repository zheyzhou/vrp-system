package com.vrp.distributesystem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vrp.distributesystem.entity.DynamicCustomer;
import com.vrp.distributesystem.mapper.DynamicCustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class DynamicCustomerService extends ServiceImpl<DynamicCustomerMapper, DynamicCustomer> {

    public boolean saveOrUpdateDynamic(DynamicCustomer dynamicCustomer) {
        return saveOrUpdate(dynamicCustomer);
    }
}
