package com.vrp.distributesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalTime;

@Data //自动提供get与set函数
@TableName(value = "all_customer") // 指定要操作的表名
public class AllCustomer {

    @TableId(value = "id", type = IdType.AUTO) // 标明在数据库里的名称，当程序中取名变化时用
    private Integer id;
    private String location;
    private double x坐标;
    private double y坐标;
    private double demand;
    private LocalTime earliest;
    private LocalTime latest;
    private double sever;
//    @TableField(value = "xxx") // 指定数据库的字段配置
}
