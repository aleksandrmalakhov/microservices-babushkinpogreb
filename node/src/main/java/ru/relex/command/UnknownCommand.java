package ru.relex.command;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnknownCommand implements Command {
    final ProducerService producerService;
    public static final String UNKNOWN_MESSAGE = "Не понимаю вас \uD83D\uDE1F, напишите /help чтобы узнать что я понимаю.";

    public UnknownCommand(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void execute(@NonNull Update update) {
        var chatId = update.getMessage().getChatId();
        producerService.producerAnswer(UNKNOWN_MESSAGE, chatId);
    }
}