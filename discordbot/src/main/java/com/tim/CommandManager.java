package com.tim;


import java.util.concurrent.ConcurrentHashMap;

import com.tim.commands.ClearCommand;
import com.tim.commands.types.ServerCommand;
import com.tim.music.commands.PlayCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class CommandManager {
    
    public ConcurrentHashMap<String,ServerCommand> commands;

    public CommandManager(){
        commands = new ConcurrentHashMap<>();

        commands.put("clear", new ClearCommand());
        commands.put("play", new PlayCommand());
    }

    public boolean perform(String command,Member m, TextChannel channel, Message message){

        ServerCommand cmd;
        if((cmd = commands.get(command.toLowerCase())) != null){
            cmd.perfomCommand(m, channel, message);
            return true;
        }

        return false;
    }

}
