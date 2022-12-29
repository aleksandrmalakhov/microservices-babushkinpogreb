package ru.relex.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorCommand implements Command {
    final ProducerService producerService;

    public ErrorCommand(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void execute(Update update) {
        var text = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        var chatId = update.getMessage().getChatId();

        producerService.producerAnswer(text, chatId);
    }
}