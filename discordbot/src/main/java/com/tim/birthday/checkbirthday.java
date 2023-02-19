package com.tim.birthday;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tim.DiscordBot;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class checkbirthday {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public void startScheduler(){
        scheduler.scheduleAtFixedRate(
            new Runnable() {
                public void run(){
                    SQL.checkConnection();
                    System.out.println("now executing!");
                    try{

                        for(Guild guild : DiscordBot.INSTANCE.shardManager.getGuilds()){
                            ResultSet set = SQL.onQuery("SELECT idlong, date FROM birthday");
                            EmbedBuilder builder = new EmbedBuilder()
                                .setColor(DiscordBot.color)
                                .setTitle("Todays birthdays!");
                            String text = "";
                            int i = 0;
                            while(set.next()){
                                if(guild.getMemberById(set.getLong("idlong")) != null){
                                    LocalDate birth = LocalDate.parse(set.getString("date"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                    if(LocalDate.now().getDayOfYear() == birth.getDayOfYear()){
                                        int difference = LocalDateTime.now().getYear() - birth.getYear();
                                        text += "Its " + guild.getMemberById(set.getLong("idlong")).getAsMention() + " birthday! He/She is " + difference + " now! \n";
                                        i++;
                                    }
                                }
                            }
                            builder.setDescription(text);
                            if(i != 0){
                                guild.getDefaultChannel().asTextChannel().sendMessageEmbeds(builder.build()).queue();
                            }
                        }
                    }catch(SQLException e){
                        e.printStackTrace();
                    }

                }
        }, 86400 - LocalDateTime.now().toLocalTime().toSecondOfDay() , 86400, TimeUnit.SECONDS);
    }
}
