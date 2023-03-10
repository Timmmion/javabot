package com.tim.music;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tim.DiscordBot;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class MusicUtil {
    public static void updateChannel(TextChannel channel){
		if(SQL.checkConnection()){
			ResultSet set = SQL.onQuery("SELECT * FROM musicchannel WHERE guildid = " + channel.getGuild().getIdLong());

			try{
				if(set.next()){
					SQL.onUpdate("UPDATE musicchannel SET channelid = " + channel.getIdLong() + " WHERE guildid = " + channel.getGuild().getIdLong());
				}else{
					SQL.onUpdate("INSERT INTO musicchannel(guildid, channelid) VALUES(" + channel.getGuild().getIdLong() + "," + channel.getIdLong() + ")");
				}
			}catch(SQLException ex){
				SQL.checkConnection();
			}
		}else{
			DiscordBot.embedsender("**SQL ERROR!** Pleas message the owner of the bot :(", channel);
		}
    }

	public static void sendEmbed(long guildid, EmbedBuilder builder, boolean onlytext) {
		TextChannel channel;
		builder.setColor(DiscordBot.color);
		if((channel = getMusicChannel(guildid)) != null) {
            if(onlytext){
                channel.sendMessageEmbeds(builder.build()).queue();
            }else{
                channel.sendMessageEmbeds(builder.build()).queue();
            }
			
		}
	}


    public static TextChannel getMusicChannel(long guildid) {
		if(SQL.checkConnection()){
			ResultSet set = SQL.onQuery("SELECT * FROM musicchannel WHERE guildid = " + guildid);	
			try {
				if(set.next()) {
					long channelid = set.getLong("channelid");
					
					Guild guild;
					if((guild = DiscordBot.INSTANCE.shardManager.getGuildById(guildid)) != null) {
						TextChannel channel;
						if((channel = guild.getTextChannelById(channelid)) != null) {
							return channel;
						}
					}
				}
			} catch(SQLException ex) { }
			return null;
		}else{
			return null;
		}
	}
}
