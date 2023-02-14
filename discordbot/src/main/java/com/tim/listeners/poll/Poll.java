package com.tim.listeners.poll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tim.DiscordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Poll extends ListenerAdapter{  

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    List<Emoji> possbileEmojis = new ArrayList<>();
    LocalDateTime date;
    TextChannel textChannel;
    
    public Poll(Member m, TextChannel channel, Message message) {
        
        textChannel = channel;

        if(m.getUser().isBot()) return;
        if(!m.isOwner()){ DiscordBot.embedsender("The owner is the only one who is permitted to do that!", channel); return; }

        String[] args = message.getContentDisplay().split("<>");
        Emoji[] reactions = { Emoji.fromUnicode("1️⃣"), Emoji.fromUnicode("2️⃣"), Emoji.fromUnicode("3️⃣"), Emoji.fromUnicode("4️⃣"), Emoji.fromUnicode("5️⃣"), Emoji.fromUnicode("6️⃣"), Emoji.fromUnicode("7️⃣"), Emoji.fromUnicode("8️⃣"),Emoji.fromUnicode("9️⃣")};
        String text = "";

        try{
            date = LocalDateTime.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }catch(Exception e){
            e.printStackTrace();
            EmbedBuilder builder = new EmbedBuilder()
                .setColor(DiscordBot.color)
                .setTitle("Poll")
                .setDescription("**How to use Poll:** \n &poll <>yyyy-MM-dd HH:mm<> 1 Option <> 2 Option <> 3 Option \n You can add up to 9 options! \n In the first field you have to put the deadline!");
            channel.sendMessageEmbeds(builder.build()).queue();
        }

        for(int i = 3; i < args.length;i++){
            text += reactions[i - 3].getAsReactionCode() + ": ";
            possbileEmojis.add(reactions[i - 3]);
            text += args[i];
            text += "\n";
        }

        if(args.length >= 4 && args.length <= 10){
            EmbedBuilder builder = new EmbedBuilder()
                .setColor(DiscordBot.color)
                .setTitle(args[2])
                .setDescription(text)
                .appendDescription("\n" + "**You can vote until: " + date.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "**");
            channel.sendMessageEmbeds(builder.build()).queue(a -> addReactions(a, args, reactions));
        }else{ 
            EmbedBuilder builder = new EmbedBuilder()
                .setColor(DiscordBot.color)
                .setTitle("Poll")
                .setDescription("**How to use Poll:** \n &poll <>yyyy-MM-dd HH:mm<> 1 Option <> 2 Option <> 3 Option \n You can add up to 9 options! \n In the first field you have to put the deadline!");
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    public void addReactions(Message msg, String[] args, Emoji[] reactions){
        for(int i = 3; i < args.length; i++){
            msg.addReaction(reactions[i - 3]).queue(); 
        }
        startScheduler(msg);
    }

    public void startScheduler(Message msg){
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run(){
                if(LocalDateTime.now().isAfter(date)){
                    getResults(msg);
                    scheduler.shutdown();
                }
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    public void getResults(Message msg){
        try{
            List<Integer> list = new ArrayList<>();
            List<Integer> sortinglist = new ArrayList<>();
            Message message = DiscordBot.INSTANCE.shardManager.getGuildById(msg.getGuild().getIdLong()).getTextChannelById(msg.getChannel().getIdLong()).retrieveMessageById(msg.getIdLong()).complete();
            
            for(int i = 0;i < possbileEmojis.size();i++){
                list.add(i, message.getReaction(possbileEmojis.get(i)).getCount());
                sortinglist.add(i, message.getReaction(possbileEmojis.get(i)).getCount());
            }
            
            Collections.sort(sortinglist);
            Collections.reverse(sortinglist);
    
            if(sortinglist.get(0) == sortinglist.get(1)){
                EmbedBuilder builder = new EmbedBuilder()
                    .setColor(DiscordBot.color)
                    .setTitle("The vote is over but there is no clear winner! :c");
                msg.replyEmbeds(builder.build()).queue();
            }else{
                int index = 0;
                for(int i = 0; i < list.size();i++){
                    if(list.get(i) == sortinglist.get(0)){
                        index = i;
                    }
                }
                
                EmbedBuilder builder = new EmbedBuilder()
                    .setColor(DiscordBot.color)
                    .setTitle("The Vote is over!")
                    .setDescription("The winner is: " + possbileEmojis.get(index).getAsReactionCode());
                msg.replyEmbeds(builder.build()).queue();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
