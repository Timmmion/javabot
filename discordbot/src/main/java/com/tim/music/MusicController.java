package com.tim.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.tim.DiscordBot;

import net.dv8tion.jda.api.entities.Guild;

public class MusicController {
    
    private Guild guild;
    private AudioPlayer player;
    private Queue queue;
    
    public MusicController(Guild guilda){
        guild = guilda;

        player = DiscordBot.INSTANCE.audioPlayerManager.createPlayer();
        queue = new Queue(this);

        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
        player.addListener(new TrackScheduler());
        player.setVolume(10);
    }

    public Guild getGuild() {
        return guild;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public Queue getQueue(){
        return queue;
    }

}
