package com.talking4o.smartbot.bot;

import com.talking4o.smartbot.api.ApiManager;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final String accessKey;
    private final String baseUrl;

    public Bot(String botToken, String accessKey, String baseUrl) {
        super(botToken);
        this.accessKey = accessKey;
        this.baseUrl = baseUrl;
    }

    @Override
    public String getBotUsername() {
        return "@talking4o_bot";
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            if (message.hasText()){
                try {
                    startChat(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void startChat(Message message) throws IOException {
        String text = message.getText();
        String user = message.getChat().getFirstName();
        Long chatId = message.getChatId();

        log.info("User [{}] message: {}",user,chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (text.equals("/start")){
            sendMessage.setText("Hey there, " + getBotUsername() + " here! 👋\nReady to chat? Ask me anything! 😊");
        }else {
            // chat with Ai
            ApiManager ai = new ApiManager(accessKey, baseUrl);
            String response = ai.getResponse(text);
            sendMessage.setText(response);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.info("Error sending message " + e);
        }

    }

}
