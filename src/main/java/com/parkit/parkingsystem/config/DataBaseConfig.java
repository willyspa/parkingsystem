package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {

        Properties properties = new Properties();
        FileInputStream in = null;
        try{
            in = new FileInputStream(("src/main/resources/db.properties"));
            properties.load(in);
        }catch(IOException e){
            System.out.println("error on FileInputStream: "+e);
        }finally {
            if ( in != null ){
                try {
                    in.close();
                }catch( Exception e ){
                    System.out.println("the stream failed to close :"+e);
                }
            }

        }
        String driver = properties.getProperty("ConnectionDB.driver");
        String url = properties.getProperty("ConnectionDB.url");
        String username = properties.getProperty("ConnectionDB.username");
        String password = properties.getProperty("ConnectionDB.password");

        Class.forName(driver);
        logger.info("Create DB connection");

        return DriverManager.getConnection(url,username,password);
    }

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
