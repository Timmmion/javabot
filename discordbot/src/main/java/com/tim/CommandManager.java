package com.tim;

import java.util.concurrent.ConcurrentHashMap;

import com.tim.birthday.addbirthday;
import com.tim.birthday.removebirthday;
import com.tim.birthday.showbirthday;
import com.tim.commands.ExitCommand;
import com.tim.commands.NotifySwitch;
import com.tim.commands.WhenIsFriday;
import com.tim.commands.broadcast;
import com.tim.commands.downloadServerIcon;
import com.tim.commands.help;
import com.tim.commands.randomTimeout;
import com.tim.commands.types.ServerCommand;
import com.tim.listeners.poll.PollInstaniate;
import com.tim.minigames.tictactoe.commands.tictactoeStart;
import com.tim.music.commands.PlayCommand;
import com.tim.music.commands.ShuffleCommand;
import com.tim.music.commands.SkipCommand;
import com.tim.music.commands.StopCommand;
import com.tim.music.commands.VolumeCommand;
import com.tim.scheduler.channeltimer.commands.getCompleteLeaderboard;
//import com.tim.scheduler.channeltimer.commands.getTopLeaderboard;
import com.tim.scheduler.channeltimer.commands.stats;

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
        //commands.put("leaderboard", new getTopLeaderboard());
        commands.put("lb", new getCompleteLeaderboard());
        commands.put("broadcast", new broadcast());
        commands.put("tictactoe", new tictactoeStart());
        commands.put("stats", new stats());
        commands.put("randomtimeout", new randomTimeout());
        commands.put("poll", new PollInstaniate());
        commands.put("bdadd", new addbirthday());
        commands.put("bdremove", new removebirthday());
        commands.put("bdshow", new showbirthday());
        commands.put("notification", new NotifySwitch());
        commands.put("wif", new WhenIsFriday());
        commands.put("icon", new downloadServerIcon());
        commands.put("help", new help());
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
