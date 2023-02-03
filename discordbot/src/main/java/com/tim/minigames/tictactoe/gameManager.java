package com.tim.minigames.tictactoe;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tim.DiscordBot;
import com.tim.manage.SQL;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

public class gameManager extends ListenerAdapter{

    private Guild guild;
    private Member p1;
    private Member p2;
    final private Emoji whiteCheckMark = Emoji.fromUnicode("✅");
    private Emoji cricle = Emoji.fromUnicode("⏺");
    private Emoji square = Emoji.fromUnicode("⏹️");
    public List<Emoji> positions = new ArrayList<>();
    private ChannelAction<TextChannel> gameChannel;
    private TextChannel finalchannel;
    private Member activePlayer;
    Emoji whiteSquare = Emoji.fromUnicode("🟦");
    public boolean firstTime = true;
    public boolean gameStarted = false;
    public boolean notwon = true;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService gametime = Executors.newScheduledThreadPool(1);
    Role spec;

    public gameManager(Member member, TextChannel channel, Message message){

        guild = member.getGuild();
        p1 = member;
        Random ran = new Random();
        
        spec = guild.createRole().setName("spec" + ran.nextInt(123192312)).setColor(DiscordBot.color).setMentionable(false).complete();
        DiscordBot.addRemoveableRole(spec);
        gametime.scheduleAtFixedRate(
        new Runnable() {
            public void run() {
                end();
                scheduler.shutdown();
            }
        }, 5, 1, TimeUnit.MINUTES);
        try{
            p2 = message.getMentions().getMembers().get(0);
            if(p1 == p2) {DiscordBot.embedsender("Imagine no friends to play with xD", channel); return;}
            channel.sendMessage(p2.getAsMention() + " do you want to play TicTacToe with " + p1.getAsMention() + "?\n Everyone who wants to watch the game should also react to the checkmark! :)").queue(a -> {a.addReaction(whiteCheckMark).queue();});
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getMember() == p2 && !event.getUser().isBot() && !gameStarted){
            gameStarted = true;
            gameAccepted();
        }
        if(event.getMember() != p2 || event.getMember() != p1){
            event.getMember().getGuild().addRoleToMember(event.getMember(), spec).queue();
        }
    }

    public void gameAccepted(){

        EnumSet<Permission> permissiona = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
        EnumSet<Permission> permissionb = EnumSet.of(Permission.VIEW_CHANNEL);
        EnumSet<Permission> permissionc = EnumSet.of(Permission.MESSAGE_SEND);

        gameChannel = guild.createTextChannel("TicTacToe");
        gameChannel.addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, permissiona);
        gameChannel.addRolePermissionOverride(spec.getIdLong(), permissionb, permissionc);
        gameChannel.addMemberPermissionOverride(p1.getIdLong(), permissiona, null);
        gameChannel.addMemberPermissionOverride(p2.getIdLong(), permissiona, null);

        setupBoard();
    }

    public void setupBoard(){
        
        for(int i = 0; i < 9;i++){
            positions.add(whiteSquare);
        }
        sendBoard();
    }

    public void sendBoard(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(DiscordBot.color);
        builder.setTitle("TicTacToe");
        String text = "";
        for(int i = 0;i < positions.size();i++){
            if(i % 3 == 0 && i != 0){
                text += "\n";
            }
            text += positions.get(i).getAsReactionCode();
        }
        builder.setDescription(text);
        if(firstTime){
            EmbedBuilder explain = new EmbedBuilder();
            explain.setTitle("TicTacToe explanation!");
            explain.setDescription("If you want to chose a field to place your Symbol \n you have the write to number of the field");
            //explain.addField("Example:", "1 2 3\n4 5 6  ->\n7 8 9", true);
            explain.addField("Example:",":one::two::three:\n:four::five::six:\n:seven::eight::nine:",true);
            explain.setColor(DiscordBot.color);
            

            finalchannel = gameChannel.complete();
            finalchannel.sendMessageEmbeds( explain.build()).queue();
            Random rand = new Random();
            if(rand.nextInt(0, 50) > 25){
                activePlayer = p1;
                DiscordBot.embedsender(p1.getUser().getName() + " starts!", finalchannel);
            }else{
                activePlayer = p2;
                DiscordBot.embedsender(p2.getUser().getName() + " starts!", finalchannel);
            }
            DiscordBot.addRemoveableChannel(finalchannel);
            finalchannel.sendMessageEmbeds(builder.build()).queue();
            firstTime = false;
        }else{
            finalchannel.sendMessageEmbeds(builder.build()).queue();
        }


    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(notwon && event.getChannel() == finalchannel){
            String message = event.getMessage().getContentDisplay();
            if(event.isFromType(ChannelType.TEXT)){
                if(message.length() == 1){
                    try{
                        int number = Integer.parseInt(message);
                        changeBoard(number, event.getMember());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void changeBoard(int number, Member m){
        if(m == p1 && m == activePlayer){
            if(positions.get(number - 1).getAsReactionCode() == whiteSquare.getAsReactionCode()){
                positions.set(number - 1, cricle);
                checkForWin();
                if(!positions.contains(whiteSquare)){
                    if(notwon){
                        DiscordBot.embedsender("Tie!", finalchannel);
                        addTies();
                        endgame();
                    }
                    notwon = false;
                }
                sendBoard();
                if(notwon){
                    DiscordBot.embedsender(p2.getUser().getName() + " your turn!", finalchannel);
                }
                activePlayer = p2;
            }else{
                DiscordBot.embedsender("The field is already occupied! Choose another field!", finalchannel);
            }
        }

        if(m == p2 && m == activePlayer){
            if(positions.get(number -1 ).getAsReactionCode() == whiteSquare.getAsReactionCode()){
                positions.set(number- 1, square);
                checkForWin();
                if(!positions.contains(whiteSquare)){
                    if(notwon){
                        DiscordBot.embedsender("Tie!", finalchannel);
                        addTies();
                        endgame();
                    }
                    notwon = false;
                    
                }
                sendBoard();
                if(notwon){
                    DiscordBot.embedsender(p1.getUser().getName() + " your turn!", finalchannel);
                }
                activePlayer = p1;
            }else{
                DiscordBot.embedsender("The field is already occupied! Choose another field!", finalchannel);
            }
        }

    }

    public void checkForWin(){
        if(positions.get(0) == square && positions.get(1) == square && positions.get(2) == square){
            p2Win();
            return;
        }
        if(positions.get(3) == square && positions.get(4) == square && positions.get(5) == square){
            p2Win();
            return;
        }
        if(positions.get(6) == square && positions.get(7) == square && positions.get(8) == square){
            p2Win();
            return;
        }
        if(positions.get(0) == square && positions.get(3) == square && positions.get(6) == square){
            p2Win();
            return;
        }
        if(positions.get(1) == square && positions.get(4) == square && positions.get(7) == square){
            p2Win();
            return;
        }
        if(positions.get(2) == square && positions.get(5) == square && positions.get(8) == square){
            p2Win();
            return;
        }
        if(positions.get(0) == square && positions.get(4) == square && positions.get(8) == square){
            p2Win();
            return;
        }
        if(positions.get(6) == square && positions.get(4) == square && positions.get(2) == square){
            p2Win();
            return;
        }


        if(positions.get(0) == cricle && positions.get(1) == cricle && positions.get(2) == cricle){
            p1Win();
            return;
        }
        if(positions.get(3) == cricle && positions.get(4) == cricle && positions.get(5) == cricle){
            p1Win();
            return;
        }
        if(positions.get(6) == cricle && positions.get(7) == cricle && positions.get(8) == cricle){
            p1Win();
            return;
        }
        if(positions.get(0) == cricle && positions.get(3) == cricle && positions.get(6) == cricle){
            p1Win();
            return;
        }
        if(positions.get(1) == cricle && positions.get(4) == cricle && positions.get(7) == cricle){
            p1Win();
            return;
        }
        if(positions.get(2) == cricle && positions.get(5) == cricle && positions.get(8) == cricle){
            p1Win();
            return;
        }
        if(positions.get(0) == cricle && positions.get(4) == cricle && positions.get(8) == cricle){
            p1Win();
            return;
        }
        if(positions.get(6) == cricle && positions.get(4) == cricle && positions.get(2) == cricle){
            p1Win();
            return;
        }
    }

    public void p2Win(){
        notwon = false;
        DiscordBot.embedsender(p2.getUser().getName() + " won!", finalchannel);
        if(SQL.checkConnection()){
            try{
                ResultSet set = SQL.onQuery("SELECT * FROM tttstats WHERE idLong='" + p2.getIdLong() + "'");
                if(set.next()){
                    Long wins = set.getLong("wins");
                    wins++;
                    SQL.onUpdate("UPDATE tttstats SET wins='" + wins + "' WHERE idlong= '" + p2.getIdLong() + "'");
                }else{
                    SQL.onUpdate("INSERT INTO tttstats VALUES(" + "'" +  p2.getUser().getName()+ "'" + " , " + "'" + p2.getIdLong() + "' , '1' , '0', '0')");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                ResultSet set = SQL.onQuery("SELECT * FROM tttstats WHERE idLong='" + p1.getIdLong() + "'");
                if(set.next()){
                    Long loses = set.getLong("loses");
                    loses++;
                    SQL.onUpdate("UPDATE tttstats SET loses='" + loses + "' WHERE idlong= '" + p1.getIdLong() + "'");
                }else{
                    SQL.onUpdate("INSERT INTO tttstats VALUES(" + "'" +  p1.getUser().getName()+ "'" + " , " + "'" + p1.getIdLong() + "' , '0' , '1', '0')");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            DiscordBot.embedsender("**SQL ERROR!** Pleas message the owner of the bot :(", finalchannel);
        }
        endgame();
    }

    public void p1Win(){
        notwon = false;
        DiscordBot.embedsender(p1.getUser().getName() + " won!", finalchannel);
        if(SQL.checkConnection()){
            try{
                ResultSet set = SQL.onQuery("SELECT * FROM tttstats WHERE idLong='" + p1.getIdLong() + "'");
                if(set.next()){
                    Long wins = set.getLong("wins");
                    wins++;
                    SQL.onUpdate("UPDATE tttstats SET wins='" + wins + "' WHERE idlong= '" + p1.getIdLong() + "'");
                }else{
                    SQL.onUpdate("INSERT INTO tttstats VALUES(" + "'" +  p1.getUser().getName() + "'" + " , '" + p1.getIdLong() + "' , '1' , '0', '0')");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                ResultSet set = SQL.onQuery("SELECT * FROM tttstats WHERE idLong='" + p2.getIdLong() + "'");
                if(set.next()){
                    Long loses = set.getLong("loses");
                    loses++;
                    SQL.onUpdate("UPDATE tttstats SET loses='" + loses + "' WHERE idlong= '" + p2.getIdLong() + "'");
                }else{
                    SQL.onUpdate("INSERT INTO tttstats VALUES(" + "'" +  p2.getUser().getName()+ "'" + " , " + "'" + p2.getIdLong() + "' , '0' , '1', '0')");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            DiscordBot.embedsender("**SQL ERROR!** Pleas message the owner of the bot :(", finalchannel);
        }
        endgame();
    }
    
    public void endgame(){
        scheduler.scheduleAtFixedRate(
        new Runnable() {
            public void run() {
                end();
                scheduler.shutdown();
            }
        }, 5, 60, TimeUnit.SECONDS);
    }

    public void end(){
        DiscordBot.removeRemoveableRole(spec);
        DiscordBot.removeListener(this, DiscordBot.INSTANCE.shardManager);
        DiscordBot.removeRemoveableChannel(finalchannel);
        finalchannel.delete().queue();
    }

    public void addTies(){
        if(SQL.checkConnection()){
            try{
                ResultSet set = SQL.onQuery("SELECT * FROM tttstats WHERE idLong='" + p1.getIdLong() + "'");
                if(set.next()){
                    Long ties = set.getLong("ties");
                    ties++;
                    SQL.onUpdate("UPDATE tttstats SET ties='" + ties + "' WHERE idlong= '" + p1.getIdLong() + "'");
                }else{
                    SQL.onUpdate("INSERT INTO tttstats VALUES('" + p1.getUser().getName() + "' , '" + p1.getIdLong() + "' , '0' , '0', '1')");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                ResultSet set = SQL.onQuery("SELECT * FROM tttstats WHERE idLong='" + p2.getIdLong() + "'");
                if(set.next()){
                    Long ties = set.getLong("ties");
                    ties++;
                    SQL.onUpdate("UPDATE tttstats SET ties='" + ties + "' WHERE idlong= '" + p2.getIdLong() + "'");
                }else{
                    SQL.onUpdate("INSERT INTO tttstats VALUES('" + p2.getUser().getName() + "' , '" + p2.getIdLong() + "' , '0' , '0', '1')");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            DiscordBot.embedsender("**SQL ERROR!** Pleas message the owner of the bot :(", finalchannel);
        }
    }
}
