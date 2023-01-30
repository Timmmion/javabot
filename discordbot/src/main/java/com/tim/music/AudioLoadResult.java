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
        EmbedBuilder builder = new EmbedBuilder().setTitle("Load failed!");
        MusicUtil.sendEmbed(controller.getGuild().getIdLong(), builder, true);
    }

    @Override
    public void noMatches() {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Found no song!");
        MusicUtil.sendEmbed(controller.getGuild().getIdLong(), builder, true);
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

        EmbedBuilder builder = new EmbedBuilder().setTitle("Added **" + added + "** Tracks to the queue!");
        
        MusicUtil.sendEmbed(controller.getGuild().getIdLong(), builder, true);
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        if(controller.getPlayer().isPaused()){
            controller.getPlayer().playTrack(track);
        }else{
            controller.getQueue().addTrackToQueue(track);
            if(controller.getQueue().queueNotEmpty()){
                EmbedBuilder builder = new EmbedBuilder().setTitle("Added the Track to queue!");
                MusicUtil.sendEmbed(controller.getGuild().getIdLong(), builder, true);
            }
        }
    }

}
