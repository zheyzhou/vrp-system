package com.vrp.distributesystem.service.vrp;

import java.sql.*;

public class GetCenter {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/vrp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String user = "root";
    static final String password = "wsxzzwals";

    public Customer getCenter() {
        return center;
    }

    private final Customer center = new Customer(); // 配送中心

    // 读入配送中心
    public void set_center() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, user, password);

            stmt = conn.createStatement();

            String sql;
            sql = "SELECT id,x坐标,y坐标,demand,earliest,latest,sever FROM center";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                center.setId(rs.getInt("id"));
                center.setX(rs.getDouble("x坐标"));
                center.setY(rs.getDouble("y坐标"));
                center.setDemand(rs.getDouble("demand"));
                center.setT1(rs.getTime("earliest"));
                center.setT2(rs.getTime("latest"));
                center.setSever(rs.getDouble("sever"));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
