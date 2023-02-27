package com.tim.commands;

import java.sql.ResultSet;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class NotifySwitch implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        
        if(m.isOwner()){

            ResultSet set = SQL.onQuery("SELECT switch FROM notification WHERE idlong='" + m.getGuild().getIdLong() + "'");
            try{
                if(set.next()){
                
                    boolean bool = set.getBoolean("switch");
    
                    if(bool){

                        SQL.onUpdate("UPDATE notification SET switch='0'");
                        DiscordBot.embedsender("Notification set to :x:", channel);

                    }else{

                        SQL.onUpdate("UPDATE notification SET switch='1'");
                        DiscordBot.embedsender("Notification set to :white_check_mark:", channel);

                    }
    
                }else{
                    SQL.onUpdate("INSERT INTO notification VALUES(" + m.getGuild().getIdLong() + ", 0)");
                    DiscordBot.embedsender("Notification set to :x: ", channel);
                }
            }catch(Exception e){
                e.printStackTrace();
            }


        }else{
            DiscordBot.embedsender("Only the OWNER is allowed to change the notification settings!", channel);
        }

    }

}
