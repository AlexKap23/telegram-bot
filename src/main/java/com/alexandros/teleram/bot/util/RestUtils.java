package com.alexandros.teleram.bot.util;

import com.alexandros.teleram.bot.dto.ResponseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Component
public class RestUtils {

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public Object getObjectFromJson(String input, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        Object dto = null;
        try {
            dto = mapper.readValue(input, clazz);
        } catch (IOException e) {
            logger.error("Error occurred during build request object from json.",e);
        }
        return dto;
    }

    public Object getObjectFromJsonIgnoreUnknown(String input, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object dto = null;
        try {
            dto = mapper.readValue(input, clazz);
        } catch (IOException e) {
            logger.error("Error occurred during build request object from json.",e);
        }
        return dto;
    }

    public String getJsonFromObject(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Error occurred during build json from object",e);
        }
        return json;
    }

    public String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        return stringBuilder.toString();
    }

    @NotNull
    public ResponseEntity getResponseEntity(ResponseDto reservation) {
        if(Objects.nonNull(reservation)){
            if(reservation.getCode()>=200 && reservation.getCode()<200){
                return ResponseEntity.ok(reservation);
            }else if(reservation.getCode()>=500 && reservation.getCode()<599){
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(reservation);
            } else if (reservation.getCode()>=400 && reservation.getCode()<499) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reservation);
            }
        }
        return ResponseEntity.ok().build();
    }

}
