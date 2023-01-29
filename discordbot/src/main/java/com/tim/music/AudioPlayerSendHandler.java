package com.tim.music;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler{

    private final AudioPlayer audioplayer;
    private AudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayerC){
        audioplayer = audioPlayerC;
    }

    @Override
    public boolean canProvide() {
        if (lastFrame == null)
        {
            lastFrame = audioplayer.provide();
        }

        return lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        if (lastFrame == null)
        {
            lastFrame = audioplayer.provide();
        }

        byte[] data = lastFrame != null ? lastFrame.getData() : null;
        lastFrame = null;

        return ByteBuffer.wrap(data);
    }
    
    @Override
    public boolean isOpus() {
        return true;
    }
}
