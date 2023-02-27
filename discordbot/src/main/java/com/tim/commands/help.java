package com.tim.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class help implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {

        EmbedBuilder builder = new EmbedBuilder()
            .setColor(DiscordBot.color)
            .setTitle("Commands/Feature Help")
            .addField("Commands:","&play \n &stop \n &shuffle \n &skip \n &volume \n &lb \n &tictactoe \n &stats \n &poll \n &bdadd \n &bdremove \n &bdshow \n &notification",true)
            .addField("","Play a song! \n Stop the queue! \n Shuffle the queue! \n Skip one song! \n Set volume from 1 to 100 [Default value = 10] \n Show the leaderboard! \n Start a TicTacToe game! \n Show the stats from a user! \n Create a poll! \n Set your birthday! \n Remove your birthday! \n Shows the date of a birthday \n Turns the notification on/off",true);
        channel.sendMessageEmbeds(builder.build()).queue();
    }
}
