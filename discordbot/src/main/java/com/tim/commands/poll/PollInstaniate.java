package com.tim.commands.poll;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class PollInstaniate implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        
        Poll poll = new Poll(m,channel,message);
        DiscordBot.registerListener(poll, DiscordBot.INSTANCE.shardManager);
        
    }
}
