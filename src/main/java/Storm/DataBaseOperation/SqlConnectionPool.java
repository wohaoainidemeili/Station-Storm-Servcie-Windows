package Storm.DataBaseOperation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * create sql connection pool of the mysql database
 * Created by Yuan on 2016/5/10.
 */
public class SqlConnectionPool {
    String host;
    String dbName;
    String user;
    String password;
    String driver;
    public SqlConnectionPool(String host,String dbName,String user,String password,String driver){
        this.host=host;
        this.dbName=dbName;
        this.user=user;
        this.password=password;
        this.driver=driver;
    }

    /**
     * get the connection of database
     * @return connection
     */
    public Connection getConnection(){
        Connection con=null;

        //connect to the default db (mysql)
        String connectUrl="jdbc:mysql://"+host+"/mysql?"+"user="+user+"&password="+password+"&useUnicode=true&characterEncoding=utf8";
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(connectUrl);
            Statement statement=con.createStatement();
            //create database if not exists
            statement.execute("create database if not EXISTS "+dbName);
            //get the connection of the created database
            con=DriverManager.getConnection("jdbc:mysql://"+host+"/"+dbName,user,password);
        } catch (ClassNotFoundException e) {
           throw new ClassCastException("Database Driver NOT Found:"+e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Encounter an error"+e.getMessage());
        }
        return con;
    }
}
