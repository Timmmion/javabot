package com.tim.commands;

import java.awt.Color;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class WhenIsFriday implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        
        if(m.getUser().isBot()) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextFriday = now.with(DayOfWeek.FRIDAY);

        long days = now.until(nextFriday, ChronoUnit.DAYS);
        long restseconds = 86400 - now.toLocalTime().toSecondOfDay();

        long hours = restseconds / 60 / 60;

        long minutes = restseconds / 60 - hours * 60 ;

        long seconds = restseconds % 60;

        EmbedBuilder builder = new EmbedBuilder()
            .setColor(DiscordBot.color)
            .setDescription("Next Friday is in **" + days + "** days **"  +  hours + "** hours **" + minutes + "** minutes and **" + seconds + "** seconds! :beers: ");

        channel.sendMessageEmbeds(builder.build()).queue();
    }

}
