package com.alexandros.teleram.bot.repositories;


import com.alexandros.teleram.bot.model.Slot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends MongoRepository<Slot,String> {

}
