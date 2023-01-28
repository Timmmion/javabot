package com.tim.manage;

public class SQLManager {
    
    public static void onCreate(){

        // id guildid channelid messageid emote roleid

        SQL.onUpdate("CREATE TABLE IF NOT EXISTS musicchannel(id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, guildid BIGINT, channelid BIGINT)");
    }

}
