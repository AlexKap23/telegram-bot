package com.alexandros.teleram.bot.repositories;

import com.alexandros.teleram.bot.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation,String> {


}
