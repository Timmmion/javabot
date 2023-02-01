package com.tim.minigames.tictactoe.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class showStats implements ServerCommand{

    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        EmbedBuilder builder = new EmbedBuilder();
        Member p;
        try{
            p = message.getMentions().getMembers().get(0);
        }catch(Exception e){
            e.printStackTrace();
            DiscordBot.embedsender("You have to mention the Player! &tttstats @player", channel);
            return;
        }
        builder.setTitle(p.getUser().getName() + "s stats!");
        builder.setColor(DiscordBot.color);
        try {
            ResultSet set = SQL.onQuery("SELECT wins, loses, ties FROM tttstats WHERE idlong='" + p.getIdLong() + "'");
            while(set.next()){
                long win = set.getLong("wins"); 
                long loses = set.getLong("loses"); 
                long tie = set.getLong("ties"); 
                builder.addField("Wins:", Long.toString(win, 0),true);
                builder.addField("Loses:", Long.toString(loses, 0),true);
                builder.addField("Ties:", Long.toString(tie, 0),true);
            }

            channel.sendMessageEmbeds(builder.build()).queue();
        } catch (SQLException e) { e.printStackTrace(); DiscordBot.embedsender("No information about this user!", channel); SQL.lostConnection(); }  
    }
    
}
