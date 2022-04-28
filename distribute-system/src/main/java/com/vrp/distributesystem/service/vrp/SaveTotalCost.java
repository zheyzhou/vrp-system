package com.vrp.distributesystem.service.vrp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SaveTotalCost {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/vrp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String user = "root";
    static final String password = "wsxzzwals";

    // 保存总成本到数据库 total_cost
    public void save(Path path) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, user, password);

            stmt = conn.createStatement();

            String clear_sql,add_sql;
            clear_sql = "TRUNCATE TABLE total_cost";
            stmt.execute(clear_sql);

            add_sql = "INSERT INTO total_cost(drivingcost,timecost,fixedcost,cost)" +
                    "VALUE ('"+path.getDriving_cost()+"','"+
                    path.getTime_cost()+"','"+path.getFixed_cost()+"','"+path.getCost()+"')";
            stmt.execute(add_sql);

            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
