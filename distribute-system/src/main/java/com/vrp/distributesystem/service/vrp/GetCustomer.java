package com.vrp.distributesystem.service.vrp;

import java.sql.*;
import java.util.ArrayList;

public class GetCustomer {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/vrp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String user = "root";
    static final String password = "wsxzzwals";

    static private final Sol sol = new Sol();
    static private final ArrayList<Customer> tmp_cus_seq = sol.getCust_seq();

    public Sol getSol() {
        return sol;
    }

    // 读入顾客点
    public void set_cus(){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, user, password);

            stmt = conn.createStatement();

            String sql;
            sql = "SELECT id,x坐标,y坐标,demand,earliest,latest,sever FROM used_customer";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Customer tmp_cust = new Customer();
                tmp_cust.setId(rs.getInt("id"));
                tmp_cust.setX(rs.getDouble("x坐标"));
                tmp_cust.setY(rs.getDouble("y坐标"));
                tmp_cust.setDemand(rs.getDouble("demand"));
                tmp_cust.setT1(rs.getTime("earliest"));
                tmp_cust.setT2(rs.getTime("latest"));
                tmp_cust.setSever(rs.getDouble("sever"));
                tmp_cus_seq.add(tmp_cust);
                sol.setCust_seq(tmp_cus_seq);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
