package com.vrp.distributesystem.controller;

import com.vrp.distributesystem.entity.EachPath;
import com.vrp.distributesystem.entity.TotalCost;
import com.vrp.distributesystem.mapper.TotalCostMapper;
import com.vrp.distributesystem.service.TotalCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/totalcost")
public class TotalCostController {

    @Autowired
    private TotalCostService totalCostService;

    // 新增或更新
    @PostMapping("/savoured")
    public boolean saveOrUpdate(@RequestBody TotalCost totalCost) {
        return totalCostService.saveOrUpdateTotalCost(totalCost);
    }

    // 查询所有数据
    @GetMapping("/findAll")
    public List<TotalCost> findAll() {
        return totalCostService.list();
    }

}
