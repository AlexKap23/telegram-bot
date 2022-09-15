package com.alexandros.teleram.bot.telegram.bot;

import com.alexandros.teleram.bot.kafka.MqttConsumerToKafkaProducer;
import com.alexandros.teleram.bot.kafka.TelegramBotKafkaConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AlexKapBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    public String botToken;

    @Value("${bot.username}")
    public String botUserName;

    @Autowired
    private MqttConsumerToKafkaProducer mqttConsumerToKafkaProducer;

    @Autowired
    private TelegramBotKafkaConsumer kafkaConsumer;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    @PostConstruct
    public void init(){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();
        sendMessageToUser(update.getMessage().getChatId(), command);
        try {
            if ("kafka".equalsIgnoreCase(command)) {
                mqttConsumerToKafkaProducer.transferMessages();
//				kafkaConsumer.consumeKafkaMessages();
            }
//			if(StringUtils.isEmpty(message) || StringUtils.isBlank(message)) return;
//			sendMessageToUser(update.getMessage().getChatId(), message);
        } catch (Exception e) {
            logger.error("Exception caught while update received", e.getCause());
        }
    }

    public void sendMessageToUser(Long chatId, String text) {
        SendMessage message = SendMessage.builder().chatId(chatId).text(text).build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return this.botUserName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }
}
