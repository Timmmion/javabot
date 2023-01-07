package com.tim.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.music.MusicController;
import com.tim.music.MusicUtil;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class VolumeCommand implements ServerCommand{

    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        String[] args = message.getContentDisplay().split(" ");

        GuildVoiceState state = m.getVoiceState();
        if(state != null){
            try{
                VoiceChannel vc = state.getChannel().asVoiceChannel();
                MusicController controller = DiscordBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
                AudioPlayer player = controller.getPlayer();

                MusicUtil.updateChannel(channel);
                
                int volume = Integer.valueOf(args[1]);

                if(volume >= 0 && volume <= 100){
                    player.setVolume(volume);
                    channel.sendMessage("Volume set to: **" + volume + "**!").queue();
                }else{
                    channel.sendMessage("Volume can't be highter then **100**").queue();
                }

                
            }catch(Exception e){
                e.getStackTrace();
            }
        }

        
    }
}
