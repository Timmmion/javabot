package com.tim.music;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.tim.DiscordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {
    @Override
    public void onPlayerPause(AudioPlayer player) {
        super.onPlayerPause(player);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        super.onPlayerResume(player);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        long guildid = DiscordBot.INSTANCE.playerManager.getGuildbyPlayerHash(player.hashCode());
        AudioTrackInfo info = track.getInfo();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.GREEN);

        builder.setTitle("**Now playing:** " + info.title);
        MusicUtil.sendEmbed(guildid, builder, true);
        
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        long guildid = DiscordBot.INSTANCE.playerManager.getGuildbyPlayerHash(player.hashCode());
		Guild guild = DiscordBot.INSTANCE.shardManager.getGuildById(guildid);

        MusicController controller = DiscordBot.INSTANCE.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		
		if(endReason.mayStartNext) {
			
			if(queue.next())
				return;
		}
		
		AudioManager manager = guild.getAudioManager();

        if(queue.getQueuelist().isEmpty()){
            player.stopTrack();
            manager.closeAudioConnection();
        }
    }
}
