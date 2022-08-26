package com.alexandros.teleram.bot.telegram.bot;

import com.alexandros.teleram.bot.services.CommandProcessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AlexKapBot extends TelegramLongPollingBot {

	@Value("${bot.token}")
	private String botToken;

	@Value("${bot.username}")
	private String botUserName;
	private final CommandProcessService commandProcessService;

	Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

	public AlexKapBot(CommandProcessService commandProcessService) {
		this.commandProcessService = commandProcessService;
	}

	@PostConstruct
	public void registerBot() {
		TelegramBotsApi botsApi = new TelegramBotsApi();
		try {
			botsApi.registerBot(this);
			logger.info("TelegramBotService.afterPropertiesSet:registerBot finish");
		} catch (TelegramApiException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void onUpdateReceived(Update update) {
		String command = update.getMessage().getText();
		try {
			String message = commandProcessService.executeCommand(command);
			if(StringUtils.isEmpty(message) || StringUtils.isBlank(message)) return;
			sendMessageToUser(update.getMessage().getChatId(), message);
		} catch (Exception e) {
			logger.error("Exception caught while update received", e.getCause());
		}
	}

	public void sendMessageToUser(Long chatId, String text) {
		SendMessage message = new SendMessage()
			.setChatId(chatId)
			.setText(text);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			logger.error(e.getMessage());
		}
	}

	private Date formatStringDate(String myDate) {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		Date rememberDate = null;
		try {
			logger.debug("DEBUG - Parsing remember date");
			rememberDate = formater.parse(myDate);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return rememberDate;
	}


	//@Scheduled(cron = "0 0 10 ? * *")
	@Scheduled(cron = "0 54 13 ? * *")
	private void fetchIdea() {
		logger.debug("DEBUG - Fetching ideas");
		/*List<Idea> todaysThings = ideaRepository.fetchByRememberDate(new Date());
		for (Idea idea : todaysThings) {
			sendMessageToUser(idea.getChatId(),
			                  "ID:".concat(String.valueOf(idea.getId())).concat(" message is: " + idea.getMessage()));
			logger.debug("DEBUG - Sending idea now");
		}*/

	}

	public String getBotUsername() {
		return botUserName;
	}

	public String getBotToken() {
		return botToken;
	}

	public String getBotPath() {
		return null;
	}

}
