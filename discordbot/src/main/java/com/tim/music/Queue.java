package com.tim.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.tim.DiscordBot;

public class Queue {

	private List<AudioTrack> queuelist;
	private MusicController controller;
	
	public Queue(MusicController controller) {
		this.setController(controller);
		this.setQueuelist(new ArrayList<AudioTrack>());
	}
	
	
	public boolean next() {
		if(this.queuelist.size() >= 1) {
			AudioTrack currenttrack = queuelist.remove(0);
			
			if(currenttrack != null) {
				this.controller.getPlayer().playTrack(currenttrack);
				return true;
			}

		}
		
		return false;
	}
	
	public void addTrackToQueue(AudioTrack track) {
		this.queuelist.add(track);
		
		if(controller.getPlayer().getPlayingTrack() == null) {
			next();
		}
	}
	

	public MusicController getController() {
		return controller;
	}

	public void setController(MusicController controller) {
		this.controller = controller;
	}

	public List<AudioTrack> getQueuelist() {
		return queuelist;
	}

	public void setQueuelist(List<AudioTrack> queuelist) {
		this.queuelist = queuelist;
	}
	
	public void shuffel() {
		Collections.shuffle(queuelist);
	}

}