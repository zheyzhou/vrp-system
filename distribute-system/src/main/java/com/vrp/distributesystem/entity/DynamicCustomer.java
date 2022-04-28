package com.vrp.distributesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalTime;

@Data
@TableName(value = "dynamic_customer")
public class DynamicCustomer {
    private Integer id;
    private String location;
    private double x坐标;
    private double y坐标;
    private double demand;
    private LocalTime earliest;
    private LocalTime latest;
    private double sever;
    private LocalTime cometime;
}
