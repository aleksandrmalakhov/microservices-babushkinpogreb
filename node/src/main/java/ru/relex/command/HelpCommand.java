package ru.relex.command;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

import static ru.relex.command.enums.CommandName.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HelpCommand implements Command {
    final ProducerService producerService;
    static final String HELP_MSG_USER = String.format("""
                    ✨<b>Дотупные команды</b>✨

                    %s - начать работу со мной
                    %s - приостановить работу со мной
                    %s - помощь в работе со мной
                    %s - отменить команду
                    %s - регистрация
                    %s - мои фотографии
                    %s - мои документы
                    """,
            START.getCommandName(),
            STOP.getCommandName(),
            HELP.getCommandName(),
            CANCEL.getCommandName(),
            REGISTRATION.getCommandName(),
            PHOTO.getCommandName(),
            DOC.getCommandName());

    public HelpCommand(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void execute(@NonNull Update update) {
        var chatId = update.getMessage().getChatId();
        producerService.producerAnswer(HELP_MSG_USER, chatId);
    }
}