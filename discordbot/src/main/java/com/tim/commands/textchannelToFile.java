package com.tim.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.tim.DiscordBot;
import com.tim.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

public class textchannelToFile implements ServerCommand {


    @Override
    public void perfomCommand(Member m, TextChannel channel, Message message) {

        if(m.getIdLong() != Long.parseLong(DiscordBot.INSTANCE.config.get("OWNERIDLONG"))){
            DiscordBot.embedsender("Only the BOT-Owner can use this command!", channel);
            return;
        }

        try{
                        
            List<Message> history = new ArrayList<>();
            history.addAll(channel.getHistoryBefore(message, 100).complete().getRetrievedHistory());

            String name = "Histroy_" + channel.getName().toString().toUpperCase() + "_"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".txt";
            FileWriter file = new FileWriter(name);
            file.write("||---Message Logger by Timmmion!---|| \n \n");
            
            for(Message msg : history){
                String line = msg.getContentDisplay();
                file.write(message.getAuthor().getName() + " \n");
                file.write(line);
                if(!line.isBlank()) {
                    file.write("\n \n");
                }else{
                    file.write("\n");
                }
            }
    
            file.close();
            File finalFile = new File(name);
            FileUpload fileUpload = FileUpload.fromData(finalFile, "Log.txt");

            channel.sendFiles(fileUpload).queue();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public List<Message> createHistory(TextChannel tc, Message latestMessage){
        return tc.getHistoryAfter(latestMessage, 1).complete().getRetrievedHistory();
        
    }
}
