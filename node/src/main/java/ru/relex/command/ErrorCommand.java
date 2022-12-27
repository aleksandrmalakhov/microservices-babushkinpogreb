package ru.relex.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

public class ErrorCommand implements Command {
    private final ProducerService producerService;

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