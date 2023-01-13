package com.tim.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.LiteSQL;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.sharding.ShardManager;

public class ShutdownCommand implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {

        ShardManager shardManager = DiscordBot.INSTANCE.shardManager;

        if(message.getAuthor().isBot()) return;
        if(m.isOwner()){
            if(shardManager != null){   
                channel.sendMessage("**Going to sleep goodbye :wave:**").queue();;
                shardManager.setStatus(OnlineStatus.OFFLINE);
                DiscordBot.INSTANCE.svManager.endGame();
                shardManager.shutdown();
                LiteSQL.disconnect();
                System.out.println("BOT OFFLINE!");
            }
        }else{
            channel.sendMessage("You're not the Owner you can't shutdown the server!").queue();
        }
    

        return;
    }
}
