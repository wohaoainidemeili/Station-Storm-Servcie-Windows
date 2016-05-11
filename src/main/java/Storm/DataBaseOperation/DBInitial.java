package Storm.DataBaseOperation;

import Storm.SensorConfigInfo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * used to initial the database by the DBInitial.sql in resources
 * Created by Yuan on 2016/5/9.
 */
public class DBInitial {
    public static void initial(){
        //get sql from dbinitial.sql
        List<String> sqlList=new ArrayList<String>();
        try {
            InputStream sqlFile=ClassLoader.getSystemResourceAsStream("DBInitial.sql");//get the file as stream under resources
            StringBuffer sqlBuffer=new StringBuffer();
            byte[] buff=new byte[1024];
            int byteReader=0;
            while ((byteReader=sqlFile.read(buff))!=-1){
                sqlBuffer.append(new String(buff,0,byteReader));
            }

            // for linux and windows
            String[] sqlArr = sqlBuffer.toString().split("(;\\s*\\r\\n)|(;\\s*\\n)");
            for (int i=0;i<sqlArr.length;i++){
                String sql=sqlArr[i].replace("- -.*","").trim();
                if (!sql.equals("")) sqlList.add(sql);
            }
        } catch (Exception e) {
            System.out.println("open the sql file error!" + e.getMessage());
        }

        //get the database initial document
        String host= SensorConfigInfo.getHost();
        String user=SensorConfigInfo.getUser();
        String passWord=SensorConfigInfo.getPassword();
        String dbname= SensorConfigInfo.getDbName();
        String driver=SensorConfigInfo.getDriver();

        //get the connection
        SqlConnectionPool connectionPool=new SqlConnectionPool(host,dbname,user,passWord,driver);
        Connection con= connectionPool.getConnection();
        Statement state=null;

        //create tables
        try {
            state=con.createStatement();
            for(String sql:sqlList) {
                state.addBatch(sql);
            }
            state.executeBatch();
        } catch (SQLException e) {
            System.out.print("create table error!"+e.getMessage());
        }
        finally {
            if (state!=null){
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
