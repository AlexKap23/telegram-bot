package com.alexandros.teleram.bot.services;

import antlr.StringUtils;
import com.alexandros.teleram.bot.model.Idea;
import com.alexandros.teleram.bot.repositories.IdeaRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IdeaServiceImpl implements IdeaService {
	private final IdeaRepository ideaRepository;
	private static final String ID = "id";
	private static final String MESSAGE = "message";
	private static final String USER_ID = "userId";
	private static final String SUBMIT_DATE = "submitDate";
	private static final String REMEMBER_DATE = "rememberDate";
	private static final String CHAT_ID = "chatId";
	private static final String STATUS = "status";
	private static final String EMPTY_STRING = "";

	public IdeaServiceImpl(IdeaRepository ideaRepository) {
		this.ideaRepository = ideaRepository;
	}

	public Object getPropertyValue(Idea idea, String property) {
		Object value = new Object();
		switch (property) {
			case ID:
				value = idea.getId();
				break;
			case MESSAGE:
				value = idea.getMessage();
				break;
			case USER_ID:
				value = idea.getUserId();
				break;
			case SUBMIT_DATE:
				value = idea.getSubmitDate();
				break;
			case REMEMBER_DATE:
				value = idea.getRememberDate();
				break;
			case CHAT_ID:
				value = idea.getChatId();
				break;
			case STATUS:
				value = idea.getStatus();
				break;
		}
		return value;
	}

	public void setPropertyValue(Idea idea, String property, Object value) throws ParseException {
		SimpleDateFormat formater;
		switch (property) {
			case ID:
				idea.setId((int) value);
				break;
			case MESSAGE:
				idea.setMessage((String) value);
				break;
			case USER_ID:
				idea.setUserId((int) value);
				break;
			case SUBMIT_DATE:
				formater = new SimpleDateFormat("dd/MM/yyyy");
				idea.setSubmitDate(formater.parse((value.toString())));
				break;
			case REMEMBER_DATE:
				formater = new SimpleDateFormat("dd/MM/yyyy");
				idea.setRememberDate(formater.parse((value.toString())));
				break;
			case CHAT_ID:
				idea.setChatId((long) value);
				break;
			case STATUS:
				idea.setStatus((String) value);
				break;
		}
	}

	@Override
	public List<Idea> findAll() {
		List<Idea> ideas = new ArrayList<>();
		ideaRepository.findAll().iterator().forEachRemaining(ideas::add);
		return ideas;
	}

	@Override
	public Idea findById(Long id) {
		Optional<Idea> optionalIdea = ideaRepository.findById(id);
		if (!optionalIdea.isPresent()) {
			throw new RuntimeException("Idea with this id does not exist " + id);
		}
		return optionalIdea.get();
	}

	@Override
	public Idea save(Idea object) {
		if (object != null) {
			ideaRepository.save(object);
			return object;
		} else {
			throw new RuntimeException("Cannot save a null object");
		}
	}

	@Override
	public void delete(Idea object) {
		if (object != null) {
			ideaRepository.delete(object);
		} else {
			throw new RuntimeException("Cannot delete an null object");
		}
	}

	@Override
	public void deleteById(Long id) {
		Optional<Idea> optionalIdea = ideaRepository.findById(id);
		if (optionalIdea.isPresent()) {
			ideaRepository.delete(optionalIdea.get());
		} else {
			throw new RuntimeException("Cannot delete idea with id: " + id);
		}
	}
}
