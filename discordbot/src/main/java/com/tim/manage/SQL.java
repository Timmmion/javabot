package com.tim.manage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import com.tim.DiscordBot;

import io.github.cdimascio.dotenv.Dotenv;

public class SQL {
    
    private static Connection con;
    private static Statement statement;
    private static final Dotenv config = DiscordBot.INSTANCE.config;

    public static void connect(){
        con = null;

        String url = "jdbc:mysql://" + config.get("IPADRESS") + ":3306/discordDB";
        String username = config.get("USERNAMEMYSQL");
        String password = config.get("PASSWORD");
        

        try{

            //Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            System.out.println("Connected to Database! " + LocalDateTime.now());

            statement = con.createStatement();

        }catch (SQLException e){
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
    
    public static boolean checkConnection(){
        try{
            if(con.isClosed()){
                System.out.println("DISCONNECTED! " + LocalDateTime.now());
                SQL.connect();
                return true;
            }else{
            }
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
