package com.tim.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.music.AudioLoadResult;
import com.tim.music.MusicController;

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
            if((state = m.getVoiceState())!= null){
                VoiceChannel vc;
                if((vc = state.getChannel().asVoiceChannel()) != null){
                    MusicController controller = DiscordBot.INSTANCE.playerManager.getController(vc.getIdLong());
                    AudioPlayerManager apm = DiscordBot.INSTANCE.audioPlayerManager;
                    AudioManager manager = controller.getGuild().getAudioManager();
                    manager.openAudioConnection(vc);

                    StringBuilder strBuilder = new StringBuilder();
                    for(int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");

                    String url = strBuilder.toString().trim();
                    if(!url.startsWith("http")){
                        url = "ytsearch: " + url;
                    }

                    apm.loadItem(url, new AudioLoadResult(controller, url));
                }
                else{
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
