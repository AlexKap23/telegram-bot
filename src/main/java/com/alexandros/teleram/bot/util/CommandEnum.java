package com.alexandros.teleram.bot.util;

public enum CommandEnum {

    ADD_USER_INFO("addUserInfo","/addUserInfo"),
    UPDATE_USER_INFO("updateUserInfo","/updateUserInfo"),
    DISPLAY("display","/displayInfo"),
    SHOW_ALL("showAll","/showAll");

    private String commandId;
    private String command;


    CommandEnum(String commandId, String command) {
        this.commandId = commandId;
        this.command = command;
    }

    public static CommandEnum getByCommand(String command) {
        for (CommandEnum constant : CommandEnum.values()) {
            if (constant.getCommand().equalsIgnoreCase(command)) {
                return constant;
            }
        }
        return null;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
