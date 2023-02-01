package com.tim.minigames.tictactoe.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.minigames.tictactoe.gameManager;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class tictactoeStart implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        gameManager man = new gameManager(m, channel, message);
        DiscordBot.registerListener(man, DiscordBot.INSTANCE.shardManager);
    }
}
