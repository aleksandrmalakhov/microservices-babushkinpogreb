package ru.relex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.relex.command.Command;
import ru.relex.command.UnknownCommand;
import ru.relex.command.enums.CommandName;
import ru.relex.dao.AppDocumentDAO;
import ru.relex.dao.AppPhotoDAO;
import ru.relex.dao.AppUserDAO;
import ru.relex.service.FileService;
import ru.relex.service.ProducerService;
import ru.relex.service.impl.CommandContainerServiceImp;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CommandContainerTest {
    private CommandContainerServiceImp commandContainer;

    @BeforeEach
    public void init() {
        AppUserDAO appUserDAO = Mockito.mock(AppUserDAO.class);
        AppPhotoDAO appPhotoDAO = Mockito.mock(AppPhotoDAO.class);
        AppDocumentDAO appDocumentDAO = Mockito.mock(AppDocumentDAO.class);
        ProducerService producerService = Mockito.mock(ProducerService.class);
        FileService fileService = Mockito.mock(FileService.class);

        commandContainer = new CommandContainerServiceImp(
                appUserDAO,
                appPhotoDAO,
                appDocumentDAO,
                producerService,
                fileService);
    }

    @Test
    public void shouldGetAllTheExistingCommands() {
        Arrays.stream(CommandName.values())
                .forEach(commandName -> {
                    Command command = commandContainer.retrieveCommand(commandName.getCommandName());

                    assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }

    @Test
    public void shouldReturnUnknownCommand() {
        String unknownCommand = "/fgjhdfgdfg";
        Command command = commandContainer.retrieveCommand(unknownCommand);

        assertEquals(UnknownCommand.class, command.getClass());
    }
}