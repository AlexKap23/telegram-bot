package com.alexandros.teleram.bot.repositories;


import com.alexandros.teleram.bot.model.Slot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends MongoRepository<Slot,String> {

    @Query(value="{}",fields="{'id' : 1, 'slotName' : 1}")
    List<Slot> findAll();

}
