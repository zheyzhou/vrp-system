package com.vrp.distributesystem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vrp.distributesystem.entity.AllCustomer;
import com.vrp.distributesystem.mapper.AllCustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class AllCustomerService extends ServiceImpl<AllCustomerMapper, AllCustomer> {

    public boolean saveOrUpdateCustomer(AllCustomer allCustomer) {
//        if (allCustomer.getId() == 0) {
//            return save(allCustomer); // mybatis-plus提供的方法，表示插入数据，成功返回true
//        } else {
//            return updateById(allCustomer); // mybatis-plus提供的方法，表示更新数据，成功返回true
//        }
        return saveOrUpdate(allCustomer); // mybatis-plus提供的方法，表示插入或更新数据，成功返回true
    }

}
