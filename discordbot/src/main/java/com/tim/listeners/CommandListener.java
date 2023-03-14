package com.tim.listeners;


import java.util.ArrayList;
import java.util.List;

import com.tim.DiscordBot;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

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
                        DiscordBot.embedsender("**Unknown Command!**",channel);
                    }
                }
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("active")){
            event.reply("Got it").queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("active", "make active"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
