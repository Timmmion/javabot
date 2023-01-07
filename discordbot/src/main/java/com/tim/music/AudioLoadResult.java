package com.tim.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;

public class AudioLoadResult implements AudioLoadResultHandler{

    private final MusicController controller;
    private final String uri;

    public AudioLoadResult(MusicController conroller, String uri){
        this.controller = conroller;
        this.uri = uri;
    }

    @Override
    public void loadFailed(FriendlyException arg0) {
        
    }

    @Override
    public void noMatches() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        Queue queue = controller.getQueue();
        if(uri .startsWith("ytsearch: ")){
            queue.addTrackToQueue(playlist.getTracks().get(0));
            return;
        }

        int added = 0;
        
        for(AudioTrack track : playlist.getTracks()){
            queue.addTrackToQueue(track);
            added++;
        }

        EmbedBuilder builder = new EmbedBuilder().setDescription("Added **" + added + "** Track/s to the queue!");
        
        MusicUtil.sendEmbed(controller.getGuild().getIdLong(), builder, true);
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        controller.getPlayer().playTrack(track);
        
    }
    
}
