package com.tim.birthday;

import java.sql.ResultSet;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class showbirthday implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        
        String prefix = DiscordBot.PREFIX;
        if(message.getAuthor().isBot()) return;

        try{
            Member target = message.getMentions().getMembers().get(0);
            if(SQL.checkConnection()){
                ResultSet set = SQL.onQuery("SELECT date FROM birthday WHERE idlong='" + target.getIdLong() + "'");
                if(set.next()){
                    String date = set.getString("date");
                    EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(target.getUser().getName() + "s birthday!")
                        .setDescription(date)
                        .setColor(DiscordBot.color);
                    channel.sendMessageEmbeds(builder.build()).queue();
                }else{
                    DiscordBot.embedsender(target.getUser().getName()  + " has no registered birthday!", channel);
                }
            }

        }catch(Exception e){
            DiscordBot.embedsender("Use " + prefix + "bdshow @user", channel);
            e.printStackTrace();
        }
        
    }
}
