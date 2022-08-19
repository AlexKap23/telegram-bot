package com.alexandros.teleram.bot.services;

import static com.alexandros.teleram.bot.util.Constants.DASH;
import static com.alexandros.teleram.bot.util.Constants.EXCEPTION_ADD_USER_INFO;
import static com.alexandros.teleram.bot.util.Constants.EXCEPTION_SHOW_ALL;
import static com.alexandros.teleram.bot.util.Constants.EXCEPTION_SHOW_BY_NAME;
import static com.alexandros.teleram.bot.util.Constants.MESSAGE_IS_EMPTY;
import static com.alexandros.teleram.bot.util.Constants.NO_USERS_SAVED;
import static com.alexandros.teleram.bot.util.Constants.NO_USER_BY_NAME;
import static com.alexandros.teleram.bot.util.Constants.SLASH;
import static com.alexandros.teleram.bot.util.Constants.SUCCESS_ADD_USER_INFO;

import com.alexandros.teleram.bot.model.UserInfo;
import com.alexandros.teleram.bot.repositories.UserInfoRepository;
import com.alexandros.teleram.bot.util.CommandEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class CommandProcessService {

    private final UserInfoRepository userInfoRepository;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public CommandProcessService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public String executeCommand(String message){
        String response = null;
        if(StringUtils.isNotEmpty(message) && !message.startsWith(SLASH)) return null;
        String command = extractCommandFromMessage(message);
        CommandEnum commandEnum = CommandEnum.getByCommand(command);
        if(Objects.isNull(commandEnum)){
            return null;
        }
        String commandId = commandEnum.getCommandId();
        if (CommandEnum.SHOW_ALL.getCommandId().equalsIgnoreCase(commandId)) {
            response = executeShowAll();
        }else if(CommandEnum.ADD_USER_INFO.getCommandId().startsWith(commandId)){
            response = executeAddNewUserInfo(message);
        }else if(CommandEnum.SHOW.getCommandId().startsWith(commandId)){
            response = executeShowByName(message);
        }
        return response;
    }

    private String executeShowAll(){
        String response = null;
        try{
            List<UserInfo> allAvailableUsers = userInfoRepository.findAll();
            if(CollectionUtils.isEmpty(allAvailableUsers)) return NO_USERS_SAVED;
            for(UserInfo userInfo: allAvailableUsers){
                response = Objects.isNull(response)?userInfo.toString():response.concat(userInfo.toString());
            }
        }catch (Exception e){
            logger.error("Exception caught while executing show all",e);
            response = EXCEPTION_SHOW_ALL;
        }
        return response;
    }

    private String executeAddNewUserInfo(String message) {
        if (StringUtils.isEmpty(message)) {
            return MESSAGE_IS_EMPTY;
        }
        try{
            String[] userInfoParts = extractUserInfo(message);
            UserInfo userInfo = new UserInfo(RandomStringUtils.random(10, true, true), userInfoParts[0], userInfoParts[1], userInfoParts[2],
                userInfoParts[3], userInfoParts[4]);
            userInfoRepository.save(userInfo);
            return SUCCESS_ADD_USER_INFO;
        }catch (Exception e){
            logger.error("Exception caught while adding new user into db",e);
            return EXCEPTION_ADD_USER_INFO;
        }
    }

    private String extractCommandFromMessage(String message){
        String[] messageParts = message.split("\\s+");
        return messageParts[0];
    }

    private String[] extractUserInfo(String message){
        String[] messageParts = message.split("\\s+");
        String userInfoToken = messageParts[1];
        String[] tokenParts = userInfoToken.split(DASH);
        return tokenParts;
    }

    private String executeShowByName(String message){
        String response = null;
        if (StringUtils.isEmpty(message)){
            response = MESSAGE_IS_EMPTY;
        }
        try{
            String[] messageParts = message.split("\\s+");
            String name = messageParts[1];
            List<UserInfo> userInfos = userInfoRepository.findByName(name);
            if(CollectionUtils.isEmpty(userInfos)) response = NO_USER_BY_NAME;
            for(UserInfo userInfo: userInfos){
                response = Objects.isNull(response)?userInfo.toString():response.concat(userInfo.toString());
            }
        }catch (Exception e){
            logger.error("Exception caught while adding new user into db",e);
            response = EXCEPTION_SHOW_BY_NAME;
        }
        return response;
    }


}
