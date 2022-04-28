package com.vrp.distributesystem.service.vrp;

// 路径信息
public class Path {
    private String way;
    private double driving_cost, time_cost, fixed_cost, cost;

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public double getDriving_cost() {
        return driving_cost;
    }

    public void setDriving_cost(double driving_cost) {
        this.driving_cost = driving_cost;
    }

    public double getTime_cost() {
        return time_cost;
    }

    public void setTime_cost(double time_cost) {
        this.time_cost = time_cost;
    }

    public double getFixed_cost() {
        return fixed_cost;
    }

    public void setFixed_cost(double fixed_cost) {
        this.fixed_cost = fixed_cost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
