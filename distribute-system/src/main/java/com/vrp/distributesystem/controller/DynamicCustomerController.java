package com.vrp.distributesystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vrp.distributesystem.entity.AllCustomer;
import com.vrp.distributesystem.entity.DynamicCustomer;
import com.vrp.distributesystem.service.DynamicCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dynamiccustomer")
public class DynamicCustomerController {

    @Autowired
    private DynamicCustomerService dynamicCustomerService;

    // 新增或更新
    @PostMapping("/savoured")
    public boolean saveOrUpdate(@RequestBody DynamicCustomer dynamicCustomer) {
        return dynamicCustomerService.saveOrUpdateDynamic(dynamicCustomer);
    }

    // 查询所有数据
    @GetMapping
    public List<DynamicCustomer> findAll() {
        return dynamicCustomerService.list();
    }

    // 删除单条数据
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Integer id) {
        return dynamicCustomerService.removeById(id);
    }

    // 删除多条数据
    @PostMapping("/deletebatch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return dynamicCustomerService.removeByIds(ids);
    }

    // 分页查询 - mybatis-plus
    @GetMapping("/page")
    public IPage<DynamicCustomer> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String location,
                                       @RequestParam(defaultValue = "0") double x坐标,
                                       @RequestParam(defaultValue = "0") double y坐标) {
        IPage<DynamicCustomer> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DynamicCustomer> queryWrapper = new QueryWrapper<>();
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
        return dynamicCustomerService.page(page, queryWrapper);
    }
}
