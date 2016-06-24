package com.v5zhu.autotest.oracle;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class Tester extends AbstractJavaSamplerClient {
    private String server;
    private int port;
    private String database;
    private String username;
    private String password;
    private String sql;
    //	private String phase;
    private SampleResult result;

    /**
     * the cache of Terminal Connections
     */
    private static final ThreadLocal<Map<String, Dao>> tp = new ThreadLocal<Map<String, Dao>>() {
        @Override
        protected Map<String, Dao> initialValue() {
            return new HashMap<>();
        }
    };

    public void setupTest(JavaSamplerContext arg0) {
    }

    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("server", "192.168.13.201");
        params.addArgument("port", "1521");
        params.addArgument("database", "oracledb");
        params.addArgument("username", "tpsdbo");
        params.addArgument("password", "tpsdbop");
        params.addArgument("sql", "select name from T_CPOSMER where ROWNUM<20");
        return params;
    }

    public SampleResult runTest(JavaSamplerContext arg0) {
        server = arg0.getParameter("server");
        port = Integer.parseInt(arg0.getParameter("port"));
        database = arg0.getParameter("database");
        username = arg0.getParameter("username");
        password = arg0.getParameter("password");
        sql = arg0.getParameter("sql");

        result = new SampleResult();
        result.setThreadName(sql);
        //设置请求参数
        result.setRequestHeaders(sql);
        try {
            result.sampleStart();
            Map<String, Dao> cp = tp.get();
            String response = Dao.execute(server, String.valueOf(port), database, username, password, sql);
            System.out.println("签到返回:" + response);
            result.setResponseMessage(response);
            result.setSuccessful(true);
            result.setEndTime(System.currentTimeMillis());
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);

			/* print stack trace */
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new java.io.PrintWriter(stringWriter));
            result.setResponseData(stringWriter.toString(), "utf-8");
            result.setDataType(SampleResult.TEXT);
            result.setEndTime(System.currentTimeMillis());
        }
        return result;
    }

    public void teardownTest(JavaSamplerContext arg0) {
    }
}
