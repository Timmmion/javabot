package com.tim.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

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
    public void playlistLoaded(AudioPlaylist arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        controller.getPlayer().playTrack(track);
        
    }
    
}
