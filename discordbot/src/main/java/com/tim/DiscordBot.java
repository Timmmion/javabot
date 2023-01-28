package com.tim;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.tim.listeners.CommandListener;
import com.tim.manage.SQL;
import com.tim.manage.SQLManager;
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


    public final Dotenv config;
    public ShardManager shardManager;
    private CommandManager cmdMan;

    public AudioPlayerManager audioPlayerManager;
    public PlayerManager playerManager;


    public DiscordBot() throws LoginException{
        INSTANCE = this;

        config = Dotenv.configure().load();

        SQL.connect();
        SQLManager.onCreate();

        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        
        audioPlayerManager = new DefaultAudioPlayerManager();
        playerManager = new PlayerManager();
        cmdMan = new CommandManager();

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Hentai"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_PRESENCES,GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES,GatewayIntent.GUILD_VOICE_STATES);
        builder.addEventListeners(new CommandListener());
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);

        shardManager = builder.build();
        System.out.println("BOT ONLINE!");

        //Register Listeners
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
            if(bot.equals(bot)){
                
            }
        }catch (LoginException e){
            System.out.println("Error: Bot token is not valid!");
        }

    }
}
