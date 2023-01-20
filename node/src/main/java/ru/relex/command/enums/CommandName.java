package ru.relex.command.enums;

public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    REGISTRATION("/registration"),
    PHOTO("/myphoto"),
    DOC("/mydoc"),
    CANCEL("/cancel"),
    DELETE("/delete"),
    NO("noCommand"),
    ERROR("error"),
    SET_EMAIL("setEmail");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}