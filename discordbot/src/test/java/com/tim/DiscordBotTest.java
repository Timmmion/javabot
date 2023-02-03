package com.tim;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;


public class DiscordBotTest
{   
    private final Dotenv config;
    private final ShardManager shardManager;


    public DiscordBotTest() throws LoginException{
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setActivity(Activity.listening("Spotify"));
        builder.setStatus(OnlineStatus.ONLINE);
        shardManager = builder.build();
    }


    public Dotenv getconfig(){
        return config;
    }


    public ShardManager getShardManager(){
        return shardManager;
    }


    public static void main( String[] args )
    {
        try{
            DiscordBot bot = new DiscordBot();
            if(bot.equals(bot)){
                
            }
        }catch (LoginException e){
            System.out.println("Error: Bot token is not valid!");
        }

    }
}
