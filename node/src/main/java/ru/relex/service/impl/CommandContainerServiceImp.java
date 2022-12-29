package ru.relex.service.impl;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.relex.command.*;
import ru.relex.dao.AppDocumentDAO;
import ru.relex.dao.AppPhotoDAO;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.CommandContainerService;
import ru.relex.service.FileService;
import ru.relex.service.ProducerService;

import static ru.relex.command.enums.CommandName.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandContainerServiceImp implements CommandContainerService {
    final Command unknownCommand;
    final ImmutableMap<String, Command> commandMap;

    public CommandContainerServiceImp(AppUserDAO appUserDAO,
                                      AppPhotoDAO appPhotoDAO,
                                      AppDocumentDAO appDocumentDAO,
                                      ProducerService producerService,
                                      FileService fileService) {
        this.commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(appUserDAO, producerService))
                .put(STOP.getCommandName(), new StopCommand(appUserDAO, producerService))
                .put(HELP.getCommandName(), new HelpCommand(producerService))
                .put(CANCEL.getCommandName(), new CancelCommand(appUserDAO, producerService))
                .put(REGISTRATION.getCommandName(), new RegistrationCommand(producerService))
                .put(PHOTO.getCommandName(), new MyPhotoCommand(appUserDAO, appPhotoDAO, producerService, fileService))
                .put(DOC.getCommandName(), new MyDocCommand(appUserDAO, appDocumentDAO, fileService, producerService))
                .put(NO.getCommandName(), new NoCommand(producerService))
                .put(ERROR.getCommandName(), new ErrorCommand(producerService))
                .build();
        this.unknownCommand = new UnknownCommand(producerService);
    }

    @Override
    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}