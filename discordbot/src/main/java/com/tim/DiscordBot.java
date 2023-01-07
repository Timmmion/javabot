package com.tim;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.tim.listeners.CommandListener;
import com.tim.listeners.EventListener;
import com.tim.music.PlayerManager;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


public class DiscordBot 
{   
    public static DiscordBot INSTANCE;
    public static String PREFIX = "&";


    private final Dotenv config;
    public ShardManager shardManager;
    private CommandManager cmdMan;

    public AudioPlayerManager audioPlayerManager;
    public PlayerManager playerManager;


    public DiscordBot() throws LoginException{
        INSTANCE = this;

        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        
        audioPlayerManager = new DefaultAudioPlayerManager();
        playerManager = new PlayerManager();
        cmdMan = new CommandManager();

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Hentai"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_PRESENCES,GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES);
        builder.addEventListeners(new CommandListener());
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);

        shardManager = builder.build();

        //Register Listeners
        shardManager.addEventListener(new EventListener());
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);


    }

    public Dotenv getconfig(){
        return config;
    }

    public CommandManager getCmdMan() {
        return cmdMan;
    }


    public static void main( String[] args )
    {
        try{
            DiscordBot bot = new DiscordBot();
        }catch (LoginException e){
            System.out.println("Error: Bot token is not valid!");
        }

    }
}
