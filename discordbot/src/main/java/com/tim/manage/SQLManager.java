package com.tim.manage;

public class SQLManager {
    
    public static void onCreate(){

        // id guildid channelid messageid emote roleid

        LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS musicchannel(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, channelid INTEGER)");
    }

}
