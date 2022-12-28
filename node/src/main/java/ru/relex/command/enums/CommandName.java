package ru.relex.command.enums;

public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    REGISTRATION("/registration"),
    PHOTO("/myphoto"),
    CANCEL("/cancel"),
    NO("noCommand"),
    ERROR("error");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}