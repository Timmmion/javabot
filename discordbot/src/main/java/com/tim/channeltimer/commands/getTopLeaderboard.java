package com.tim.channeltimer.commands;

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

public class getTopLeaderboard implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Top 5 active users!");
        ResultSet set = SQL.onQuery("SELECT idlong, timeinmin FROM channeltime ORDER BY timeinmin DESC LIMIT 5");
        List<Long> idlist = new ArrayList<>();
        List<Long> timelist = new ArrayList<>();
        try {
            while(set.next()){
                long id = set.getLong("idlong");
                long time = set.getLong("timeinmin"); 

                idlist.add(id);
                timelist.add(time);
            }
            for(int i = 0;i < idlist.size();i++){
                builder.addField((i + 1) + ".", DiscordBot.INSTANCE.shardManager.getUserById(idlist.get(i)).getAsMention() + " " + timelist.get(i) + " minutes", false);
            }
            channel.sendMessageEmbeds(builder.build()).queue();;
        } catch (SQLException e) { e.printStackTrace(); }  
    }
}
