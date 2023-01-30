package com.tim.commands;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class broadcast implements ServerCommand {
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        if(message.getAuthor().isBot()) return;

        if(m.getIdLong() == Long.parseLong(DiscordBot.INSTANCE.config.get("OWNERIDLONG"))){
            String[] args = message.getContentDisplay().split(" ");
            List<Long> servers = new ArrayList<>();

            if(args.length == 3){
                try{
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.addField(args[1],args[2],false);
                    
                    ResultSet set = SQL.onQuery("SELECT server FROM channeltime");

                    while(set.next()){
                        long id = set.getLong("server");
                        if(!servers.contains(id)){
                            servers.add(id);
                        }
                    }

                    for(int i = 0; i < servers.size();i++){
                        DiscordBot.INSTANCE.shardManager.getGuildById(servers.get(i)).getDefaultChannel().asTextChannel().sendMessageEmbeds(builder.build()).queue();
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            DiscordBot.embedsender("You're not the owner of the bot so you cannot send messages!", channel);
        }
    }

}
