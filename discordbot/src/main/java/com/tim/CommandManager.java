package com.tim;


import java.util.concurrent.ConcurrentHashMap;

import com.tim.commands.ClearCommand;
import com.tim.commands.ShutdownCommand;
import com.tim.commands.types.ServerCommand;
import com.tim.minigames.sv.commands.placeShip;
import com.tim.minigames.sv.commands.svStartGame;
import com.tim.music.commands.PlayCommand;
import com.tim.music.commands.ShuffleCommand;
import com.tim.music.commands.SkipCommand;
import com.tim.music.commands.StopCommand;
import com.tim.music.commands.VolumeCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class CommandManager {
    
    public ConcurrentHashMap<String,ServerCommand> commands;

    public CommandManager(){
        commands = new ConcurrentHashMap<>();

        commands.put("shutdown", new ShutdownCommand());
        commands.put("clear", new ClearCommand());
        commands.put("play", new PlayCommand());
        commands.put("stop", new StopCommand());
        commands.put("shuffle", new ShuffleCommand());
        commands.put("skip", new SkipCommand());
        commands.put("volume", new VolumeCommand());
        commands.put("sv", new svStartGame());
        commands.put("svship", new placeShip());
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
