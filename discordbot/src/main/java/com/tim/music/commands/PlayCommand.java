package com.tim.music.commands;

import java.util.ArrayList;

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
    
    AudioPlayerManager apm;

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
                    AudioManager manager = controller.getGuild().getAudioManager();
                    apm = DiscordBot.INSTANCE.audioPlayerManager;
                    manager.openAudioConnection(vc);

                    MusicUtil.updateChannel(channel);

                    StringBuilder strBuilder = new StringBuilder();
                    for(int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");

                    String url = strBuilder.toString().trim();

                    if(url.startsWith("https://open.spotify.com")){
                        ArrayList<String> name = DiscordBot.INSTANCE.spotifyInterpreter.convert(url);
                        
                        if(!controller.getPlayer().isPaused()){
                            if(name.size() == 1) {
                                DiscordBot.embedsender("Added the Track to queue!", channel);
                            }else{
                                DiscordBot.embedsender("Added " + name.size() + " Tracks to the queue!",  channel);
                            }
                            
                        }
                        for(String str : name){
                            startapm("ytsearch: " + str + " audio ", controller);
                        }



                        return;
                    }

                    if(!url.startsWith("http")){
                        url = "ytsearch: " + url + " audio";
                        if(!controller.getPlayer().isPaused()){
                            DiscordBot.embedsender("Added the Track to queue!", channel);
                        }
                    }
                    
                    startapm(url, controller);

                }catch (Exception e)
                {
                    DiscordBot.embedsender("Sadly you're **not** in a voice channel :( Go there to use the Command!",channel);
                }
            }
            else{
                DiscordBot.embedsender("Sadly you're **not** in a voice channel :( Go there to use the Command!",channel);
            }
        }
        else {
            DiscordBot.embedsender("Bitte benutze **" + DiscordBot.PREFIX + "play [url]** !",channel);
        }
    }

    public void startapm(String finishedurl, MusicController c){
        apm.loadItem(finishedurl, new AudioLoadResult(c, finishedurl));
    }
}
