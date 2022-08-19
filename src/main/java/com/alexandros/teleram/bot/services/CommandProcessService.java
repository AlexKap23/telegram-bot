package com.alexandros.teleram.bot.services;

import com.alexandros.teleram.bot.model.UserInfo;
import com.alexandros.teleram.bot.repositories.UserInfoRepository;
import com.alexandros.teleram.bot.util.CommandEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class CommandProcessService {

    private final UserInfoRepository userInfoRepository;

    public CommandProcessService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public String executeCommand(String command){
        String response = null;
        CommandEnum commandEnum = CommandEnum.getByCommand(command);
        if(Objects.isNull(commandEnum)){
            return null;
        }
        String commandId = commandEnum.getCommandId();

        if(CommandEnum.SHOW_ALL.getCommandId().equalsIgnoreCase(commandId)){
            List<UserInfo> allAvailableUsers = userInfoRepository.findAll();
            if(CollectionUtils.isEmpty(allAvailableUsers)) return response;
            for(UserInfo userInfo: allAvailableUsers){
                response = Objects.isNull(response)?userInfo.toString():response.concat(userInfo.toString());
            }
        }

        return response;
    }

}
