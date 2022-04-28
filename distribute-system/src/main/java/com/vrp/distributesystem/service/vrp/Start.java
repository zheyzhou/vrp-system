package com.vrp.distributesystem.service.vrp;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Start {
    private final CalCost calcost = new CalCost();
    private DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<Path> paths = new ArrayList<>(); // 路径
    private Path path = new Path(); // 总成本

    // 赋值
    public void new_sol(Sol s1,Sol s2) {
        for (Customer c : s2.getCust_seq()) {
            s1.getCust_seq().add(new Customer(c.getId(),c.getX(),c.getY(),
                    c.getDemand(),c.getE(),c.getL(),c.getSever()));
        }
        s1.setFit(s2.getFit());
        s1.setCost(s2.getCost());
    }

    // 把路径独立出来，并生成数据库中的 way 字符串
    private void depend(ArrayList<Sol> ss, Sol s) {
        // 独立路径
        Sol so = new Sol();
        so.getCust_seq().add(s.getCust_seq().get(0));
        for (int i=1;i<s.getCust_seq().size();i++) {
            if (s.getCust_seq().get(i).getId() == 0) {
                so.getCust_seq().add(s.getCust_seq().get(i));
                ss.add(so);
                so = new Sol();
            }
            so.getCust_seq().add(s.getCust_seq().get(i));
        }
        so.getCust_seq().add(s.getCust_seq().get(0));
        ss.add(so);

        // 生成 way 字符串，并保存
        for (Sol sol : ss) {
            String way = null;
            for (Customer c : sol.getCust_seq()) {
                int j = sol.getCust_seq().indexOf(c);
                if (j == 0) {
                    way = Integer.toString(c.getId());
                } else {
                    way += Integer.toString(c.getId());
                }
                // 如果不是最后一个，加上"-"
                if (j != sol.getCust_seq().size()-1) {
                    way += "-";
                }
            }
            Path p = new Path();
            p.setWay(way);
            paths.add(p);
        }
    }

    // 保存各路径各成本并输出
    private void savePrint(ArrayList<Sol> ss){
        double sum_cost = 0;
        for (Sol s : ss){
            // 保存
            int i = ss.indexOf(s);
            paths.get(i).setDriving_cost(calcost.cal_driving_cost(s));
            paths.get(i).setTime_cost(calcost.cal_ela_cost(s));
            paths.get(i).setFixed_cost(calcost.cal_fixed_cost());
            paths.get(i).setCost(calcost.cal_tarval(s));

            path.setDriving_cost(path.getDriving_cost() + paths.get(i).getDriving_cost());
            path.setTime_cost(path.getTime_cost() + paths.get(i).getTime_cost());
            path.setFixed_cost(path.getFixed_cost() + paths.get(i).getFixed_cost());
            path.setCost(path.getCost() + paths.get(i).getCost());

            // 输出
            System.out.println("-------");
            sum_cost += calcost.cal_tarval(s);
            System.out.println("第"+(i+1)+"条路径为：");
            for (Customer c : s.getCust_seq()){
                System.out.print(c.getId()+" ");
            }
            System.out.println();
            System.out.println("行驶成本："+calcost.cal_driving_cost(s));
            System.out.println("时间成本："+calcost.cal_ela_cost(s));
            System.out.println("固定成本："+calcost.cal_fixed_cost());
            System.out.println("总成本："+calcost.cal_tarval(s));
//            System.out.println("早到总时长："+s.getEarly_time());
//            System.out.println("迟到总时长："+s.getLate_time());
        }

        path.setDriving_cost(Double.parseDouble(df.format(path.getDriving_cost())));
        path.setTime_cost(Double.parseDouble(df.format(path.getTime_cost())));
        path.setFixed_cost(Double.parseDouble(df.format(path.getFixed_cost())));
        path.setCost(Double.parseDouble(df.format(path.getCost())));

        System.out.println("所有路径总成本："+sum_cost);
        System.out.println("---------------------------------------------------");
    }

    public boolean start() {
        // 读入配送中心
        GetCenter getCenter = new GetCenter();
        getCenter.set_center();
        Customer center = getCenter.getCenter();
        center.setId(0);

        // 读入顾客点
        GetCustomer getCustomer = new GetCustomer();
        getCustomer.set_cus();
        Sol sol = getCustomer.getSol();

        // 用遗传算法得到初始路径
        GA ga = new GA();
        ga.start(center, sol);

        // 得到初始路径
        Sol tmp = ga.getBest_sol();
        Sol first_sol = new Sol();
        new_sol(first_sol, tmp);

        // 输出初始路径，首先将各路径独立，再输出
        ArrayList<Sol> first_ss = new ArrayList<>();
        depend(first_ss, first_sol);
        System.out.println("静态顾客点配送最优路径：");
        savePrint(first_ss);

        // 存入数据库
        SavePath savePath = new SavePath();
        savePath.save(paths);

        SaveTotalCost saveTotalCost = new SaveTotalCost();
        saveTotalCost.save(path);

        // 画图
        new print(first_sol);

        return true;

//
//
//        // 实时更新路径
//        Update update = new Update();
//        update.auto_update(first_sol);
//
//        // 得到最终结果
//        Sol tmp2 = update.getBest_sol();
//        Sol last_sol = new Sol();
//        new_sol(last_sol, tmp2);
//
//        // 输出最终路径，首先将各路径独立，再输出
//        ArrayList<Sol> last_ss = new ArrayList<>();
//        depend(last_ss, last_sol);
//        System.out.println("最终顾客点配送最优路径：");
//        print(last_ss);
//        //print print1 = new print(last_sol);
//
//        // 算法结束
//        System.out.println("当日配送已完成！");
    }
}
