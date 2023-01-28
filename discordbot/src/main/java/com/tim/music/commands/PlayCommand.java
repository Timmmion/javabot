package com.tim.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.music.AudioLoadResult;
import com.tim.music.MusicController;
import com.tim.music.MusicUtil;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        String[] args = message.getContentDisplay().split(" ");

        if(args.length > 1){
            GuildVoiceState state;
            state = m.getVoiceState();
            if(state != null){
                try{
                VoiceChannel vc;
                vc = state.getChannel().asVoiceChannel();
                    MusicController controller = DiscordBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
                    AudioPlayerManager apm = DiscordBot.INSTANCE.audioPlayerManager;
                    AudioManager manager = controller.getGuild().getAudioManager();
                    manager.openAudioConnection(vc);

                    MusicUtil.updateChannel(channel);

                    StringBuilder strBuilder = new StringBuilder();
                    for(int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");

                    String url = strBuilder.toString().trim();
                    if(!url.startsWith("http")){
                        url = "ytsearch: " + url + " audio";
                    }
                    
                    apm.loadItem(url, new AudioLoadResult(controller, url));
                }catch (Exception e)
                {
                    channel.sendMessage("Sadly you're **not** in a voice channel :( Go there to use the Command!").queue();
                }
            }
            else{
                channel.sendMessage("Sadly you're **not** in a voice channel :( Go there to use the Command!").queue();
            }
        }
        else {
            channel.sendMessage("Bitte benutze **" + DiscordBot.PREFIX + "play [url]** !").queue();
        }
    }
}
