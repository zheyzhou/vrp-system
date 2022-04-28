package com.vrp.distributesystem.service.vrp;

import java.sql.*;
import java.util.ArrayList;

// 保存新增的客户点
public class SaveNewCus {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/vrp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String user = "root";
    static final String password = "wsxzzwals";

    public void save_new_cus(ArrayList<Customer> customers) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, user, password);

            stmt = conn.createStatement();

            String sql;

            for (Customer c : customers) {
                sql = "INSERT INTO used_customer(id,location,x坐标,y坐标,demand,earliest,latest,sever)" +
                        "VALUE ('"+c.getId()+"','"+c.getLocation()+"','"+c.getX()+"','"+c.getY()+ "','"+
                        c.getDemand()+"','"+c.getT1()+"','"+c.getT2()+"','"+c.getSever()+"')";
                stmt.execute(sql);
            }

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
