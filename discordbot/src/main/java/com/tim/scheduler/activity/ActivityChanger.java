package com.tim.scheduler.activity; 

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tim.DiscordBot;

import net.dv8tion.jda.api.entities.Activity;

public class ActivityChanger {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    String[] wachting = {"Hentai" , "Porn" , "Fischis Fische an", "auf Janoix Ass"};
    String[] listening = {"nichts"};
    String[] playing = {"Fortnite" , "Virgin Impact"};

    public void startScheduler(){
        scheduler.scheduleAtFixedRate(
            new Runnable(){
                public void run(){
                    Random rand = new Random();
                    
                    int i = 0;
                    i = rand.nextInt(3);

                    switch(i){
                        case 0:
                            DiscordBot.INSTANCE.shardManager.setActivity(Activity.watching(wachting[rand.nextInt(wachting.length)]));
                            break;

                        case 1:
                            DiscordBot.INSTANCE.shardManager.setActivity(Activity.listening(listening[rand.nextInt(listening.length)]));
                            break;

                        case 2: 
                            DiscordBot.INSTANCE.shardManager.setActivity(Activity.playing(playing[rand.nextInt(playing.length)]));
                            break;

                        default:
                            DiscordBot.INSTANCE.shardManager.setActivity(Activity.watching(wachting[rand.nextInt(wachting.length)]));
                            break;
                    }
                }
            }, 15, 15, TimeUnit.SECONDS);
    }
}
