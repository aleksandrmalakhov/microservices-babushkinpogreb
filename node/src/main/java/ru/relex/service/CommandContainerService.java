package ru.relex.service;

import ru.relex.command.Command;

public interface CommandContainerService {
    Command retrieveCommand(String commandIdentifier);
}