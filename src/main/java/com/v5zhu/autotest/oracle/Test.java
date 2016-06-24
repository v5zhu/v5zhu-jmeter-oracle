package com.v5zhu.autotest.oracle;

/**
 * Created by zhuxl@paxsz.com on 2016/6/22.
 */
public class Test {
    public static void main(String[] args) {
        String ip="192.168.13.201";
        String port="1521";
        String database="oracledb";
        String username="tpsdbo";
        String password="tpsdbop";
//        String sql="select name from T_CPOSMER where ROWNUM<20";
        String sql="select * from T_CPOSMER where MID='888888888888888'";
        String result=Dao.execute(ip,port,database,username,password,sql);
        System.out.println("sql执行返回:"+result);
    }
}
