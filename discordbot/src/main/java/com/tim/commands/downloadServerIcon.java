package com.tim.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class downloadServerIcon implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        String img = m.getGuild().getIconUrl();

        EmbedBuilder builder = new EmbedBuilder()
            .setColor(DiscordBot.color)
            .setTitle(m.getGuild().getName() + "s icon")
            .setImage(img);

        channel.sendMessageEmbeds(builder.build()).queue();
    }
}
