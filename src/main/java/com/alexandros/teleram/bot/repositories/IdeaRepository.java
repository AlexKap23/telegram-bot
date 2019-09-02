package com.alexandros.teleram.bot.repositories;

import com.alexandros.teleram.bot.model.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea,Long>,CrudRepository<Idea,Long> {
	List<Idea> fetchByRememberDate(@Param("date") Date date);
}
