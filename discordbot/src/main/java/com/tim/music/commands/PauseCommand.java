package com.tim.music.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.music.MusicController;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class PauseCommand implements ServerCommand{

    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {

        GuildVoiceState state;
        state = m.getVoiceState();
        if(state != null){
            try{
            VoiceChannel vc;
            vc = state.getChannel().asVoiceChannel();
                MusicController controller = DiscordBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
                if(!controller.getPlayer().isPaused()){
                    controller.getPlayer().setPaused(true);
                    DiscordBot.embedsender("Paused! Type &resume to continue!", channel);
                }else{
                    DiscordBot.embedsender("Already paused!", channel);
                }

            }catch (Exception e){
                DiscordBot.embedsender("Sadly you're **not** in a voice channel :( Go there to use the Command!",channel);
            }
        }
        else{
            DiscordBot.embedsender("Sadly you're **not** in a voice channel :( Go there to use the Command!",channel);
        }
    }

}
