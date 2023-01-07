package com.tim.manage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LiteSQL {
    
    private static Connection con;
    private static Statement statement;

    public static void connect(){
        con = null;

        try{
            File file = new File("data.db");
            if(!file.exists()){
                file.createNewFile();
            }

            String url = "jdbc:sqlite: " + file.getPath();
            con = DriverManager.getConnection(url);

            System.out.println("Connected to Database!");

            statement = con.createStatement();

        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try{
            if(con != null){
                con.close();
                System.out.println("Disconnected from Database!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void onUpdate(String sql){
        try{
            statement.execute(sql);
            

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static ResultSet onQuery(String sql){
        try{
            return statement.executeQuery(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
