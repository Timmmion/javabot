package com.tim.music.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class interpret implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        String[] args = message.getContentDisplay().split(" ");
        try{
            DiscordBot.INSTANCE.spotifyInterpreter.convert(args[1], channel);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
