package com.tim;


import java.util.concurrent.ConcurrentHashMap;

import com.tim.channeltimer.commands.getCompleteLeaderboard;
import com.tim.channeltimer.commands.getTopLeaderboard;
import com.tim.commands.ExitCommand;
import com.tim.commands.broadcast;
import com.tim.commands.types.ServerCommand;
import com.tim.minigames.tictactoe.commands.showStats;
import com.tim.minigames.tictactoe.commands.tictactoeStart;
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

        commands.put("exit", new ExitCommand());
        commands.put("play", new PlayCommand());
        commands.put("stop", new StopCommand());
        commands.put("shuffle", new ShuffleCommand());
        commands.put("skip", new SkipCommand());
        commands.put("volume", new VolumeCommand());
        commands.put("leaderboard", new getTopLeaderboard());
        commands.put("leaderboardall", new getCompleteLeaderboard());
        commands.put("broadcast", new broadcast());
        commands.put("tictactoe", new tictactoeStart());
        commands.put("tttstats", new showStats());
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
