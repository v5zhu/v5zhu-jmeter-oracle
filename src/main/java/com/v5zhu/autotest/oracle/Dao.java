package com.v5zhu.autotest.oracle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.sql.*;

/**
 * Created by zhuxl@paxsz.com on 2016/6/22.
 */
public class Dao {
    private static final String mysqlDriver="com.mysql.jdbc.Driver";
    /**
     * oracle驱动
     */
    private static final String oracleDriver = "oracle.jdbc.driver.OracleDriver";

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
    public static Connection getConnection(String db,String ip, String port, String database, String username, String password) throws Exception {
        try {
            String url=null;
            if(db.equals("mysql")){
                Class.forName(mysqlDriver);
                url="jdbc:mysql://"+ip+":"+port+"/"+database;
            }else if(db.equals("oracle")){
                Class.forName(oracleDriver);
                url="jdbc:oracle:thin:@" + ip + ":" + port + ":" + database;
            }else{
                throw new Exception("参数错误");
            }
            Connection conn = DriverManager.getConnection(url, username, password);
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
    public static String execute(String db,String ip, String port, String database, String username, String password, String sql) {
        ResultSet rs=null;
        Connection connection=null;
        try {
            connection = getConnection(db,ip, port, database, username, password);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
    @SuppressWarnings("Duplicates")
    public static String executeBatch(String db,String ip, String port, String database, String username, String password, String sql)  {
        ResultSet rs=null;
        Connection connection=null;
        try {
            connection = getConnection(db,ip, port, database, username, password);
            if (!connection.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
            }

            // statement用来执行SQL语句
            Statement statement = connection.createStatement();
            JSONArray outerArray=new JSONArray();
            for(String one:sql.split(";")){
                JSONObject outObj=new JSONObject();
                outObj.put("sql",one);
                rs = statement.executeQuery(one);
                // 结果集
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
                outObj.put("data",array);
                outerArray.add(outObj);
            }

            return outerArray.toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
