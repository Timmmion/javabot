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
            System.out.println(controller.get(guildId).toString());
            controller.put(guildId, mc);
        }

        return mc;
    }
}
