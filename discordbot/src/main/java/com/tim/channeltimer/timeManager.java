package com.tim.channeltimer;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tim.DiscordBot;
import com.tim.manage.SQL;

public class timeManager extends ListenerAdapter {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startScheduleTask() {

        scheduler.scheduleAtFixedRate(
        new Runnable() {
            public void run() {
                try {
                    for(VoiceChannel vc : DiscordBot.INSTANCE.shardManager.getVoiceChannels()){
                        List<Member> m = vc.getMembers();
                        for(Member member : m){
                            
                            GuildVoiceState vstate = member.getVoiceState();
                            if(!(vstate.isDeafened() || vstate.isMuted())) {
                                ResultSet set = SQL.onQuery("SELECT * FROM channeltime WHERE idLong='"+ member.getIdLong() + "' AND server='" + member.getGuild().getIdLong() + "'");
                                if(set.next()){
                                    Long time = set.getLong("timeinmin");
                                    time++;
                                    SQL.onUpdate("UPDATE channeltime SET timeinmin=" + time + " WHERE name = '" + member.getUser().getName() + "' AND " + " server='" + member.getGuild().getIdLong() + "'");
                                }else{
                                    SQL.onUpdate("INSERT INTO channeltime VALUES(" + "'" + member.getUser().getName()+ "'" + " , '" + member.getIdLong() + "' , '0' , '" + member.getGuild().getIdLong() + "')");
                                }
                            }else{

                            }
                        }
                    }

                }catch(Exception ex) {
                    ex.printStackTrace();
                    SQL.lostConnection();
                }
            }
        }, 1, 60, TimeUnit.SECONDS);
        
    }
    
}