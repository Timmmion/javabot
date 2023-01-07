package com.tim.listeners;


import com.tim.DiscordBot;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();

        if(event.isFromType(ChannelType.TEXT)){
            TextChannel channel = event.getChannel().asTextChannel(); 

            if(message.startsWith(DiscordBot.PREFIX)){
                String[] args = message.substring(1).split(" ");
    
                if(args.length > 0){
                    if(!DiscordBot.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, event.getMessage())){
                        channel.sendMessage(message.replace(DiscordBot.PREFIX, "")).queue();
                        channel.sendMessage("**Unknown Command!**").queue();
                    }
                }
            }
        }
    }
}
