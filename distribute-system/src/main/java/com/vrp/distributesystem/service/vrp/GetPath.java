package com.vrp.distributesystem.service.vrp;

import java.sql.*;
import java.util.ArrayList;

public class GetPath {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/vrp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String user = "root";
    static final String password = "wsxzzwals";

    private final ArrayList<Path> paths = new ArrayList<>();

    // 取出各路径信息
    public ArrayList<Path> get() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, user, password);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT way,drivingcost,timecost,fixedcost,cost FROM each_path";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Path path = new Path();
                path.setWay(rs.getString("way"));
                path.setDriving_cost(rs.getDouble("drivingcost"));
                path.setTime_cost(rs.getDouble("timecost"));
                path.setFixed_cost(rs.getDouble("fixedcost"));
                path.setCost(rs.getDouble("cost"));
                paths.add(path);
            }

            rs.close();

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return paths;
    }
}
