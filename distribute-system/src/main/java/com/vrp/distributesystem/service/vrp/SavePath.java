package com.vrp.distributesystem.service.vrp;

import java.sql.*;
import java.util.ArrayList;

public class SavePath {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/vrp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String user = "root";
    static final String password = "wsxzzwals";

    // 保存各路径信息到数据库 each_path
    public void save(ArrayList<Path> paths) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, user, password);

            stmt = conn.createStatement();

            String clear_sql,add_sql;
            clear_sql = "TRUNCATE TABLE each_path";
            stmt.execute(clear_sql);

            for (Path path : paths) {
                add_sql = "INSERT INTO each_path(way,drivingcost,timecost,fixedcost,cost)" +
                        "VALUE ('"+path.getWay()+"','"+path.getDriving_cost()+"','"+
                        path.getTime_cost()+"','"+path.getFixed_cost()+"','"+path.getCost()+"')";
                stmt.execute(add_sql);
            }

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
