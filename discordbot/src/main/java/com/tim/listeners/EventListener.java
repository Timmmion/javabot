package com.tim.listeners;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter{
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getName();
        String channelMention = event.getChannel().getAsMention();

        String message = user.getAsTag() + " hat auf eine Nachricht mit " + emoji + " in " + channelMention + " reagiert!";
        event.getChannel().asNewsChannel().sendMessage(message);

    }
}