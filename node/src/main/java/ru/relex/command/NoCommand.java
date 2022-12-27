package ru.relex.command;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

public class NoCommand implements Command {
    private final ProducerService producerService;
    public static final String NO_MESSAGE = "Я поддерживаю команды, начинающиеся со слеша(/).\n"
            + "Чтобы посмотреть список команд введите /help";

    public NoCommand(ProducerService producerService) {
        this.producerService = producerService;
    }


    @Override
    public void execute(@NonNull Update update) {
        var chatId = update.getMessage().getChatId();
        producerService.producerAnswer(NO_MESSAGE, chatId);
    }
}