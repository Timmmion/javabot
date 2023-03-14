package com.tim;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.tim.birthday.checkbirthday;
import com.tim.listeners.CommandListener;
import com.tim.manage.SQL;
import com.tim.manage.SQLManager;
import com.tim.music.PlayerManager;
import com.tim.music.SpotifyInterpreter;
//import com.tim.scheduler.activity.ActivityChanger;
import com.tim.scheduler.channeltimer.timeManager;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
    public static Color color = Color.decode("#9309dd");

    public final Dotenv config;
    public ShardManager shardManager;
    private CommandManager cmdMan;

    public AudioPlayerManager audioPlayerManager;
    public PlayerManager playerManager;
    public SpotifyInterpreter spotifyInterpreter;

    public static List<ListenerAdapter> listeners = new ArrayList<>();
    public static List<TextChannel> textchannels = new ArrayList<>();
    public static List<Role> roles = new ArrayList<>();

    public static void main(String[] args) { try{ DiscordBot bot = new DiscordBot(); if(bot.equals(bot)){ }} catch (LoginException e){ System.out.println("Error: Bot token is not valid!"); }}

    //BOT CONFIG AND SETUP
    public DiscordBot() throws LoginException{

        INSTANCE = this;
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        
        SQL.connect();
        SQLManager.onCreate();

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        shardManager = createBuilder(builder);

        registerManager();
        registerListeners();

        System.out.println("BOT ONLINE!");
    }

    public void registerListeners(){
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);

        timeManager timeManager = new timeManager();
        timeManager.startScheduleTask();

        /*ActivityChanger activityChanger = new ActivityChanger();
        activityChanger.startScheduler();*/

        checkbirthday checkbirthday = new checkbirthday();
        checkbirthday.startScheduler();
    }

    public void registerManager(){
        audioPlayerManager = new DefaultAudioPlayerManager();
        playerManager = new PlayerManager();
        cmdMan = new CommandManager();
        spotifyInterpreter = new SpotifyInterpreter();
    }

    public static void embedsender(String title ,TextChannel channel){
        //channel.sendMessage(title).queue();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setColor(color);
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    public ShardManager createBuilder(DefaultShardManagerBuilder builder){
        
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Hentai"));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES ,GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_PRESENCES,GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES,GatewayIntent.GUILD_VOICE_STATES);
        builder.addEventListeners(new CommandListener());
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);

        return builder.build();
    }

    public static void registerListener(ListenerAdapter a, ShardManager sh){
        sh.addEventListener(a);
        listeners.add(a);
    }

    public static void removeListener(ListenerAdapter a, ShardManager sh){
        sh.removeEventListener(a);
    }

    public static void addRemoveableChannel(TextChannel channel){
        textchannels.add(channel);
    }

    public static void removeRemoveableChannel(TextChannel channel){
        textchannels.remove(channel);
    }   

    public static void addRemoveableRole(Role role){
        roles.add(role);
    }

    public static void removeRemoveableRole(Role role){
        roles.remove(role);
    } 

    public Dotenv getconfig(){
        return config;
    }

    public CommandManager getCmdMan() {
        return cmdMan;
    }
}
