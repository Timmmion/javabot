package com.tim.scheduler.channeltimer.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class getCompleteLeaderboard implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        
        if(SQL.checkConnection()){

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Leaderboard TOP 20!");
            builder.setColor(DiscordBot.color);
            
            ResultSet set = SQL.onQuery("SELECT name, idlong, timeinmin FROM channeltime WHERE server='" + m.getGuild().getIdLong() + "' ORDER BY timeinmin DESC LIMIT 20");
            List<Long> idlist = new ArrayList<>();
            List<Long> timelist = new ArrayList<>();

            try {
                while(set.next()){
                    long id = set.getLong("idlong");
                    long time = set.getLong("timeinmin"); 

                    idlist.add(id);
                    timelist.add(time);
                }

                String part = "";

                for(int i = 0;i < idlist.size();i++){
                    
                    if(DiscordBot.INSTANCE.shardManager.getUserById(idlist.get(i)) != null){
                        part += "**"+ (i+1) + ".** " +  DiscordBot.INSTANCE.shardManager.getUserById(idlist.get(i)).getAsMention() + " " + timelist.get(i) + "minutes \n";
                    }

                }   

                builder.setDescription(part);

                channel.sendMessageEmbeds(builder.build()).queue();

            } catch (SQLException e) { e.printStackTrace(); }  

        }else{
            DiscordBot.embedsender("**SQL ERROR!** Pleas message the owner of the bot :(", channel);
        }
    }
}
