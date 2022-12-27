package ru.relex.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

public class RegistrationCommand implements Command {
    private final ProducerService producerService;

    public RegistrationCommand(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void execute(Update update) {
        //TODO добавить регистрацию
        var text = "Команда временно недоступна.";
        var chatId = update.getMessage().getChatId();
        producerService.producerAnswer(text, chatId);
    }
}