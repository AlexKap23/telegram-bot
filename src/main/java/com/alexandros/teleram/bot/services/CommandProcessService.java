package com.alexandros.teleram.bot.services;

import static com.alexandros.teleram.bot.util.Constants.DASH;

import com.alexandros.teleram.bot.model.UserInfo;
import com.alexandros.teleram.bot.repositories.UserInfoRepository;
import com.alexandros.teleram.bot.util.CommandEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class CommandProcessService {

    private final UserInfoRepository userInfoRepository;

    public CommandProcessService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public String executeCommand(String message){
        String response = null;
        String command = extractCommandFromMessage(message);
        CommandEnum commandEnum = CommandEnum.getByCommand(command);
        if(Objects.isNull(commandEnum)){
            return null;
        }
        String commandId = commandEnum.getCommandId();

        if (CommandEnum.SHOW_ALL.getCommandId().equalsIgnoreCase(commandId)) {
            response = executeShowAll();
        }else if(CommandEnum.ADD_USER_INFO.getCommandId().startsWith(commandId)){
            executeAddNewUserInfo(message);
        }

        return response;
    }

    private String executeShowAll(){
        String response = null;
        List<UserInfo> allAvailableUsers = userInfoRepository.findAll();
        if(CollectionUtils.isEmpty(allAvailableUsers)) return null;
        for(UserInfo userInfo: allAvailableUsers){
            response = Objects.isNull(response)?userInfo.toString():response.concat(userInfo.toString());
        }
        return response;
    }

    private String executeAddNewUserInfo(String message) {
        if (StringUtils.isEmpty(message)) {
            return null;
        }
        String[] userInfoParts = extractUserInfo(message);
        UserInfo userInfo = new UserInfo(RandomStringUtils.random(10, true, true), userInfoParts[0], userInfoParts[1], userInfoParts[2],
            userInfoParts[3], userInfoParts[4]);
        userInfoRepository.save(userInfo);
        return null;
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


}
