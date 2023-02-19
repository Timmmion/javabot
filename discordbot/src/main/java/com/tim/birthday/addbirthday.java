package com.tim.birthday;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class addbirthday implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {

        String prefix = DiscordBot.PREFIX;
        String[] args = message.getContentDisplay().split(" ");
        LocalDate date = null;

        if(m.getUser().isBot()) return;
        if(args.length >= 1){

            try{
                date = LocalDate.parse(args[1],DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }catch(Exception e){
                DiscordBot.embedsender("Use " + prefix + "bdadd dd-MM-yyyy", channel);
                e.printStackTrace();
            }

            if(SQL.checkConnection() && date != null){
                try{
                    ResultSet set = SQL.onQuery("SELECT * FROM birthday WHERE idlong='" + m.getIdLong() + "'");
                    if(set.next()){
                        SQL.onUpdate("UPDATE birthday SET date='" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "' WHERE idlong='" + m.getIdLong() + "'");
                        DiscordBot.embedsender("Birthday overwritten!", channel);
                    }else{
                        SQL.onUpdate("INSERT INTO birthday VALUES('" + m.getUser().getName() + "' , '" + m.getIdLong() + "' , '" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +  "')");
                        DiscordBot.embedsender("Birthday set!", channel);
                    }

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

        }
    }
}
