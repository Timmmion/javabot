package com.tim.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.sharding.ShardManager;

public class ExitCommand implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {

        ShardManager shardManager = DiscordBot.INSTANCE.shardManager;
        String idOwner = DiscordBot.INSTANCE.config.get("OWNERIDLONG");


        if(message.getAuthor().isBot()) return;
        if(m.getIdLong() == Long.parseLong(idOwner)){
            if(shardManager != null){   
                DiscordBot.embedsender("**Going to sleep goodbye :wave:**",channel);
                shardManager.setStatus(OnlineStatus.OFFLINE);
                shardManager.shutdown();
                SQL.disconnect();
                System.out.println("BOT OFFLINE!");
            }
        }else{
            DiscordBot.embedsender("You're not the owner of the bot so you cannot turn it off!",channel);
        }

        return;
    }
}
