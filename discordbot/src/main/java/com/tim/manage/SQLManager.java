package com.tim.manage;

public class SQLManager {
    
    public static void onCreate(){

        SQL.onUpdate("CREATE TABLE IF NOT EXISTS musicchannel(id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, guildid BIGINT, channelid BIGINT)");
        SQL.onUpdate("CREATE TABLE IF NOT EXISTS channeltime(name MEDIUMTEXT,idLong BIGINT,timeinmin BIGINT,server BIGINT)");
        SQL.onUpdate("CREATE TABLE IF NOT EXISTS tttstats(name MEDIUMTEXT,idLong BIGINT,wins BIGINT,loses BIGINT, ties BIGINT)");
        SQL.onUpdate("CREATE TABLE IF NOT EXISTS birthday(name MEDIUMTEXT, idlong BIGINT, date MEDIUMTEXT)");
    }

}


