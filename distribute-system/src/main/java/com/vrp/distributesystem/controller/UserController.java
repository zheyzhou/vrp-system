package com.vrp.distributesystem.controller;

import com.vrp.distributesystem.entity.AllCustomer;
import com.vrp.distributesystem.entity.User;
import com.vrp.distributesystem.mapper.UserMapper;
import com.vrp.distributesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user") //给接口加前缀
public class UserController {

    @Autowired //引入其他类
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping //插入、删除、修改请求
    public Integer save(@RequestBody User user) { //接受传来的参数并改为User类型,把前台的json对象转为Java对象
        //新增或者更新
        return userService.save(user);
    }

    // 查询所有数据
    @GetMapping
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return userMapper.deleteById(id);
    }

    // 分页查询
    // 接口路径：/user/page?pageNum=1&pageSize=10
    // @RequestParam接受
    // limit第一个参数 = (pageNum - 1) * pageSize
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize) {
//        pageNum = (pageNum - 1) * pageSize;
//        List<AllCustomer> data = allCustomerMapper.selectPage(pageNum, pageSize);
//        Integer total = allCustomerMapper.selectTotal();
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", data);
//        res.put("total", total);
//        return res;
//    }
}
