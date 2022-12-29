package ru.relex.command;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.service.ProducerService;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationCommand implements Command {
    final ProducerService producerService;

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