package com.tim.commands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class randomTimeout implements ServerCommand{    

    List<Member> finalmember = new ArrayList<>();
    Random rand = new Random();

    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        
        try{
            Duration duration = Duration.of(rand.nextLong(360), ChronoUnit.SECONDS);
            Member mem = m.getGuild().getMemberById("566207040288981018");
            
            mem.timeoutFor(duration).queue();
            DiscordBot.embedsender(mem.getUser().getName() + " was timeouted for " + duration.getSeconds() + " seconds! xD", channel);
        }catch(Exception e){
            DiscordBot.embedsender("Timeout failed!", channel);
        }

    }
}
