package com.alexandros.teleram.bot.util;

public enum CommandEnum {

    ADD_USER_INFO("addUserInfo","/adduserinfo","/adduserinfo <name>-<afm>-<amka>-<mobilePhone>-<email>"),
    UPDATE_USER_INFO("updateUserInfo","/updateuserinfo",""),
    SHOW_ALL("showAll","/showall","/showall"),
    SHOW("show","/show","/show <name>"),
    HELP("help","/help","/help");

    private String commandId;
    private String command;
    private String template;

    CommandEnum(String commandId, String command,String template) {
        this.commandId = commandId;
        this.command = command;
        this.template = template;
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

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
