package com.tim.music.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.music.MusicController;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class ShuffleCommand implements ServerCommand{

    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        GuildVoiceState state;
        state = m.getVoiceState();
        if(state!= null){
            try{
                VoiceChannel vc;
                vc = state.getChannel().asVoiceChannel();
                if(vc != null){
                    MusicController controller = DiscordBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
                    controller.getQueue().shuffel();
                    DiscordBot.embedsender("**Shuffled!**",channel);
                }
            }catch(Exception e){
                DiscordBot.embedsender("You **can't** shuffle because you're not in the same channel as the bot!",channel);
            }
        }
    }
}
