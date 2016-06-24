package com.v5zhu.autotest.oracle;

/**
 * Created by zhuxl@paxsz.com on 2016/6/22.
 */
public class Test {
    public static void main(String[] args) {
//        String ip="192.168.13.201";
        String ip="127.0.0.1";
//        String port="1521";
        String port="3306";
//        String database="oracledb";
        String database="v5zhu-jmeter";
//        String username="tpsdbo";
        String username="root";
//        String password="tpsdbop";
        String password="123456";
//        String sql="select name from T_CPOSMER where ROWNUM<20";
        String sql="select * from user;select * from user where id=1";
        String result= null;
        try {
//            result = Dao.execute("mysql",ip,port,database,username,password,sql);
            result = Dao.executeBatch("mysql",ip,port,database,username,password,sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sql执行返回:"+result);
    }
}
