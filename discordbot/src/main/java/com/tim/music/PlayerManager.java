package com.tim.music;

import java.util.concurrent.ConcurrentHashMap;

import com.tim.DiscordBot;

public class PlayerManager {
    
    public ConcurrentHashMap<Long,MusicController> controller;

    public PlayerManager(){
        controller = new ConcurrentHashMap<Long, MusicController>();
    }

    public MusicController getController(long guildId){
        MusicController mc = null;

        if(controller.containsKey(guildId)){
            mc = controller.get(guildId);
        }
        else{
            mc = new MusicController(DiscordBot.INSTANCE.shardManager.getGuildById(guildId));
            controller.put(guildId, mc);
        }

        return mc;
    }

    public long getGuildbyPlayerHash(int hash){
    for(MusicController controller : this.controller.values()){
        if(controller.getPlayer().hashCode() == hash){
            return controller.getGuild().getIdLong();
        }
    }

        return -1;
    }
}
