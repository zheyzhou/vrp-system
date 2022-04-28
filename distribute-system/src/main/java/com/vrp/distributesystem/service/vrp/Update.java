package com.vrp.distributesystem.service.vrp;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Update {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/vrp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String user = "root";
    static final String password = "wsxzzwals";

    private final CalCost calcost = new CalCost();
    private DecimalFormat df = new DecimalFormat("0.00");
    private final GetSpeed getspeed = new GetSpeed();                  // 不同时段的车速
    private final ArrayList<Customer> all_new_cus = new ArrayList<>(); // 所有的新增的客户点
    private final ArrayList<Customer> new_cus = new ArrayList<>();     // 每时段新增的客户点
    private Customer center = new Customer();                          // 配送中心
    private Sol original_cus = new Sol();                              // 原客户点
    private Sol original_sol = new Sol();                              // 原最优解
    private Sol best_sol = new Sol();                                  // 最优解
    private ArrayList<Path> paths = new ArrayList<>();                 // 路径
    private Path path = new Path();                                    // 总成本

    private void auto_update(){

        // 把路径独立出来
        ArrayList<Sol> ss = new ArrayList<>();
        depend(ss, original_sol);

        // 将已配送的顾客点整合为一个虚拟顾客点后的路径
        ArrayList<Sol> ns = new ArrayList<>();
        // 已配送的顾客点
        ArrayList<Sol> os = new ArrayList<>();

        // 配送中心开始配送时间t0，结束配送时间t1,更新周期td
        double t0 = 10,t1 = 600,td = 30,t = t0;
        double v = getspeed.get_speed(t);

        while (t < t1){
            // 检查数据库中是否有新的客户点,若有,则将其取出
            if (judge_new(t)){
                for (Sol item : ss){
                    int whole = 1;//是否全部完成，0部分完成，1全部完成
                    double tt = t0, ttt = tt;
                    for (int i=1;i<item.getCust_seq().size();i++){
                        // 计算车辆达到下一个顾客点的时间
                        Customer c1 = item.getCust_seq().get(i-1);
                        Customer c2 = item.getCust_seq().get(i);
                        CalDistance caldistance = new CalDistance(c1.getX(),c1.getY(),c2.getX(),c2.getY());
                        double dis = caldistance.distance();
                        tt += dis/v*60;
                        if (getspeed.get_speed(tt) != v) {
                            double vv = getspeed.get_speed(tt);
                            double tm = getspeed.get_minutes(vv);
                            tt -= dis / v * 60;
                            tt += tm - tt + (dis - (tm - tt) / 60 * v) / vv * 60;
                            v = vv;
                        }

                        // 若到达下个顾客点时到达更新时刻,则构建新的路径
                        if (tt >= t){
                            double demand = 0;

                            // 存储已服务的顾客点
                            Sol tmp_s = new Sol();
                            for (int j=0;j<i;j++){
                                Customer tmp_c = item.getCust_seq().get(j);
                                demand += tmp_c.getDemand();
                                tmp_s.getCust_seq().add(new Customer(tmp_c.getId(),tmp_c.getX(),tmp_c.getY(),
                                        tmp_c.getDemand(),tmp_c.getE(),tmp_c.getL(),tmp_c.getSever()));
                            }
                            os.add(tmp_s);

                            // 存储未服务的顾客点
                            tmp_s = new Sol();
                            // 虚拟顾客点
                            tmp_s.getCust_seq().add(new Customer(original_sol.getCust_seq().size()+100,
                                    item.getCust_seq().get(i-1).getX(),item.getCust_seq().get(i-1).getY(),
                                    demand,ttt,0,0));
                            for (int j=i;j<item.getCust_seq().size();j++){
                                Customer tmp_c = item.getCust_seq().get(j);
                                tmp_s.getCust_seq().add(new Customer(tmp_c.getId(),tmp_c.getX(),tmp_c.getY(),
                                        tmp_c.getDemand(),tmp_c.getE(),tmp_c.getL(),tmp_c.getSever()));
                            }
                            ns.add(tmp_s);

                            whole = 0;// 部分完成
                            break;
                        }

                        // 加上服务时间
                        tt += item.getCust_seq().get(i).getSever();
                        ttt = tt;
                    }
                    // 如果全部完成
                    if (whole == 1){
                        double demand = 0;

                        // 存储已服务的顾客点
                        Sol tmp_s = new Sol();
                        for (Customer c : item.getCust_seq()){
                            demand += c.getDemand();
                            tmp_s.getCust_seq().add(new Customer(c.getId(),c.getX(),c.getY(),
                                    c.getDemand(),c.getE(),c.getL(),c.getSever()));
                        }
                        os.add(tmp_s);

                        // 存储未服务的顾客点
                        tmp_s = new Sol();
                        // 虚拟顾客点
                        tmp_s.getCust_seq().add(new Customer(original_sol.getCust_seq().size()+100,
                                item.getCust_seq().get(item.getCust_seq().size()-1).getX(),
                                item.getCust_seq().get(item.getCust_seq().size()-1).getY(),
                                demand,ttt,0,0));
                        ns.add(tmp_s);
                    }
                }

                // 使用模拟退火算法更新路径
                Sol los = update_pop(ns,os);
                original_sol = new Sol();
                for (Customer c : los.getCust_seq()){
                    original_sol.getCust_seq().add(c);
                }

                // 输出相关信息
                //print(ns,os,t,original_sol);

                original_sol.setCost(los.getCost());
                ss.clear();
                ns.clear();
                os.clear();
                depend(ss, original_sol);
            }
            t += td;
        }

        best_sol = original_sol;
    }

    // 根据原客户点还原原最优解
    private void restore(ArrayList<Path> paths) {
        for (Path path : paths) {

            System.out.println(path);

            // 先将类似0-1-2-0的字符串分解
            String[] way = path.getWay().split("-");

            for (String s : way){
                System.out.print(s+" ");
            }
            System.out.println();

            // 要除开最后的0
            for (int i=0;i< way.length-1;i++) {

                // 如果是配送中心
                if (way[i].equals("0")) {
                    original_sol.getCust_seq().add(center);
                }
                // 不是配送中心
                else {
                    for (Customer c : original_cus.getCust_seq()) {
                        if (way[i].equals(Integer.toString(c.getId()))) {
                            original_sol.getCust_seq().add(c);
                            break;
                        }
                    }
                }
            }

        }

        for (Customer c : original_sol.getCust_seq()) {
            System.out.print(c.getId()+" ");
        }
        System.out.println();
    }

    // 使用模拟退火算法更新路径
    private Sol update_pop(ArrayList<Sol> ns, ArrayList<Sol> os){
        SA sa = new SA();
        sa.start(ns,os,new_cus);
        return sa.getBest_sol();
    }

    // 把路径独立出来
    private void depend(ArrayList<Sol> ss, Sol s){
        Sol so = new Sol();
        so.getCust_seq().add(s.getCust_seq().get(0));
        for (int i=1;i<s.getCust_seq().size();i++){
            if (s.getCust_seq().get(i).getId() == 0){
                ss.add(so);
                so = new Sol();
            }
            so.getCust_seq().add(s.getCust_seq().get(i));
        }
        ss.add(so);
    }

    // 把路径独立出来，并生成数据库中的 way 字符串
    private void dependWay(ArrayList<Sol> ss, Sol s) {
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

    // 检查数据库中是否有新的客户点,若有,则将其取出
    private boolean judge_new(double t){
        new_cus.clear();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, user, password);

            stmt = conn.createStatement();

            String sql;
            sql = "SELECT id,location,x坐标,y坐标,demand,earliest,latest,sever,cometime FROM dynamic_customer";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Time time = rs.getTime("cometime");
//                int h = (time.getHours()+16)<24 ? time.getHours()+16 : time.getHours()-8;
//                time.setHours(h);
                double ti = (time.getHours()-8)*60+time.getMinutes()+time.getSeconds()/60;
                if (ti < t && ti > t - 30){
                    Customer tmp_cus = new Customer();
                    tmp_cus.setId(rs.getInt("id"));
                    tmp_cus.setX(rs.getDouble("x坐标"));
                    tmp_cus.setY(rs.getDouble("y坐标"));
                    tmp_cus.setDemand(rs.getDouble("demand"));
                    tmp_cus.setT1(rs.getTime("earliest"));
                    tmp_cus.setT2(rs.getTime("latest"));
                    tmp_cus.setSever(rs.getDouble("sever"));
                    new_cus.add(tmp_cus);
                    tmp_cus.setLocation(rs.getString("location"));
                    all_new_cus.add(tmp_cus);
                }
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return new_cus.size() != 0;
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
//    // 输出函数
//    private void print(ArrayList<Sol> ns,ArrayList<Sol> os,double t,Sol sol){
//        CalCost calcost = new CalCost();
//
//        System.out.println("第 "+t+" 分钟更新如下：");
//
//        System.out.print("未完成：");
//        for (Sol s1 : ns){
//            for (Customer c1 : s1.getCust_seq()){
//                System.out.print(c1.getId()+" ");
//            }
//        }
//        System.out.println();
//        System.out.print("已完成：");
//        for (Sol s1 : os){
//            for (Customer c1 : s1.getCust_seq()){
//                System.out.print(c1.getId()+" ");
//            }
//        }
//        System.out.println();
//
//        ArrayList<Sol> ss = new ArrayList<>();
//        depend(ss,sol);
//        double sum_cost = 0;
//
//        System.out.println("插入新解后：");
//        for (Sol s : ss){
//            System.out.println("-------");
//            s.getCust_seq().add(s.getCust_seq().get(0));
//            sum_cost += calcost.cal_tarval(s);
//            System.out.println("第"+(ss.indexOf(s)+1)+"条路径为：");
//            for (Customer c : s.getCust_seq()){
//                System.out.print(c.getId()+" ");
//            }
//            System.out.println();
//            System.out.println("行驶成本："+calcost.cal_driving_cost(s));
//            System.out.println("时间成本："+calcost.cal_ela_cost(s));
//            System.out.println("固定成本："+calcost.cal_fixed_cost());
//            System.out.println("总成本："+calcost.cal_tarval(s));
//            System.out.println("早到总时长："+s.getEarly_time());
//            System.out.println("迟到总时长："+s.getLate_time());
//        }
//
//        System.out.println("所有路径总成本："+sum_cost);
//        System.out.println("---------------------------------------------------");
//       // print print2 = new print(sol);
//    }

    public boolean startUpdate() {

        // 从数据库中把上一次的解取出来
        GetPath getPath = new GetPath();
        ArrayList<Path> tmp_paths = getPath.get();

        // 读入原客户点
        GetCustomer getCustomer = new GetCustomer();
        getCustomer.set_cus();
        original_cus = getCustomer.getSol();

        // 读入配送中心
        GetCenter getCenter = new GetCenter();
        getCenter.set_center();
        center = getCenter.getCenter();
        center.setId(0);

        // 根据原客户点还原原最优解
        restore(tmp_paths);

        // 开始实时更新
        auto_update();

        // 输出更新后路径，首先将各路径独立，再输出
        ArrayList<Sol> first_ss = new ArrayList<>();
        dependWay(first_ss, best_sol);
        System.out.println("更新后配送最优路径：");
        savePrint(first_ss);

        // 存入数据库
        SavePath savePath = new SavePath();
        savePath.save(paths);

        SaveTotalCost saveTotalCost = new SaveTotalCost();
        saveTotalCost.save(path);

        SaveNewCus saveNewCus = new SaveNewCus();
        saveNewCus.save_new_cus(all_new_cus);

        new print(best_sol);

        // 画图
        return true;
    }
}
