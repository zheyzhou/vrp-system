package com.vrp.distributesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "total_cost")
public class TotalCost {
    private double drivingcost;
    private double timecost;
    private double fixedcost;
    private double cost;
}
