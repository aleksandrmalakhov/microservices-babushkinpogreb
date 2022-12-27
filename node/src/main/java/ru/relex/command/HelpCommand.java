package ru.relex.command;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

import static ru.relex.command.enums.CommandName.*;

public class HelpCommand implements Command {
    private final ProducerService producerService;
    private static final String HELP_MSG_USER = String.format("""
                    ✨<b>Дотупные команды</b>✨

                    %s - начать работу со мной
                    %s - приостановить работу со мной
                    %s - помощь в работе со мной
                    %s - отменить команду
                    %s - регистрация
                    """,
            START.getCommandName(),
            STOP.getCommandName(),
            HELP.getCommandName(),
            CANCEL.getCommandName(),
            REGISTRATION.getCommandName());

    public HelpCommand(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void execute(@NonNull Update update) {
        var chatId = update.getMessage().getChatId();
        producerService.producerAnswer(HELP_MSG_USER, chatId);
    }
}