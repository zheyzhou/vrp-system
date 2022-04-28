package com.vrp.distributesystem.controller;

import com.vrp.distributesystem.entity.EachPath;
import com.vrp.distributesystem.service.EachPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eachpath")
public class EachPathController {

    @Autowired
    private EachPathService eachPathService;

    // 新增或更新
    @PostMapping("/savoured")
    public boolean saveOrUpdate(@RequestBody EachPath eachPath) {
        return eachPathService.saveOrUpdateEachPath(eachPath);
    }

    // 查询所有数据
    @GetMapping("/findAll")
    public List<EachPath> findAll() {
        return eachPathService.list();
    }

}
