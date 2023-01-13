package com.tim.minigames.sv;

import java.sql.Array;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

public class SVManager extends ListenerAdapter{

    private Guild guild = null;
    private Member player = null;
    private Member enemy = null;
    private Emoji whiteCheckMark = Emoji.fromUnicode("✅");
    private boolean gameStartedYet = false;
    private List<Emoji> playerFieldList = new ArrayList<>();
    private List<Emoji> enemyFieldList = new ArrayList<>();
    public boolean isRunning = false;
    ChannelAction<TextChannel> textenemy;
    ChannelAction<TextChannel> textPlayer1;
    List<Integer> availableShipsplayer = List.of(5,4,4,3,3,3,2,2,2,2);
    List<Integer> availableShipsenemy = List.of(5,4,4,3,3,3,2,2,2,2);
    Category category;

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getMember().getUser() == enemy.getUser() && !event.getUser().isBot() && !gameStartedYet){
            gameStartedYet = true;
            gameAccepted();
        }
        return;
    }

    public void initiateGame(Member m, TextChannel channel, Message message){

        guild = m.getGuild();
        player = m;
        try{
            enemy = message.getMentions().getMembers().get(0);
            /*if(enemy == player){
                channel.sendMessage("You can't play with yourself :(").queue();;
                return;
            }*/
            isRunning = true;
            channel.sendMessage(enemy.getAsMention() + " do you want to accept the request and play a game with " + player.getAsMention() + "?").queue(a -> {a.addReaction(whiteCheckMark).queue();});
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void gameAccepted(){
        category = guild.createCategory("SV").complete();
        
        EnumSet<Permission> permission = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);

        textPlayer1 = category.createTextChannel("sv");
        textenemy = category.createTextChannel("sv");

        textPlayer1.addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, permission);
        textPlayer1.addMemberPermissionOverride(player.getIdLong(), permission, null);

        textenemy.addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, permission);
        textenemy.addMemberPermissionOverride(enemy.getIdLong(), permission, null);

        playerFieldList = createListWithFieldsPlayer(playerFieldList);
        enemyFieldList = createListWithFieldsPlayer(enemyFieldList);

        sendMessage(playerFieldList, textPlayer1);
        sendMessage(enemyFieldList, textenemy);
        
    }

    public void placeShip(Member m, TextChannel channel, Message message){
        if(m.getUser() == player.getUser()){

            String[] args = message.getContentDisplay().split(" ");

            if(args.length - 2 == Integer.parseInt(args[1]) && Integer.parseInt(args[1]) - 2 != 1){

                int shipNumber = Integer.parseInt(args[1]);


                if(availableShipsplayer.contains(shipNumber)){

                    List<String> shipPosition = new ArrayList<>();

                    for(int i = 0;i < shipNumber - 1;i++){
                        shipPosition.add(args[-i]);
                        
                    }
                    for(int i = 0;i < shipPosition.size();i++){
                        System.out.println(shipPosition.get(i));
                    }
                }
            }
        }
    }


    public void endGame(){
        category.delete().queue();
        textPlayer1.complete().delete().queue();
        textenemy.complete().delete().queue();
    }

    public void sendMessage(List<Emoji> list, ChannelAction<TextChannel> channel){
        String text = "";
        for(int i = 0;i < list.size();i++){
            if(i % 11 == 0 && i != 0){
                text += "\n";
            }
            text +=list.get(i).getAsReactionCode();

        }

        channel.complete().sendMessage(text).queue();
    }

    public List<Emoji> createListWithFieldsPlayer(List<Emoji> playerFieldList){

        for(int i = 0;i < 121;i++){
            Emoji blueSquare = Emoji.fromUnicode("🟦");
            playerFieldList.add(blueSquare);
        }
        playerFieldList.set(1, Emoji.fromUnicode("1️⃣"));
        playerFieldList.set(2, Emoji.fromUnicode("2️⃣"));
        playerFieldList.set(3, Emoji.fromUnicode("3️⃣"));
        playerFieldList.set(4, Emoji.fromUnicode("4️⃣"));
        playerFieldList.set(5, Emoji.fromUnicode("5️⃣"));
        playerFieldList.set(6, Emoji.fromUnicode("6️⃣"));
        playerFieldList.set(7, Emoji.fromUnicode("7️⃣"));
        playerFieldList.set(8, Emoji.fromUnicode("8️⃣"));
        playerFieldList.set(9, Emoji.fromUnicode("9️⃣"));
        playerFieldList.set(10, Emoji.fromUnicode("🔟"));
        playerFieldList.set(11, Emoji.fromUnicode("🇦"));
        playerFieldList.set(22, Emoji.fromUnicode("🇧"));
        playerFieldList.set(33, Emoji.fromUnicode("🇨"));
        playerFieldList.set(44, Emoji.fromUnicode("🇩"));
        playerFieldList.set(55, Emoji.fromUnicode("🇪"));
        playerFieldList.set(66, Emoji.fromUnicode("🇫"));
        playerFieldList.set(77, Emoji.fromUnicode("🇬"));
        playerFieldList.set(88, Emoji.fromUnicode("🇭"));
        playerFieldList.set(99, Emoji.fromUnicode("🇮"));
        playerFieldList.set(110, Emoji.fromUnicode("🇯"));

        return playerFieldList;
    }
}
