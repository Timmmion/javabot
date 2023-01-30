package com.tim.music.commands;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.music.MusicController;
import com.tim.music.MusicUtil;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class SkipCommand implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        GuildVoiceState state;
        state = m.getVoiceState();
        if(state != null){
            try{
            VoiceChannel vc;
            vc = state.getChannel().asVoiceChannel();
                MusicController controller = DiscordBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
                AudioManager manager = controller.getGuild().getAudioManager();
                manager.openAudioConnection(vc);

                controller.getQueue().skip(channel);

                MusicUtil.updateChannel(channel);
                    
            }catch (Exception e){
                DiscordBot.embedsender("Sadly you're **not** in a voice channel :( Go there to use the Command!",channel);
                e.getStackTrace();
            }
        }
        else{
            DiscordBot.embedsender("Sadly you're **not** in a voice channel :( Go there to use the Command!",channel);
        }
    }
}
