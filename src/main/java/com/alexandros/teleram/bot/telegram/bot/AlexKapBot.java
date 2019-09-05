package com.alexandros.teleram.bot.telegram.bot;

import com.alexandros.teleram.bot.model.Idea;
import com.alexandros.teleram.bot.repositories.IdeaRepository;
import com.alexandros.teleram.bot.services.IdeaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

@Controller
public class AlexKapBot extends TelegramLongPollingBot {
	private final IdeaService ideaService;
	private final IdeaRepository ideaRepository;

	Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

	public AlexKapBot(IdeaService ideaService, IdeaRepository ideaRepository) {
		this.ideaService = ideaService;
		this.ideaRepository = ideaRepository;
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
		int length = update.getMessage().getText().length();
		try {
			if (command.startsWith("/addidea")) {
				// /addidea message date
				logger.debug("DEBUG - Handling /addidea command");
				String idea = update.getMessage().getText().substring(8, length - 10);
				String rememberDate = update.getMessage().getText().substring(length - 10, length);
				addIdea(idea, update.getMessage().getFrom().getId(), update.getMessage().getDate(), rememberDate,
				        update.getMessage().getChatId());
			} else if (command.startsWith("/delete")) {
				// /delete <id,id id/id>
				logger.debug("DEBUG - handling /delete command");
				String deleteCommand = update.getMessage().getText().substring(8, length);
				sendMessageToUser(update.getMessage().getChatId(), deleteIdeas(deleteCommand));
			} else if (command.startsWith("/updateIdea")) {
				// /updateIdea <id> <param,param,..> <value,value,value>
				logger.debug("DEBUG - handling /updateIdea command");
				String updateCommand = update.getMessage().getText().substring(12, length);
				sendMessageToUser(update.getMessage().getChatId(), updateIdea(updateCommand));
			} else if (command.startsWith("/allIdeas")) {
				// /allIdeas
				List<Idea> todays = ideaRepository.fetchTodaysIdeas(new Date());
				logger.debug("DEBUG - handling /allIdeas command");
				for (Idea idea : todays) {
					sendMessageToUser(idea.getChatId(),
					                  "ID:".concat(String.valueOf(idea.getId()))
						                  .concat(" message is: " + idea.getMessage()));
				}
			}
		} catch (Exception e) {
			logger.error("Exception caught while update received", e.getCause());
		}
	}

	private void sendMessageToUser(Long chatId, String text) {
		SendMessage message = new SendMessage()
			.setChatId(chatId)
			.setText(text);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			logger.error(e.getMessage());
		}
	}

	private Date formatDate(int unixDate) {
		try {
			Date date = new Date((long) unixDate * 1000);
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
			String dateAsString = formater.format(date);
			logger.debug("DEBUG - Formatting date " + formater.parse(dateAsString).toString());
			return formater.parse(dateAsString);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Error parsing date");
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

	private void addIdea(String message, int user, int date, String rememberDate, Long chatId) {
		Idea idea = new Idea();
		idea.setMessage(message);
		idea.setUserId(user);
		logger.debug("DEBUG - adding an idea to db");
		idea.setSubmitDate(formatDate(date));
		idea.setRememberDate(formatStringDate(rememberDate));
		idea.setChatId(chatId);
		ideaService.save(idea);
		logger.info("Idea saved");
	}

	private String deleteIdeas(String deleteCommand) {
		logger.debug("DEBUG - deleteIdeas method. ");
		String[] ideas = deleteCommand.split("[, .+|/]");
		String message = null;
		try {
			if (ideas.length > 1) {
				message = "All selected ideas deleted";
			} else if (ideas.length == 1) {
				message = "Idea deleted";
			}
			for (String i : ideas) {
				ideaService.deleteById(Long.valueOf(i));
			}
		} catch (Exception e) {
			message = "ERROR";
			logger.error("Error while deleting ideas.", e.getCause());
		}

		return message;
	}

	private String updateIdea(String updateCommand) {
		logger.debug("DEBUG - updateIdeas method. ");
		String[] updateCommandParts = updateCommand.split(" ");
		String[] params = updateCommandParts[1].split(",");
		String[] values = updateCommandParts[2].split(",");
		String message = null;
		try {
			logger.info("DEBUG - updateIdeas method. ");
			if (updateCommandParts.length > 3) {
				message = "ERROR - Possible format error. Please use correct format - Aborting update";
			} else {
				for (int i = 0; i < params.length; i++) {
					long id = Long.valueOf(updateCommandParts[0]);
					Idea ideaToUpdate = ideaService.findById(id);
					if (values[i] != ideaService.getPropertyValue(ideaToUpdate, params[i])) {
						ideaService.setPropertyValue(ideaToUpdate, params[i], values[i]);
					}
					ideaService.save(ideaToUpdate);
					message = "Idea updated";
				}
			}
		} catch (Exception e) {
			message = "ERROR - Possible format error. Please use correct format - Aborting update";
			logger.error("ERROR - Aborting update ", e.getCause());
		}

		return message;
	}

	//@Scheduled(cron = "0 0 10 ? * *")
	@Scheduled(cron = "0 54 13 ? * *")
	private void fetchIdea() {
		logger.debug("DEBUG - Fetching ideas");
		List<Idea> todaysThings = ideaRepository.fetchByRememberDate(new Date());
		for (Idea idea : todaysThings) {
			sendMessageToUser(idea.getChatId(),
			                  "ID:".concat(String.valueOf(idea.getId())).concat(" message is: " + idea.getMessage()));
			logger.debug("DEBUG - Sending idea now");
		}

	}

	public String getBotUsername() {
		return "AlexKap23Bot";
	}

	public String getBotToken() {
		return "857045337:AAGwlyQ38QB7yfK3XSIzV9auUhI1hHZJHko";
	}

	public String getBotPath() {
		return null;
	}


}
