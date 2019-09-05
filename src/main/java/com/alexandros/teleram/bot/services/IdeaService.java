package com.alexandros.teleram.bot.services;

import com.alexandros.teleram.bot.model.Idea;

import java.text.ParseException;

public interface IdeaService extends CrudService<Idea,Long> {

	Object getPropertyValue(Idea idea,String property);
	void setPropertyValue(Idea idea,String propertyValue,Object value) throws ParseException;
}
