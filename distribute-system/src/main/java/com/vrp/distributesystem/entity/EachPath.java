package com.vrp.distributesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "each_path")
public class EachPath {
    private String way;
    private double drivingcost;
    private double timecost;
    private double fixedcost;
    private double cost;
}
