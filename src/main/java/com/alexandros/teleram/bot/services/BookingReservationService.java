package com.alexandros.teleram.bot.services;

import static com.alexandros.teleram.bot.util.Constants.ACCEPTED_MODE;
import static com.alexandros.teleram.bot.util.Constants.ACCEPTED_STATUS;
import static com.alexandros.teleram.bot.util.Constants.ALL;
import static com.alexandros.teleram.bot.util.Constants.PENDING_STATUS;
import static com.alexandros.teleram.bot.util.Constants.REJECTED_STATUS;

import com.alexandros.teleram.bot.dto.ReservationDateTimePayloadDto;
import com.alexandros.teleram.bot.dto.ReservationDto;
import com.alexandros.teleram.bot.dto.ReservationResponseDto;
import com.alexandros.teleram.bot.dto.ResponseDto;
import com.alexandros.teleram.bot.dto.SlotDto;
import com.alexandros.teleram.bot.dto.SlotResponseDto;
import com.alexandros.teleram.bot.model.Reservation;
import com.alexandros.teleram.bot.model.Slot;
import com.alexandros.teleram.bot.repositories.ReservationRepository;
import com.alexandros.teleram.bot.repositories.SlotRepository;
import com.alexandros.teleram.bot.telegram.bot.AlexKapBot;
import com.alexandros.teleram.bot.util.DateUtils;
import com.alexandros.teleram.bot.util.ReservationResponseBuilder;
import com.alexandros.teleram.bot.util.RestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingReservationService {

    @Autowired
    private RestUtils restUtils;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private AlexKapBot bot;
    @Value("${bot.reservation.chat}")
    private long reservationBotChat;

    @Value("${reservation.time.range.min}")
    private int timeRangeInMin;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public ResponseDto createNewReservation(ReservationDto payload){
        ResponseDto response = new ResponseDto();
        if(Objects.isNull(payload)){
            response.setCode(400);
            response.setMessage("No payload received");
        }
        try{
            Date date = DateUtils.parseDate(payload.getDateTime());
            if(Objects.isNull(date)) return ReservationResponseBuilder.buildResponse(500,"date.format.error");
            Reservation reservation = new Reservation(payload.getClientName(),date,payload.getSlot(),PENDING_STATUS);
            reservationRepository.save(reservation);
            String message = "New Reservation has just been added. \nReservation id is "+reservation.getId(); //message could be enhanced. And also should create a template  about the message to avoid hardcoded strings.
            bot.sendMessageToUser(getReservationBotChat(),message);
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
            return ReservationResponseBuilder.buildResponse(400, "No reservation id received");
        }
        try {
            Optional<Reservation> optional = reservationRepository.findById(id);
            Reservation reservation = optional.orElse(null);
            if (Objects.isNull(reservation)) {
                return ReservationResponseBuilder.buildResponse(404, "No reservation found");
            }
            String date = DateUtils.formatDate(reservation.getDateTime());
            return ReservationResponseBuilder.buildReservationResponse(200, StringUtils.EMPTY, reservation.getClientName(), date,
                reservation.getSlotId(), reservation.getId(), reservation.getStatus());
        } catch (Exception e) {
            logger.error("Exception caught while finding reservation by id " + id, e);
            return ReservationResponseBuilder.buildResponse(500, "Service unavailable");
        }
    }

    public ReservationResponseDto approveOrRejectReservation(String reservationId, String mode) {
        if (StringUtils.isEmpty(reservationId)) {
            return ReservationResponseBuilder.buildResponse(400, "No reservation id received");
        }
        try {
            //TODO maybe check if it's already accepted/rejected and not do that again
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

    /**
     * Mode variable is there to help utilize the same method to retrieve the reservations either depending by mode or not*/
    public ReservationResponseDto findAllByMode(String mode) {
        try {
            if (StringUtils.isBlank(mode) || ALL.equalsIgnoreCase(mode)) {
                List<Reservation> reservations = reservationRepository.findAll();
                if (CollectionUtils.isEmpty(reservations)) {
                    return ReservationResponseBuilder.buildResponse(404, "No reservations found");
                }
                ReservationResponseDto responseDto = new ReservationResponseDto();
                responseDto.setReservations(convertEntitiesToDtos(reservations));
                responseDto.setCode(200);
                return responseDto;
            } else {
                List<Reservation> reservations = reservationRepository.findByStatus(mode);
                if(CollectionUtils.isEmpty(reservations)){
                    return ReservationResponseBuilder.buildResponse(404, "No reservations found");
                }
                ReservationResponseDto response = new ReservationResponseDto();
                response.setCode(200);
                response.setReservations(convertEntitiesToDtos(reservations));
                return response;
            }
        } catch (Exception e) {
            logger.error("Exception caught while finding reservations. Mode is " + mode, e);
            return ReservationResponseBuilder.buildResponse(500, "Service unavailable");
        }
    }

    private ReservationDto convertEntityToDto(Reservation reservation){
        //TODO convert to java 8
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setClientName(reservation.getClientName());
        reservationDto.setDateTime(DateUtils.formatDate(reservation.getDateTime()));
        reservationDto.setSlot(reservation.getSlotId());
        reservationDto.setStatus(reservation.getStatus());
        return reservationDto;
    }

    private List<ReservationDto> convertEntitiesToDtos(List<Reservation> reservations){
        //TODO convert to java 8
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for(Reservation reservation:reservations){
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(reservation.getId());
            reservationDto.setClientName(reservation.getClientName());
            reservationDto.setDateTime(DateUtils.formatDate(reservation.getDateTime()));
            reservationDto.setSlot(reservation.getSlotId());
            reservationDto.setStatus(reservation.getStatus());
            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }

    /*Code accepts dates of dd/MM/yyyy hh:mm */
    public ReservationResponseDto findReservationByDate(ReservationDateTimePayloadDto payload){
        if(Objects.isNull(payload)) return ReservationResponseBuilder.buildResponse(500, "Payload is null");
        if(StringUtils.isEmpty(payload.getDate())) return ReservationResponseBuilder.buildResponse(400,"No date found");
        Date dateTime = DateUtils.parseDate(payload.getDate());
        if(Objects.isNull(dateTime)) return ReservationResponseBuilder.buildResponse(500,"Date provided cannot be parsed");
        List<Reservation> reservations = reservationRepository.findByDate(dateTime);
        if(CollectionUtils.isEmpty(reservations)) return ReservationResponseBuilder.buildResponse(200,"No reservations found for given date");
        ReservationResponseDto response = new ReservationResponseDto();
        response.setCode(200);
        response.setReservations(convertEntitiesToDtos(reservations));
        return response;
    }

    /*day=0 means today
     * day=1 means tomorrow
     * day=anything else means every day after the date provided*/
    public ReservationResponseDto findReservationsAfterDate(ReservationDateTimePayloadDto payload) {
        if(Objects.isNull(payload)) return ReservationResponseBuilder.buildResponse(500, "Payload is null");
        Date dateTime = null;
        List<Reservation> reservations = new ArrayList<>();
        if (payload.getDay() == 0) {
            //build end date
            Date now = new Date(System.currentTimeMillis());
            Date endOfDay = DateUtils.buildEndOfDay(now);
            reservations = reservationRepository.findByStartEndDate(now, endOfDay);
        } else if (payload.getDay() == 1) {
            Date tomorrow = DateUtils.buildTomorrow();
            Date tomorrowEndOfDay = DateUtils.buildEndOfDay(tomorrow);
            reservations = reservationRepository.findByStartEndDate(tomorrow, tomorrowEndOfDay);
        } else {
            if (StringUtils.isEmpty(payload.getDate())) {
                return ReservationResponseBuilder.buildResponse(400, "No date found");
            }
            dateTime = DateUtils.parseDate(payload.getDate());
            if (Objects.isNull(dateTime)) {
                return ReservationResponseBuilder.buildResponse(500, "Date provided cannot be parsed");
            }
            reservations = reservationRepository.findByDateTimeAfterDate(dateTime);
        }
        if (CollectionUtils.isEmpty(reservations)) {
            return ReservationResponseBuilder.buildResponse(404, "No reservations found");
        }
        ReservationResponseDto response = new ReservationResponseDto();
        response.setCode(200);
        response.setReservations(convertEntitiesToDtos(reservations));
        return response;
    }

    public boolean checkIfSlotIsAvailable(String slotName,Date date){
        //checking X min before and X min after the provided date. This could be parameterized based on clients needs.
        if(Objects.nonNull(date)){
            Date[] timeRange = DateUtils.buildTimeRangeDates(date,getTimeRangeInMin());
            Reservation reservation = reservationRepository.findByDateAndSlot(timeRange[0],timeRange[1],slotName);
            return Objects.isNull(reservation);
        }
        return false;
    }


    /* Finding all slots registered. And we characterize them based on if there is a reservation on the given type for the same slot*/
    public SlotResponseDto findSlots(){
        SlotResponseDto response = new SlotResponseDto();
        try{
            List<Slot> slots = slotRepository.findAll();
            List<SlotDto> slotDtos = new ArrayList<>();
            slots.forEach(slot->{
                SlotDto slotDto = new SlotDto();
                slotDto.setSlotId(slot.getId());
                slotDto.setSlotName(slot.getSlotName());
                slotDto.setAvailable(checkIfSlotIsAvailable(slot.getSlotName(),new Date(System.currentTimeMillis())));
                slotDtos.add(slotDto);
            });
            response.setCode(200);
            response.setSlots(slotDtos);
        }catch (Exception e){
            logger.error("Exception caught while finding slots for reservation",e);
            response.setCode(500);
            response.setMessage("Exception caught while finding slots");
        }
        return response;
    }

    public ResponseDto createNewSlot(SlotDto payload){
        ResponseDto response = new ResponseDto();
        if(Objects.isNull(payload)){
            response.setCode(400);
            response.setMessage("No payload received");
        }
        try{
            Slot newSlot = new Slot(payload.getSlotName(),payload.getSlotDescription());
            slotRepository.save(newSlot);
            response.setCode(200);
        }catch (Exception e){
            logger.error("Exception caught while saving new slot",e);
            response.setCode(500);
            response.setMessage("Exception caught while saving new slot");
        }
        return response;
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

    public AlexKapBot getBot() {
        return bot;
    }

    public void setBot(AlexKapBot bot) {
        this.bot = bot;
    }

    public long getReservationBotChat() {
        return reservationBotChat;
    }

    public void setReservationBotChat(long reservationBotChat) {
        this.reservationBotChat = reservationBotChat;
    }

    public SlotRepository getSlotRepository() {
        return slotRepository;
    }

    public void setSlotRepository(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public int getTimeRangeInMin() {
        return timeRangeInMin;
    }

    public void setTimeRangeInMin(int timeRangeInMin) {
        this.timeRangeInMin = timeRangeInMin;
    }
}
