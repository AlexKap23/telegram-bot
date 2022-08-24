package com.alexandros.teleram.bot.services;

import static com.alexandros.teleram.bot.util.Constants.ACCEPTED_MODE;
import static com.alexandros.teleram.bot.util.Constants.ACCEPTED_STATUS;
import static com.alexandros.teleram.bot.util.Constants.INSERT_OPERATION_TYPE;
import static com.alexandros.teleram.bot.util.Constants.OPERATION_TYPE;
import static com.alexandros.teleram.bot.util.Constants.PENDING_STATUS;
import static com.alexandros.teleram.bot.util.Constants.REJECTED_STATUS;
import static com.alexandros.teleram.bot.util.Constants.RESERVATIONS_COLLECTION_NAME;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.in;
import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.alexandros.teleram.bot.dto.ReservationResponseDto;
import com.alexandros.teleram.bot.dto.ResponseDto;
import com.alexandros.teleram.bot.model.Reservation;
import com.alexandros.teleram.bot.repositories.ReservationRepository;
import com.alexandros.teleram.bot.util.ReservationResponseBuilder;
import com.alexandros.teleram.bot.util.RestUtils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.apache.commons.lang3.StringUtils;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class BookingReservationService {

    @Autowired
    private RestUtils restUtils;
    @Autowired
    private ReservationRepository reservationRepository;

    @Value("${mongo.db.name}")
    private String mongoDbName;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUrl;
    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public ResponseDto createNewReservation(ReservationResponseDto payload){
        ResponseDto response = new ResponseDto();
        if(Objects.isNull(payload)){
            response.setCode(400);
            response.setMessage("No payload received");
        }
        try{
            Reservation reservation = new Reservation(payload.getClientName(),payload.getDateTime(),payload.getSlot(),PENDING_STATUS);
            reservationRepository.save(reservation);
            response.setCode(200);
        }catch (Exception e){
            logger.error("Exception caught while adding reservation",e);
            response.setCode(500);
            response.setMessage("Service unavailable");
        }
        return response;
    }

    public ReservationResponseDto findReservationById(String id) {
        if (StringUtils.isBlank(id)) {
            return ReservationResponseBuilder.buildResponse(400,"No reservation id received");
        }
        try {
            Optional<Reservation> optional = reservationRepository.findById(id);
            Reservation reservation = optional.orElse(null);
            if(Objects.isNull(reservation)){
                return ReservationResponseBuilder.buildResponse(404,"No reservation found");
            }
            return ReservationResponseBuilder.buildReservationResponse(200,StringUtils.EMPTY,reservation.getClientName(),reservation.getDateTime(),reservation.getSlotId(),reservation.getId(),reservation.getStatus());
        } catch (Exception e) {
            logger.error("Exception caught while finding reservation by id "+id, e);
            return ReservationResponseBuilder.buildResponse(500,"Service unavailable");
        }
    }

    public ReservationResponseDto approveOrRejectReservation(String reservationId, String mode) {
        if (StringUtils.isEmpty(reservationId)) {
            return ReservationResponseBuilder.buildResponse(400, "No reservation id received");
        }
        try {
            Optional<Reservation> optional = reservationRepository.findById(reservationId);
            Reservation reservation = optional.orElse(null);
            reservation.setStatus(
                StringUtils.isNotEmpty(mode) ? ACCEPTED_MODE.equalsIgnoreCase(mode) ? ACCEPTED_STATUS : REJECTED_STATUS : PENDING_STATUS);
            reservationRepository.save(reservation);
            return ReservationResponseBuilder.buildResponse(200, StringUtils.EMPTY);
        } catch (Exception e) {
            logger.error("Exception caught while updating reservation by id " + reservationId, e);
            return ReservationResponseBuilder.buildResponse(500, "Service unavailable");
        }
    }

    public void listenForNewReservations(){
        ConnectionString connectionString = new ConnectionString(getMongoUrl());
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .build();
        try{
            MongoClient mongoClient = MongoClients.create(clientSettings);
            MongoDatabase db = mongoClient.getDatabase(getMongoDbName());
            MongoCollection<Reservation> reservations = db.getCollection(RESERVATIONS_COLLECTION_NAME, Reservation.class);
            List<Bson> pipeline = singletonList(match(in(OPERATION_TYPE, Arrays.asList(INSERT_OPERATION_TYPE))));
            ChangeStreamIterable<Reservation> changes = reservations.watch(pipeline);
            for(ChangeStreamDocument reservation:changes){
                reservation.getDocumentKey();
            }
            mongoClient.close();
        }catch (Exception e){
            logger.error("Exception caught while listening for new reservations.");
        }
    }

    public RestUtils getRestUtils() {
        return restUtils;
    }

    public void setRestUtils(RestUtils restUtils) {
        this.restUtils = restUtils;
    }

    public ReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public String getMongoDbName() {
        return mongoDbName;
    }

    public void setMongoDbName(String mongoDbName) {
        this.mongoDbName = mongoDbName;
    }

    public String getMongoUrl() {
        return mongoUrl;
    }

    public void setMongoUrl(String mongoUrl) {
        this.mongoUrl = mongoUrl;
    }
}
