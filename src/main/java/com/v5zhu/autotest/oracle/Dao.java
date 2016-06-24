package com.v5zhu.autotest.oracle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.sql.*;

/**
 * Created by zhuxl@paxsz.com on 2016/6/22.
 */
public class Dao {
//    private static final String driver="com.mysql.jdbc.Driver";
    /**
     * oracle驱动
     */
    private static final String driver = "oracle.jdbc.driver.OracleDriver";

    /**
     * 得到数据库连接
     *
     * @param ip       数据库ip
     * @param port     数据库端口
     * @param database 数据库名称
     * @param username 用户名
     * @param password 密码
     * @return 连接对象
     */
    public static Connection getConnection(String ip, String port, String database, String username, String password) {
        try {
            // 加载驱动程序
            Class.forName(driver);

            // 连续数据库
            String oracleUrl = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + database;
//            String mysqlUrl="jdbc:mysql://"+ip+":"+port+"/"+database;

            Connection conn = DriverManager.getConnection(oracleUrl, username, password);
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的参数执行动态sql返回执行后返回的结果
     *
     * @param ip       数据库ip
     * @param port     数据库端口
     * @param database 数据库名称
     * @param username 用户名
     * @param password 密码
     * @param sql      动态sql
     * @return 执行sql返回结果,json数组格式
     */
    public static String execute(String ip, String port, String database, String username, String password, String sql) {
        ResultSet rs=null;
        Connection connection=null;
        try {
            connection = getConnection(ip, port, database, username, password);
            if (!connection.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
            }

            // statement用来执行SQL语句
            Statement statement = connection.createStatement();

            // 结果集
            rs = statement.executeQuery(sql);
            JSONArray array = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                ResultSetMetaData metaData = rs.getMetaData();
                int columns = metaData.getColumnCount();
                //显示列,表格的表头
                for (int i = 1; i <= columns; i++) {
                    String key = metaData.getColumnName(i);
                    Object value = rs.getObject(key);
                    obj.put(key, value);
                }
                array.add(obj);
            }
            return array.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
