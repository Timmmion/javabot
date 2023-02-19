package com.tim.birthday;

import java.sql.ResultSet;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class removebirthday implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {

        if(m.getUser().isBot()) return;
        if(SQL.checkConnection()){

            try{

                ResultSet set = SQL.onQuery("SELECT date FROM birthday WHERE idlong='" + m.getIdLong() + "'");
                if(set.next()){
                    SQL.onUpdate("DELETE FROM birthday WHERE idlong='" + m.getIdLong() + "'");
                    DiscordBot.embedsender("Birthday removed!", channel);
                }else{
                    DiscordBot.embedsender("There is no birthday to remove!", channel);
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }

        }else{
            DiscordBot.embedsender("**SQL ERROR!** Pleas try again!", channel);
        }
    }
}
