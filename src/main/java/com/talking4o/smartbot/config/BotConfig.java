package com.talking4o.smartbot.config;

import com.talking4o.smartbot.bot.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${api.access.key}")
    private String accessKey;

    @Value("${api.base.url}")
    private String baseUrl;

    @Bean
    public Bot bot(){
        return new Bot(token, accessKey, baseUrl);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot());
        return telegramBotsApi;
    }

}
