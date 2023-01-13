package com.tim.minigames.sv.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class placeShip implements ServerCommand{

    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        if(DiscordBot.INSTANCE.svManager.isRunning == true){
            DiscordBot.INSTANCE.svManager.placeShip(m,channel,message);
        }
        channel.sendMessage("There is currently no game! Start a game to place ships :D");
        
    }
    
}
