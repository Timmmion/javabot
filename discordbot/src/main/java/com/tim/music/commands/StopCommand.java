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
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand implements ServerCommand{
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        GuildVoiceState state;
        state = m.getVoiceState();
        if(state != null){
            try{
            VoiceChannel vc;
            vc = state.getChannel().asVoiceChannel();
            MusicController controller = DiscordBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
            AudioManager manager = vc.getGuild().getAudioManager();
            AudioPlayer player = controller.getPlayer();

            MusicUtil.updateChannel(channel);

            player.stopTrack();
            manager.closeAudioConnection();
            channel.sendMessage("**Stopped!**").queue();
            }catch (Exception e){
                channel.sendMessage("You can't stop the bot when you're **not** in the **same** channel!").queue();
            }
        }
    }
}
