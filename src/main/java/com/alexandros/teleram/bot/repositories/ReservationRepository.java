package com.alexandros.teleram.bot.repositories;

import com.alexandros.teleram.bot.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation,String> {


    @Query("{clientName: '?0'}")
    List<Reservation> findByClientName(String clientName);

    @Query("{status: '?0'}")
    List<Reservation> findByStatus(String status);

    @Query("{dateTime: '?0'}")
    List<Reservation> findByDate(Date datetime);

    @Query("{dateTime: {$gte: ?0, $lte: ?1}}")
    List<Reservation> findByStartEndDate(Date startDate,Date endDate);

    @Query("{dateTime: {$gte: ?0}}")
    List<Reservation> findByDateTimeAfterDate(Date startDate);

    @Query("{dateTime: {$gte: ?0, $lte: ?1},slotId: '?2'}")
    Reservation findByDateAndSlot(Date startDate,Date endDate,String slotName);
}
