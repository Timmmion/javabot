package com.tim.channeltimer.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class stats implements ServerCommand{
    
    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {
        Member target;
        String prefix = DiscordBot.PREFIX;
        EmbedBuilder builder = new EmbedBuilder();

        try{
            target = message.getMentions().getMembers().get(0);
            builder.setTitle(target.getUser().getName() + "s stats!");
            builder.setColor(DiscordBot.color);

            if(SQL.checkConnection()){

                
                ResultSet setServer = SQL.onQuery("SELECT idlong, timeinmin FROM channeltime WHERE server='" + m.getGuild().getIdLong() + "' ORDER BY timeinmin DESC");
                List<Long> timelist = new ArrayList<>();
                List<Long> pointsid = new ArrayList<>();
                List<Long> pointstime = new ArrayList<>();
                int rank = 0;
                try {
                    //LEADERBOARD STATS
                    while(setServer.next()){
                        rank++;
                        long id = setServer.getLong("idlong");
                        long time = setServer.getLong("timeinmin"); 
                        long finaltime = 0;
                        if(id == target.getIdLong()){
                            setServer.close();
                            
                            //GET GLOBAL STATS
                            ResultSet setGlobal = SQL.onQuery("SELECT timeinmin FROM channeltime WHERE idLong='" + target.getIdLong() + "' ORDER BY timeinmin");
                            while(setGlobal.next()){
                                long ctime = setGlobal.getLong("timeinmin");
                                timelist.add(ctime);
                            }
                            for(int i = 0;i < timelist.size();i++){
                                finaltime = finaltime + timelist.get(i);
                            }
                            setGlobal.close();

                            //GET GLOBAL RANK
                            ResultSet setGlobalRank = SQL.onQuery("SELECT idlong, timeinmin FROM channeltime");

                            int a = 0;
                            
                            while(setGlobalRank.next()){
                                long idRank = setGlobalRank.getLong("idlong");
                                long timeInMin = setGlobalRank.getLong("timeinmin"); 
                                if(pointsid.contains( (long) a)){
                                    pointstime.add(pointstime.get(a) + timeInMin);
                                }else{
                                    pointsid.add(idRank);
                                    pointstime.add(timeInMin);
                                }
                                a++;
                            }

                            

                            int rankglobal = 0;
                            
                            for(int i = 0; i < pointsid.size();i++){
                                if(pointsid.get(i) == target.getIdLong()){
                                    long ptime = pointstime.get(i);
                                    Collections.sort(pointstime);
                                    Collections.reverse(pointstime);
                                    for(int u = 0;u < pointstime.size();u++){
                                        if(ptime == pointstime.get(u)){
                                            rankglobal = u + 1;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }

                            setGlobalRank.close();
                            builder.setDescription("Time spent on this server: " + time + " Rank: " + rank + "\n Global time spent in a VC: " + finaltime + " Rank: " + rankglobal);
                            break;
                        }
                    }

                    //TICTACTOE STATS

                    try {
                        ResultSet set = SQL.onQuery("SELECT wins, loses, ties FROM tttstats WHERE idlong='" + target.getIdLong() + "'");
                        while(set.next()){
                            long win = set.getLong("wins"); 
                            long loses = set.getLong("loses"); 
                            long tie = set.getLong("ties"); 
                            builder.addField("TicTacToe:", "Wins: " + Long.toString(win, 0) + "\nLoses: " + Long.toString(loses, 0) + "\nTies: " +  Long.toString(tie, 0),false);
                        }

                        set.close();
                    } catch (Exception e) { e.printStackTrace(); DiscordBot.embedsender("No TICTACTOE information about this user!", channel); }

                    channel.sendMessageEmbeds(builder.build()).queue();

                } catch (SQLException e) { e.printStackTrace(); } 

            }else{
                DiscordBot.embedsender("**SQL ERROR!** Pleas message the owner of the bot :(", channel);
            }
        }catch(Exception e){
            DiscordBot.embedsender("Use " + prefix + "stats @user!", channel);
            e.printStackTrace();
        }
    }

}
