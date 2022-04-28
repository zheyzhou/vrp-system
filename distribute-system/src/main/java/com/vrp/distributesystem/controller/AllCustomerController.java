package com.vrp.distributesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vrp.distributesystem.entity.AllCustomer;
import com.vrp.distributesystem.service.AllCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allcustomer")
public class AllCustomerController {

    @Autowired
    private AllCustomerService allCustomerService;

    // 新增或更新
    @PostMapping("/savoured")
    public boolean saveOrUpdate(@RequestBody AllCustomer allCustomer) {
        return allCustomerService.saveOrUpdateCustomer(allCustomer);
    }

    // 查询所有数据
    @GetMapping
    public List<AllCustomer> findAll() {
        return allCustomerService.list();
    }

    // 删除单条数据
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Integer id) {
        return allCustomerService.removeById(id);
    }

    // 删除多条数据
    @PostMapping("/deletebatch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return allCustomerService.removeByIds(ids);
    }

    // 分页查询 - mybatis-plus
    @GetMapping("/page")
    public IPage<AllCustomer> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String location,
                                       @RequestParam(defaultValue = "0") double x坐标,
                                       @RequestParam(defaultValue = "0") double y坐标) {
        IPage<AllCustomer> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AllCustomer> queryWrapper = new QueryWrapper<>();
        if (!"".equals(location)) {
            queryWrapper.like("location", location);
        }
        if (x坐标 != 0) {
            queryWrapper.like("x坐标", x坐标);
        }
        if (y坐标 != 0) {
            queryWrapper.like("y坐标", y坐标);
        }
        queryWrapper.orderByDesc("id");
        return allCustomerService.page(page, queryWrapper);
    }
}
