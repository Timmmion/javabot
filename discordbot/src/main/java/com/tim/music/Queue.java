package com.tim.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

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

	public void skip(TextChannel channel){
		if(!queuelist.isEmpty()){
			AudioTrack nextTrack = queuelist.get(0);
			controller.getPlayer().playTrack(nextTrack);
			queuelist.remove(nextTrack);
			channel.sendMessage("**Skipped!** " + queuelist.size() + " remaining songs in the playlist").queue();
		}else{
			channel.sendMessage("Can't skip anything when the playlist is empty xD").queue();
		}
	}

}